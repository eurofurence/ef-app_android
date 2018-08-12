package org.eurofurence.connavigator.ui.adapter

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.filterEvents
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

infix fun Date.sameDayAs(other: Date) =
        time / (24 * 60 * 60 * 1000) == other.time / (24 * 60 * 60 * 1000)

class DayEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun saveState(): Parcelable? {
        return null
    }
    val days by lazy { db.days }

    companion object {
        fun indexOfToday(db: Db) =
                Date().let { now ->
                    db.days.asc { it.date }.indexOfFirst { it.date sameDayAs now }
                }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (AppPreferences.shortenDates) {
            DateTime(days.asc { it.date }[position].date).dayOfWeek().asShortText
        } else {
            DateTime(days.asc { it.date }[position].date).toString(DateTimeFormat.forPattern("MMM d"))
        }
    }


    override fun getItem(position: Int): Fragment? {
        return EventRecyclerFragment().withArguments(db.filterEvents()
                .onDay(days.asc { it.date }[position].id)
                .sortByStartTime())
    }

    override fun getCount(): Int {
        return days.size
    }
}

class RoomEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun saveState(): Parcelable? {
        return null
    }
    override fun getItem(position: Int): Fragment {
        return EventRecyclerFragment().withArguments(
                eventList = db.filterEvents()
                    .inRoom(rooms.asc { it.name }[position].id)
                    .sortByStartTime(),
                daysInsteadOfGlyphs = true
        )
    }

    override fun getPageTitle(position: Int): String = rooms.asc { it.name }[position].name

    override fun getCount() = rooms.size

    private val rooms by lazy { db.rooms }
}

class TrackEventPagerAdapter(val db: Db, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun saveState(): Parcelable? {
        return null
    }
    override fun getItem(position: Int): Fragment {
        return EventRecyclerFragment().withArguments(
                eventList = db.filterEvents()
                    .onTrack(tracks[position].id)
                    .sortByStartTime(),
                daysInsteadOfGlyphs = true
        )
    }

    override fun getPageTitle(position: Int): String = tracks[position].name

    override fun getCount() = tracks.size

    private val tracks by lazy { db.tracks.asc { it.name } }
}

class FavoriteEventPagerAdapter(val db: Db, fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager){
    override fun getItem(position: Int) = EventRecyclerFragment().withArguments(
            eventList = db.filterEvents()
                    .isFavorited()
                    .onDay(days[position].id)
                    .sortByStartTime()
    )

    override fun getPageTitle(position: Int): String = days[position].name

    override fun getCount() = days.size

    val days by lazy { db.days.asc { it.date }}
}