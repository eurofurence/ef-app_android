package org.eurofurence.connavigator.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.lzyzsd.circleprogress.ArcProgress
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.DataChanged
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.fragments.AnnouncementListFragment
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.fragments.UserStatusFragment
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.arcProgress
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.dip
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.imageView
import org.jetbrains.anko.info
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, AnkoLogger {
    lateinit var ui: HomeUi

    val database by lazy { locateDb() }
    val now by lazy { DateTime.now() }

    val upcoming by lazy { EventRecyclerFragment(EventList(database).isUpcoming().sortByStartTime(), "Upcoming events", false) }
    val current by lazy { EventRecyclerFragment(EventList(database).isCurrent().sortByStartTime(), "Running events", false) }
    val favorited by lazy { EventRecyclerFragment(EventList(database).isFavorited().sortByDateAndTime(), "Favorited events", false, true) }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container!!

        info { "Initializing home view" }
        ui = HomeUi()

        return ui.createView(AnkoContext.create(container.context.applicationContext, container))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        applyOnRoot { changeTitle("Home") }

        configureProgressBar()
        configureEventRecyclers()
    }

    private fun configureEventRecyclers() {
        info { "Configuring event recyclers" }

        fragmentManager.beginTransaction()
                .replace(5000, current)
                .replace(5001, upcoming)
                .replace(5002, favorited)
                .replace(5003, AnnouncementListFragment())
                .replace(5004, UserStatusFragment())
                .commitAllowingStateLoss()
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
            ui.countdownLayout.visibility = View.GONE
        }
    }
}

class HomeUi : AnkoComponent<ViewGroup> {
    lateinit var countdownArc: ArcProgress
    lateinit var countdownLayout: LinearLayout

    lateinit var upcomingFragment: ViewGroup
    lateinit var currentFragment: ViewGroup
    lateinit var favoritesFragment: ViewGroup
    lateinit var announcementFragment: ViewGroup
    lateinit var loginWidget: ViewGroup

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                lparams(matchParent, matchParent)

                imageView(R.drawable.banner_2018) {
                    adjustViewBounds = true
                    setBackgroundColor(Color.WHITE)
                    ViewCompat.setElevation(this, 15f)
                }.lparams(matchParent, wrapContent)

                loginWidget = linearLayout {
                    id = 5004
                    lparams(matchParent, wrapContent)
                }

                countdownLayout = linearLayout {
                    countdownArc = arcProgress {
                        lparams(matchParent, displayMetrics.widthPixels - dip(2 * 20))
                        strokeWidth = 25F
                        suffixText = "Days"
                        bottomText = "Until next EF"
                        bottomTextSize = dip(20F).toFloat()
                        suffixTextSize = dip(20F).toFloat()

                        finishedStrokeColor = ContextCompat.getColor(ctx, R.color.accentLight)
                        unfinishedStrokeColor = ContextCompat.getColor(ctx, R.color.primary)
                        textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                    }
                    padding = dip(20)
                }

                announcementFragment = linearLayout {
                    id = 5003
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

                favoritesFragment = linearLayout {
                    id = 5002
                    lparams(matchParent, wrapContent)
                }
            }
        }
    }
}
