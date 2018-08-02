package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.pref.BackgroundPreferences
import org.eurofurence.connavigator.ui.adapter.DayEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.RoomEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.TrackEventPagerAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.support.v4.selector

/**
 * Created by David on 5/3/2016.
 */
class FragmentViewEvents : Fragment(), ContentAPI, HasDb, NavRepresented {
    override val db by lazyLocateDb()
    override val drawerItemId: Int
        get() = R.id.navEvents


    val eventPager: ViewPager by view()
    val eventSearchBar: EditText by view()
    val searchFragment by lazy { EventRecyclerFragment().withArguments(daysInsteadOfGlyphs = true) }

    private val detailsPopAdapter = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            if (isResumed)
                applyOnRoot { popDetails() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        configureViewpager()

        childFragmentManager.beginTransaction()
                .replace(R.id.eventSearch, searchFragment)
                .commitAllowingStateLoss()

        eventSearchBar.textWatcher {
            afterTextChanged { text ->
                searchFragment.eventList.byTitle(text.toString())
                        .sortByDateAndTime()
                searchFragment.dataUpdated()
            }
        }

        applyOnRoot {
            tabs.setupWithViewPager(eventPager)
            tabs.tabMode = TabLayout.MODE_SCROLLABLE
        }
        eventPager.addOnPageChangeListener(detailsPopAdapter)

        applyOnRoot { changeTitle("Event Schedule") }
    }


    private fun configureViewpager() {
        changePagerAdapter(BackgroundPreferences.eventPagerMode)
    }

    private fun changePagerAdapter(adapter: PagerAdapter, preferedPosition: Int? = null) {
        eventPager.adapter = adapter
        eventPager.adapter.notifyDataSetChanged()


        preferedPosition?.let {
            eventPager.setCurrentItem(it, false)
        }
    }

    private fun changePagerAdapter(mode: EventPagerMode) = when (mode) {
        EventPagerMode.ROOMS -> changePagerAdapter(RoomEventPagerAdapter(db, childFragmentManager))
        EventPagerMode.TRACKS -> changePagerAdapter(TrackEventPagerAdapter(db, childFragmentManager))
        else -> changePagerAdapter(DayEventPagerAdapter(db, childFragmentManager), DayEventPagerAdapter.indexOfToday(db))
    }.apply { BackgroundPreferences.eventPagerMode = mode }

    override fun onDestroyView() {
        super.onDestroyView()
        eventPager.removeOnPageChangeListener(detailsPopAdapter)
        applyOnRoot { tabs.setupWithViewPager(null) }
    }

    override fun onSearchButtonClick() {
        applyOnRoot { popDetails() }
        when (eventPager.visibility) {
            View.VISIBLE -> {
                eventPager.visibility = View.GONE
                val searchLayout = activity.findViewById(R.id.searchLayout)

                searchLayout.visibility = View.VISIBLE
                searchLayout.requestFocus()

                activity.inputMethodManager
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
            else -> {
                eventPager.visibility = View.VISIBLE
                activity.findViewById(R.id.searchLayout).visibility = View.GONE
            }
        }
    }

    override fun onFilterButtonClick() {
        applyOnRoot { popDetails() }

        selector("Change filtering mode", listOf("Days", "Rooms", "Event tracks")) { _, position ->
            when (position) {
                1 -> changePagerAdapter(EventPagerMode.ROOMS)
                2 -> changePagerAdapter(EventPagerMode.TRACKS)
                else -> changePagerAdapter(EventPagerMode.DAYS)
            }
        }
    }
}

enum class EventPagerMode {
    DAYS,
    ROOMS,
    TRACKS
}