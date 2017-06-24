package org.eurofurence.connavigator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.letRoot
import org.eurofurence.connavigator.util.extensions.size
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by David on 5/3/2016.
 */
class FragmentViewEvents : Fragment(), ContentAPI {
    inner class EventFragmentPagerAdapter(val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int): CharSequence? {
            if (AppPreferences.shortenDates) {
                return DateTime(database.eventConferenceDayDb.asc { it.date }[position].date).dayOfWeek().asShortText
            } else {
                return DateTime(database.eventConferenceDayDb.asc { it.date }[position].date).toString(DateTimeFormat.forPattern("MMM d"))
            }
        }

        override fun getItem(position: Int): Fragment? {
            return EventRecyclerFragment(database.filterEvents()
                    .onDay(database.eventConferenceDayDb.asc { it.date }[position].id))
        }

        override fun getCount(): Int {
            return database.eventConferenceDayDb.size
        }
    }

    val database: Database get() = letRoot { it.database }!!

    val eventPager: ViewPager by view()
    val eventSearchBar: EditText by view()

    val searchEventFilter by lazy { database.filterEvents() }

    val searchFragment by lazy { EventRecyclerFragment(searchEventFilter) }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Events Listing")

        eventPager.adapter = EventFragmentPagerAdapter(childFragmentManager)
        eventPager.offscreenPageLimit = 1

        childFragmentManager.beginTransaction()
                .replace(R.id.eventSearch, searchFragment)
                .commitAllowingStateLoss()

        eventSearchBar.textWatcher {
            afterTextChanged { text ->
                searchEventFilter.byTitle(text.toString())
                searchFragment.dataUpdated()
            }
        }

        applyOnRoot { tabs.setupWithViewPager(eventPager) }
        applyOnRoot { changeTitle("Event Schedule") }
    }

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
}