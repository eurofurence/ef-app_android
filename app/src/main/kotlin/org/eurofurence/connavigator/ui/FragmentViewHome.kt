package org.eurofurence.connavigator.ui

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.lzyzsd.circleprogress.ArcProgress
import com.pawegio.kandroid.displayWidth
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.extensions.*
import org.jetbrains.anko.*
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, AnkoLogger {
    lateinit var ui: HomeUi

    val database: Database get() = letRoot { it.database }!!
    val now by lazy { DateTime.now() }

    val announcements by lazy {
        database.announcementDb.items.filterIf(
                !AppPreferences.showOldAnnouncements,
                { it.validFromDateTimeUtc.time <= now.millis && it.validUntilDateTimeUtc.time > now.millis }
        )
    }


    val upcoming by lazy { EventRecyclerFragment(database.filterEvents().isUpcoming()) }
    val current by lazy { EventRecyclerFragment(database.filterEvents().isCurrent()) }
    val favourited by lazy { EventRecyclerFragment(database.filterEvents().isFavorited()) }

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
            ui.announcementsRecycler.visibility = View.GONE
            ui.announcementsRecycler.visibility = View.GONE
        }
    }

    private fun configureProgressBar() {
        info { "configuring progress bar" }
        val lastConDay = DateTime(1471471200000)
        val nextConDay = DateTime(1502834400000)

        val totalDaysBetween = Days.daysBetween(lastConDay, nextConDay)
        val totalDaysToNextCon = Days.daysBetween(now, nextConDay)

        info { "Days between cons : ${totalDaysBetween.days}" }
        info { "Days to next con: ${totalDaysToNextCon.days}" }

        ui.countdownArc.max = totalDaysBetween.days
        ui.countdownArc.progress = totalDaysToNextCon.days
        ui.countdownArc.layoutParams = LinearLayout.LayoutParams(
                context.displayWidth,
                context.displayWidth
        )

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

                lparams(matchParent, matchParent)
                countdownArc = arcProgress {
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
                    padding = dip(50)
                }

                announcementsTitle = textView("Latest announcements").lparams(matchParent, wrapContent) {
                    padding = dip(15)
                }.applyRecursively { android.R.style.TextAppearance_Large }

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