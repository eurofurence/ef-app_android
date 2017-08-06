package org.eurofurence.connavigator.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.filterEvents
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class DayEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    val days by lazy { db.days }
    override fun getPageTitle(position: Int): CharSequence? {
        if (AppPreferences.shortenDates) {
            return DateTime(days.asc { it.date }[position].date).dayOfWeek().asShortText
        } else {
            return DateTime(days.asc { it.date }[position].date).toString(DateTimeFormat.forPattern("MMM d"))
        }
    }

    override fun getItem(position: Int): Fragment? {
        return EventRecyclerFragment(db.filterEvents()
                .onDay(days.asc { it.date }[position].id)
                .sortByStartTime())
    }

    override fun getCount(): Int {
        return days.size
    }
}

class RoomEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return EventRecyclerFragment(db.filterEvents()
                .inRoom(rooms.asc { it.name }[position].id)
                .sortByStartTime()
        )
    }

    override fun getPageTitle(position: Int) = rooms.asc { it.name }[position].name

    override fun getCount() = rooms.size

    val rooms by lazy { db.rooms }
}

class TrackEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return EventRecyclerFragment(db.filterEvents()
                .onTrack(tracks[position].id)
                .sortByStartTime()
        )
    }

    override fun getPageTitle(position: Int) = tracks[position].name

    override fun getCount() = tracks.size

    val tracks by lazy { db.tracks.asc { it.name } }
}