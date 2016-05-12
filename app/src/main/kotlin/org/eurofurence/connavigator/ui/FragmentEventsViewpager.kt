package org.eurofurence.connavigator.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.EventView
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.joda.time.DateTime

/**
 * Created by David on 5/3/2016.
 */
class FragmentEventsViewpager : Fragment(), ContentAPI {
    class EventFragmentPagerAdapter(val fragmentManager: FragmentManager, val context: Context) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int): CharSequence? {
            val database = Database(context)
            val d = database.eventConferenceDayDb.items[position].date
            return DateTime(d).dayOfWeek().asShortText
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events_viewpager, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Events Viewpager")

        eventPager.adapter = EventFragmentPagerAdapter(fragmentManager, activity)


        applyOnRoot { tabs.setupWithViewPager(eventPager) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        applyOnRoot { tabs.setupWithViewPager(null) }
    }
}