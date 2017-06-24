package org.eurofurence.connavigator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.github.lzyzsd.circleprogress.ArcProgress
import com.pawegio.kandroid.displayHeight
import com.pawegio.kandroid.displayWidth
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.windowManager
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, AnkoLogger {
    val announcementsRecycler: RecyclerView by view()
    val announcementsTitle: TextView by view()
    val database: Database get() = letRoot { it.database }!!
    val preferences: SharedPreferences get() = letRoot { it.preferences }!!
    val progressBar : ArcProgress by view()

    val upcoming by lazy { EventRecyclerFragment(database.filterEvents().isUpcoming()) }
    val current by lazy { EventRecyclerFragment(database.filterEvents().isCurrent()) }
    val favourited by lazy { EventRecyclerFragment(database.filterEvents().isFavorited()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_home, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Home")

        applyOnRoot { changeTitle("Home") }

        configureProgressBar()

        fragmentManager.beginTransaction()
                .replace(R.id.upcomingEventRecycler, upcoming)
                .replace(R.id.currentEventsRecycler, current)
                .replace(R.id.favouritedEventsRecycler, favourited)
                .commitAllowingStateLoss()

        val now = DateTime.now()
        var announcements = database.announcementDb.items.filterIf(
                !preferences.getBoolean(this.getString(R.string.announcement_show_old), false),
                { it.validFromDateTimeUtc.time <= now.millis && it.validUntilDateTimeUtc.time > now.millis }
        )

        announcementsRecycler.adapter = AnnoucementRecyclerDataAdapter(announcements.sortedByDescending { it.lastChangeDateTimeUtc }.toList())
        announcementsRecycler.layoutManager = NonScrollingLinearLayout(activity)
        announcementsRecycler.itemAnimator = DefaultItemAnimator()

        if (announcements.count() == 0) {
            announcementsRecycler.visibility = View.GONE
            announcementsTitle.visibility = View.GONE
        }
    }

    private fun configureProgressBar() {
        info { "configuring progress bar"}
        val now = DateTime.now()
        val lastConDay = DateTime(1471471200000)
        val nextConDay = DateTime(1502834400000)

        val totalDaysBetween = Days.daysBetween(lastConDay, nextConDay)
        val totalDaysToNextCon = Days.daysBetween(now, nextConDay)

        info { "Days between cons : ${totalDaysBetween.days}"}
        info { "Days to next con: ${totalDaysToNextCon.days}"}



        progressBar.max  = totalDaysBetween.days
        progressBar.progress = totalDaysToNextCon.days
        progressBar.layoutParams = LinearLayout.LayoutParams(
                context.displayWidth,
                context.displayWidth
        )

        if(totalDaysToNextCon.days <= 0 ){
            progressBar.visibility = View.GONE
        }
    }

    override fun dataUpdated() {
        logd { "Updating home screen data" }
        catchToAnyException {
            updateContents()
        }
    }

    private fun updateContents() {
        val now = DateTime.now()
        val announcements = database.announcementDb.items.filterIf(
                !preferences.getBoolean(this.getString(R.string.announcement_show_old), false),
                { it.validFromDateTimeUtc.time <= now.millis && it.validUntilDateTimeUtc.time > now.millis })
                .sortedByDescending { it.lastChangeDateTimeUtc }

        announcementsRecycler.adapter = AnnoucementRecyclerDataAdapter(announcements)
        announcementsRecycler.adapter.notifyDataSetChanged()

        upcoming.dataUpdated()
        current.dataUpdated()
        favourited.dataUpdated()

        if (announcements.isEmpty()) {
            announcementsRecycler.visibility = View.GONE
            announcementsTitle.visibility = View.GONE
        }
    }
}