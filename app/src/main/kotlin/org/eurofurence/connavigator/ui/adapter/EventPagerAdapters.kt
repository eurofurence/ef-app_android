package org.eurofurence.connavigator.ui.adapter

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.preferences.AppPreferences
import org.eurofurence.connavigator.ui.filters.*
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

infix fun Date.sameDayAs(other: Date) =
        time / (24 * 60 * 60 * 1000) == other.time / (24 * 60 * 60 * 1000)

abstract class EventPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
}

class DayEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : EventPagerAdapter(fragmentManager) {
    private val elements = hashMapOf<Int, EventRecyclerFragment>()

    companion object {
        fun indexOfToday(db: Db) =
                Date().let { now ->
                    db.days.asc { it.date }.indexOfFirst { it.date sameDayAs now }
                }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (AppPreferences.shortenDates) {
            DateTime(days[position].date).dayOfWeek().asShortText
        } else {
            DateTime(days[position].date).toString(DateTimeFormat.forPattern("MMM d"))
        }
    }


    override fun getItem(position: Int): Fragment {
        return elements.getOrPut(position) {
            EventRecyclerFragment().withArguments(
                    FilterOnDay(days[position].id) then OrderTime())
        }
    }

    override fun getCount(): Int {
        return days.size
    }

    private val days by lazy { db.days.asc { it.date } }
}

class RoomEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : EventPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return EventRecyclerFragment().withArguments(
                FilterInRoom(rooms[position].id) then OrderTime(),
                daysInsteadOfGlyphs = true
        )
    }

    override fun getPageTitle(position: Int): String = rooms[position].name

    override fun getCount() = rooms.size

    private val rooms by lazy { db.rooms.asc { it.name } }
}

class TrackEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : EventPagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment {
        return EventRecyclerFragment().withArguments(
                FilterOnTrack(tracks[position].id) then OrderTime(),
                daysInsteadOfGlyphs = true
        )
    }

    override fun getPageTitle(position: Int): String = tracks[position].name

    override fun getCount() = tracks.size

    private val tracks by lazy { db.tracks.asc { it.name } }
}

class FavoriteEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : EventPagerAdapter(fragmentManager) {
    override fun getItem(position: Int) = EventRecyclerFragment().withArguments(
            FilterIsFavorited() then FilterOnDay(days[position].id) then OrderTime())

    override fun getPageTitle(position: Int): String = days[position].name

    override fun getCount() = days.size

    val days by lazy { db.days.asc { it.date } }
}