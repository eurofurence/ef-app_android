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
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.filters.enums.EnumEventRecyclerViewmode
import org.eurofurence.connavigator.ui.filters.factory.EventFilterFactory
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
class FragmentViewEvents : Fragment(), ContentAPI, TextWatcher {
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        searchFragment.filterVal = eventSearchBar.text
        searchFragment.dataUpdatedLong()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // pass
    }

    override fun afterTextChanged(s: Editable?) {
        // pass
    }

    inner class EventFragmentPagerAdapter(val fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int): CharSequence? {
            if (settings.getBoolean(context.getString(R.string.date_short), true)) {
                return DateTime(database.eventConferenceDayDb.asc { it.date }[position].date).dayOfWeek().asShortText
            } else {
                return DateTime(database.eventConferenceDayDb.asc { it.date }[position].date).toString(DateTimeFormat.forPattern("MMM d"))
            }
        }

        override fun getItem(position: Int): Fragment? {
            return EventRecyclerFragment(EventFilterFactory.create(EnumEventRecyclerViewmode.DAY), database.eventConferenceDayDb.asc { it.date }[position])
        }

        override fun getCount(): Int {
            return database.eventConferenceDayDb.size
        }
    }

    val database: Database get() = letRoot { it.database }!!

    val eventPager by view(ViewPager::class.java)
    val eventSearchBar by view(EditText::class.java)

    val searchFragment by lazy { EventRecyclerFragment(EventFilterFactory.create(EnumEventRecyclerViewmode.SEARCH)) }

    val settings: SharedPreferences get() = letRoot { it.preferences }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen("Events Listing")

        eventPager.adapter = EventFragmentPagerAdapter(childFragmentManager)
        eventPager.offscreenPageLimit = 1

        childFragmentManager.beginTransaction()
                .replace(R.id.eventSearch, searchFragment)
                .commitAllowingStateLoss()

        eventSearchBar.addTextChangedListener(this)

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
        if (eventPager.visibility == View.VISIBLE) {
            eventPager.visibility = View.GONE
            activity.findViewById(R.id.searchLayout).visibility = View.VISIBLE
        } else {
            eventPager.visibility = View.VISIBLE
            activity.findViewById(R.id.searchLayout).visibility = View.GONE
        }
    }
}