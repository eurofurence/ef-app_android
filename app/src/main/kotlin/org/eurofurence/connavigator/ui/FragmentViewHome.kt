package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.filters.enums.EnumEventRecyclerViewmode
import org.eurofurence.connavigator.ui.filters.factory.EventFilterFactory
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.layouts.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.letRoot
import org.eurofurence.connavigator.util.extensions.size

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI {
    val announcementsRecycler by view(RecyclerView::class.java)
    val announcementsTitle by view(TextView::class.java)
    val database: Database by lazy { letRoot { database }!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_home, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen("Home")

        applyOnRoot { changeTitle("Home") }

        instantiate()
    }

    override fun dataUpdated() {
        instantiate()
    }

    private fun instantiate() {
        announcementsRecycler.adapter = AnnoucementRecyclerDataAdapter(database.announcementDb.items.sortedByDescending { it.lastChangeDateTimeUtc }.toList())
        announcementsRecycler.layoutManager = NonScrollingLinearLayout(activity)
        announcementsRecycler.itemAnimator = DefaultItemAnimator()

        val upcoming = EventRecyclerFragment(EventFilterFactory.create(EnumEventRecyclerViewmode.UPCOMING))
        fragmentManager.beginTransaction()
                .replace(R.id.upcomingEventRecycler, upcoming)
                .commitAllowingStateLoss()

        val current = EventRecyclerFragment(EventFilterFactory.create(EnumEventRecyclerViewmode.CURRENT))
        fragmentManager.beginTransaction()
                .replace(R.id.currentEventsRecycler, current)
                .commitAllowingStateLoss()

        fragmentManager.beginTransaction()
                .replace(R.id.favouritedEventsRecycler, EventRecyclerFragment(EventFilterFactory.create(EnumEventRecyclerViewmode.FAVORITED)))
                .commitAllowingStateLoss()

        if (database.announcementDb.size == 0) {
            announcementsRecycler.visibility = View.GONE
            announcementsTitle.visibility = View.GONE
        }
    }
}