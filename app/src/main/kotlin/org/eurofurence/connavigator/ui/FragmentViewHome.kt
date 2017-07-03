package org.eurofurence.connavigator.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.lzyzsd.circleprogress.ArcProgress
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.arcProgress
import org.eurofurence.connavigator.util.extensions.filterIf
import org.eurofurence.connavigator.util.extensions.recycler
import org.jetbrains.anko.*
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, AnkoLogger {
    lateinit var ui: HomeUi

    val database by lazy { locateDb() }
    val now by lazy { DateTime.now() }

    val announcements by lazy {
        database.announcements.items.filterIf(
                !AppPreferences.showOldAnnouncements,
                { it.validFromDateTimeUtc.time <= now.millis && it.validUntilDateTimeUtc.time > now.millis }
        )
    }


    val upcoming by lazy { EventRecyclerFragment(EventList(database).isUpcoming(), "Upcoming events") }
    val current by lazy { EventRecyclerFragment(EventList(database).isCurrent(), "Current events") }
    val favourited by lazy { EventRecyclerFragment(EventList(database).isFavorited(), "Your favorited events") }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container!!

        info { "Initializing home view" }
        ui = HomeUi()

        return ui.createView(AnkoContext.Companion.create(container.context, container))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Home")

        applyOnRoot { changeTitle("Home") }

        configureProgressBar()
        configureAnnouncements()
        configureEventRecyclers()

    }

    private fun configureEventRecyclers() {
        info { "Configuring event recyclers" }

        fragmentManager.beginTransaction()
                .replace(5000, upcoming)
                .replace(5001, current)
                .replace(5002, favourited)
                .commitAllowingStateLoss()
    }

    private fun configureAnnouncements() {
        info { "Configuring announcement recycler" }
        info { "There are ${announcements.count()} announcements" }
        ui.announcementsRecycler.adapter = AnnoucementRecyclerDataAdapter(
                announcements.sortedByDescending { it.lastChangeDateTimeUtc }
                        .toList()
        )
        ui.announcementsRecycler.layoutManager = NonScrollingLinearLayout(activity)
        ui.announcementsRecycler.itemAnimator = DefaultItemAnimator()

        if (announcements.count() == 0) {
            ui.announcementsTitle.text = "There's no announcements right now :)"
            ui.announcementsRecycler.visibility = View.GONE
        }
    }

    private fun configureProgressBar() {
        info { "configuring progress bar" }
        val lastConDay = DateTime(RemotePreferences.lastConEnd)
        val nextConDay = DateTime(RemotePreferences.nextConStart)

        val totalDaysBetween = Days.daysBetween(lastConDay, nextConDay)
        val totalDaysToNextCon = Days.daysBetween(now, nextConDay)

        info { "Days between cons : ${totalDaysBetween.days}" }
        info { "Days to next con: ${totalDaysToNextCon.days}" }

        ui.countdownArc.max = totalDaysBetween.days
        ui.countdownArc.progress = totalDaysToNextCon.days

        if (totalDaysToNextCon.days <= 0) {
            info { "Hiding countdown to next con" }
            ui.countdownArc.visibility = View.GONE
        }
    }
}

class HomeUi : AnkoComponent<ViewGroup> {
    lateinit var countdownArc: ArcProgress
    lateinit var announcementsTitle: TextView
    lateinit var announcementsRecycler: RecyclerView

    lateinit var upcomingFragment: ViewGroup

    lateinit var currentFragment: ViewGroup

    lateinit var favoritedFragment: ViewGroup

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                themedTextView{
                    visibility = if(AuthPreferences.isLoggedIn()) View.VISIBLE else View.GONE
                    text = "Hello ${AuthPreferences.username}"
                }
                lparams(matchParent, matchParent)
                countdownArc = arcProgress {
                    lparams(matchParent, dip(400))
                    strokeWidth = 25F
                    suffixText = "Days"
                    bottomText = "Until next EF"
                    bottomTextSize = dip(20F).toFloat()
                    suffixTextSize = dip(20F).toFloat()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        finishedStrokeColor = ctx.getColor(R.color.accentLight)
                        unfinishedStrokeColor = ctx.getColor(R.color.primary)
                        textColor = ctx.getColor(R.color.textBlack)
                    }
                }

                announcementsTitle = textView("Latest announcements").lparams(matchParent, wrapContent) {
                    padding = dip(15)
                }

                announcementsRecycler = recycler {
                    lparams(matchParent, wrapContent)
                }

                upcomingFragment = linearLayout {
                    id = 5000
                    lparams(matchParent, wrapContent)
                }

                currentFragment = linearLayout {
                    id = 5001
                    lparams(matchParent, wrapContent)
                }

                favoritedFragment = linearLayout {
                    id = 5002
                    lparams(matchParent, wrapContent)
                }
            }
        }
    }
}