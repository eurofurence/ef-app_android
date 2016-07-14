package org.eurofurence.connavigator.database

import android.content.Context
import io.swagger.client.model.*
import org.eurofurence.connavigator.store.cached
import org.eurofurence.connavigator.store.createGson
import org.eurofurence.connavigator.store.createSerialized
import org.eurofurence.connavigator.util.extensions.cachedApiDB
import org.eurofurence.connavigator.util.extensions.get
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalTime
import java.io.File
import java.util.*

/**
 * Provides an interface to all stored databases and saves a timestamp of their last update time. The databases are
 * lazily loaded and cached with invalidation on underlying file system changes, so multiple instances may be created.
 *
 * When a new database is introduced, it should also be synchronized in the [UpdateIntentService].
 * @param context The context used to operate the background services
 */
class Database(val context: Context) {
    /**
     * Database storing dates relevant to database state.
     */
    val dateDb =
            createSerialized(File(context.cacheDir, "date.db"), Date::class.java).cached()

    /**
     * Stores the latest version
     */
    val versionDb =
            createSerialized(File(context.cacheDir, "version.db"), String::class.java).cached()
    /**
     * Database storing favorited events
     */
    val favoritedDb =
            createGson(File(context.cacheDir, "favorited.db"), EventEntry::class.java).cachedApiDB()

    /**
     * Database of announcements.
     */
    val announcementDb =
            createGson(File(context.cacheDir, "announcement.db"), Announcement::class.java).cachedApiDB()

    /**
     * Database of announcements.
     */
    val dealerDb =
            createGson(File(context.cacheDir, "dealer.db"), Dealer::class.java).cachedApiDB()

    /**
     * Database of conference days.
     */
    val eventConferenceDayDb =
            createGson(File(context.cacheDir, "eventconday.db"), EventConferenceDay::class.java).cachedApiDB()

    /**
     * Database of conference rooms.
     */
    val eventConferenceRoomDb =
            createGson(File(context.cacheDir, "eventconroom.db"), EventConferenceRoom::class.java).cachedApiDB()

    /**
     * Database of conference tracks.
     */
    val eventConferenceTrackDb =
            createGson(File(context.cacheDir, "eventcontrack.db"), EventConferenceTrack::class.java).cachedApiDB()

    val mapEntityDb =
            createGson(File(context.cacheDir, "mapentity.db"), MapEntity::class.java).cachedApiDB()
    /**
     * Database containing map entries
     */
    val mapEntryDb =
            createGson(File(context.cacheDir, "mapentry.db"), MapEntry::class.java).cachedApiDB()

    /**
     * Database of events.
     */
    val eventEntryDb =
            createGson(File(context.cacheDir, "events.db"), EventEntry::class.java).cachedApiDB()

    /**
     * Database of images (meta information).
     */
    val imageDb =
            createGson(File(context.cacheDir, "image.db"), Image::class.java).cachedApiDB()

    /**
     * Database of infos.
     */
    val infoDb =
            createGson(File(context.cacheDir, "info.db"), Info::class.java).cachedApiDB()

    /**
     * Database of info groups.
     */
    val infoGroupDb =
            createGson(File(context.cacheDir, "infogroup.db"), InfoGroup::class.java).cachedApiDB()

    fun clear() {
        dateDb.delete()
        versionDb.delete()
        favoritedDb.delete()
        eventConferenceDayDb.delete()
        eventConferenceRoomDb.delete()
        eventConferenceTrackDb.delete()
        eventEntryDb.delete()
        imageDb.delete()
        infoDb.delete()
        infoGroupDb.delete()
        mapEntryDb.delete()
        mapEntityDb.delete()
    }

    /**
     * Sees if an event is happening during the specified datetime
     */
    fun eventIsHappening(event: EventEntry, date: DateTime) =
            eventInterval(event).contains(date)

    /**
     * Checks whether an event is upcoming in the next x minutes
     * event: Event to check
     * date: Date that event might be upcoming
     * minutes: Amount of minutes you want to check ahead
     */
    fun eventIsUpcoming(event: EventEntry, date: DateTime, minutes: Int): Boolean {
        // Gap that has now and the upcoming time
        val upcomingInterval = Interval(date, date.plusMinutes(minutes))

        return upcomingInterval.contains(eventStart(event))
    }

    /**
     * Gets the day of the event
     */
    fun eventDay(eventEntry: EventEntry): DateTime = DateTime.parse(eventConferenceDayDb[eventEntry.conferenceDayId]!!.date)

    /**
     * Gets the start time and day of the event
     */
    fun eventStart(eventEntry: EventEntry): DateTime =
            eventDay(eventEntry).withTime(LocalTime.parse(eventEntry.startTime))


    /**
     * Gets the end time and day of the event. Special behavior: if end time is smaller than the start time, it is
     * assumed that the next day is meant.
     */
    fun eventEnd(eventEntry: EventEntry): DateTime {
        val st = LocalTime.parse(eventEntry.startTime)
        val et = LocalTime.parse(eventEntry.endTime)

        if (et < st)
            return eventDay(eventEntry).plusDays(1).withTime(et)
        else
            return eventDay(eventEntry).withTime(et)

    }


    /**
     * Gets the time interval this event is happening in
     */
    fun eventInterval(eventEntry: EventEntry): Interval =
            Interval(eventStart(eventEntry), eventEnd(eventEntry))
}