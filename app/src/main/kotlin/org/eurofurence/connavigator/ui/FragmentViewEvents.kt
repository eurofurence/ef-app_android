package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.eventDayNumber
import org.eurofurence.connavigator.database.filterEvents
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.pref.BackgroundPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.DayEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.RoomEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.TrackEventPagerAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.jetbrains.anko.support.v4.selector

/**
 * Created by David on 5/3/2016.
 */
class FragmentViewEvents : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    val eventPager: ViewPager by view()
    val eventSearchBar: EditText by view()
    val searchEventFilter by lazy { filterEvents() }
    val searchFragment by lazy { EventRecyclerFragment(searchEventFilter) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Events Listing")

        configureViewpager()

        childFragmentManager.beginTransaction()
                .replace(R.id.eventSearch, searchFragment)
                .commitAllowingStateLoss()

        eventSearchBar.textWatcher {
            afterTextChanged { text ->
                searchEventFilter.byTitle(text.toString())
                searchFragment.dataUpdated()
            }
        }

        applyOnRoot {
            tabs.setupWithViewPager(eventPager)
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }
        applyOnRoot { changeTitle("Event Schedule") }
    }

    private fun configureViewpager() {
        changePagerAdapter(BackgroundPreferences.eventPagerMode)
    }

    private fun changePagerAdapter(adapter: PagerAdapter) {
        eventPager.adapter = adapter
        eventPager.adapter.notifyDataSetChanged()
    }

    private fun changePagerAdapter(mode: EventPagerMode) = when (mode) {
        EventPagerMode.ROOMS -> changePagerAdapter(RoomEventPagerAdapter(db, childFragmentManager))
        EventPagerMode.TRACKS -> changePagerAdapter(TrackEventPagerAdapter(db, childFragmentManager))
        else -> changePagerAdapter(DayEventPagerAdapter(db, childFragmentManager))
    }.apply { BackgroundPreferences.eventPagerMode = mode }

    override fun dataUpdated() {
        eventPager.adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        applyOnRoot { tabs.setupWithViewPager(null) }
    }

    override fun onSearchButtonClick() {
        when (eventPager.visibility) {
            View.VISIBLE -> {
                eventPager.visibility = View.GONE
                activity.findViewById(R.id.searchLayout).visibility = View.VISIBLE
            }
            else -> {
                eventPager.visibility = View.VISIBLE
                activity.findViewById(R.id.searchLayout).visibility = View.GONE
            }
        }
    }

    override fun onFilterButtonClick() = selector("Change filtering mode", listOf("Days", "Rooms", "Event tracks"), {
        _, position ->
        when (position) {
            1 -> changePagerAdapter(EventPagerMode.ROOMS)
            2 -> changePagerAdapter(EventPagerMode.TRACKS)
            else -> changePagerAdapter(EventPagerMode.DAYS)
        }
    })
}

enum class EventPagerMode {
    DAYS,
    ROOMS,
    TRACKS
}