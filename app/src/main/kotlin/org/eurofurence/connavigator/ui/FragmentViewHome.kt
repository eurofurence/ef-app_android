package org.eurofurence.connavigator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.filterEvents
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.joda.time.DateTime

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    val announcementsRecycler: RecyclerView by view()
    val announcementsTitle: TextView by view()
    val preferences: SharedPreferences get() = letRoot { it.preferences }!!

    val upcoming by lazy { EventRecyclerFragment(filterEvents().isUpcoming()) }
    val current by lazy { EventRecyclerFragment(filterEvents().isCurrent()) }
    val favourited by lazy { EventRecyclerFragment(filterEvents().isFavorited()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_home, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen("Home")

        applyOnRoot { changeTitle("Home") }

        fragmentManager.beginTransaction()
                .replace(R.id.upcomingEventRecycler, upcoming)
                .replace(R.id.currentEventsRecycler, current)
                .replace(R.id.favouritedEventsRecycler, favourited)
                .commitAllowingStateLoss()

        val now = DateTime.now()
        var announcements = announcements.items.filterIf(
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

    override fun dataUpdated() {
        logd { "Updating home screen data" }
        catchToAnyException {
            updateContents()
        }
    }

    private fun updateContents() {
        val now = DateTime.now()
        val announcements = announcements.items.filterIf(
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