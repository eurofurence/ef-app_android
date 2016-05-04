package org.eurofurence.connavigator.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerTabStrip
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.fragments.EventView
import org.eurofurence.connavigator.util.delegators.view

/**
 * Created by David on 5/3/2016.
 */
class FragmentEventsViewpager : Fragment() {
    class EventFragmentPagerAdapter(val fragmentManager: FragmentManager, val context: Context) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int): CharSequence? {
            val database = Database(context)
            return database.eventConferenceDayDb.items[position].date
        }

        override fun getItem(position: Int): Fragment? {
            val database = Database(context)

            return EventView(position, database.eventConferenceDayDb.items[position])
        }

        override fun getCount(): Int {
            val database = Database(context)

            return database.eventConferenceDayDb.items.count()
        }

    }

    val eventPager by view(ViewPager::class.java)
    val eventHeader by view(PagerTabStrip::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Events Viewpager")

        eventPager.adapter = EventFragmentPagerAdapter(fragmentManager, activity)
        eventPager
    }
}