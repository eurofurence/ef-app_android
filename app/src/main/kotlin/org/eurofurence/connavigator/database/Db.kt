package org.eurofurence.connavigator.database

import android.content.Context
import android.support.v4.app.Fragment
import android.util.Log
import com.google.firebase.perf.metrics.AddTrace
import io.swagger.client.model.*
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.util.v2.*
import org.eurofurence.connavigator.util.v2.Stored.StoredSource
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Interval
import org.joda.time.LocalTime
import java.util.*

/**
 * Abstract database interface.
 */
interface Db {
    /**
     * Last update time.
     */
    var date: Date?

    /**
     * Stored version.
     */
    var version: String?

    /**
     * Database for [AnnouncementRecord].
     */
    val announcements: StoredSource<AnnouncementRecord>

    /**
     * Database for [DealerRecord].
     */
    val dealers: StoredSource<DealerRecord>

    /**
     * Database for [EventConferenceDayRecord].
     */
    val days: StoredSource<EventConferenceDayRecord>

    /**
     * Database for [EventConferenceRoomRecord].
     */
    val rooms: StoredSource<EventConferenceRoomRecord>

    /**
     * Database for [EventConferenceTrackRecord].
     */
    val tracks: StoredSource<EventConferenceTrackRecord>

    /**
     * Database for [EventRecord].
     */
    val events: StoredSource<EventRecord>

    /**
     * Database for [ImageRecord].
     */
    val images: StoredSource<ImageRecord>

    /**
     * Database for [KnowledgeEntryRecord].
     */
    val knowledgeEntries: StoredSource<KnowledgeEntryRecord>

    /**
     * Database for [KnowledgeGroupRecord].
     */
    val knowledgeGroups: StoredSource<KnowledgeGroupRecord>

    /**
     * Database for [MapRecord].
     */
    val maps: StoredSource<MapRecord>

    /**
     * List of favorited events.
     */
    var faves: List<UUID>

    /**
     * Dealer &rarr; Image join for the thumbnail.
     */
    val toThumbnail: JoinerBinding<DealerRecord, ImageRecord, UUID>

    /**
     * Dealer &rarr; Image join for the artist image.
     */
    val toArtistImage: JoinerBinding<DealerRecord, ImageRecord, UUID>

    /**
     * Dealer &rarr; Image join for the preview.
     */
    val toPreview: JoinerBinding<DealerRecord, ImageRecord, UUID>

    /**
     * Event &rarr; Day join.
     */
    val toDay: JoinerBinding<EventRecord, EventConferenceDayRecord, UUID>

    /**
     * Event &rarr; Track join.
     */
    val toTrack: JoinerBinding<EventRecord, EventConferenceTrackRecord, UUID>

    /**
     * Event &rarr; Room join.
     */
    val toRoom: JoinerBinding<EventRecord, EventConferenceRoomRecord, UUID>

    /**
     * Knowledge entry &rarr; Group join.
     */
    val toGroup: JoinerBinding<KnowledgeEntryRecord, KnowledgeGroupRecord, UUID>

    /**
     * Map &rarr; Image join.
     */
    val toImage: JoinerBinding<MapRecord, ImageRecord, UUID>

    /**
     * Fave &rarr;
     */
    val toEvent: JoinerBinding<UUID, EventRecord, UUID>

    fun clear()
}

/**
 * Locates the database for the current receiver.
 */
fun Any.locateDb(): Db =
        when (this) {

        // On fragment initialize on context, keep if the fragment is a DB.
            is Fragment -> requireContext().let {
                if (it is Db) it else RootDb(it)
            }

        // On context initialize on this
            is Context -> RootDb(this)


        // Otherwise fail
            else -> throw IllegalStateException(
                    "Cannot use automatic database from objects other than Context or Fragment.")
        }

fun Any.lazyLocateDb() = lazy { locateDb() }

/**
 * Delegate implementation of the database, override [db] by e.g. [locateDb].
 */
interface HasDb : Db {
    /**
     * Gets the actual delgate, may be implemented by [autoDb].
     */
    val db: Db

    override var date: Date?
        get() = db.date
        set(value) {
            db.date = value
        }

    override var version: String?
        get() = db.version
        set(value) {
            db.version = value
        }

    override val announcements: StoredSource<AnnouncementRecord>
        get() = db.announcements

    override val dealers: StoredSource<DealerRecord>
        get() = db.dealers

    override val days: StoredSource<EventConferenceDayRecord>
        get() = db.days

    override val rooms: StoredSource<EventConferenceRoomRecord>
        get() = db.rooms

    override val tracks: StoredSource<EventConferenceTrackRecord>
        get() = db.tracks

    override val events: StoredSource<EventRecord>
        get() = db.events

    override var faves: List<UUID>
        get() = db.faves
        set(value) {
            db.faves = value
        }
    override val images: StoredSource<ImageRecord>
        get() = db.images

    override val knowledgeEntries: StoredSource<KnowledgeEntryRecord>
        get() = db.knowledgeEntries

    override val knowledgeGroups: StoredSource<KnowledgeGroupRecord>
        get() = db.knowledgeGroups

    override val maps: StoredSource<MapRecord>
        get() = db.maps

    override val toThumbnail: JoinerBinding<DealerRecord, ImageRecord, UUID>
        get() = db.toThumbnail

    override val toArtistImage: JoinerBinding<DealerRecord, ImageRecord, UUID>
        get() = db.toArtistImage

    override val toPreview: JoinerBinding<DealerRecord, ImageRecord, UUID>
        get() = db.toPreview

    override val toDay: JoinerBinding<EventRecord, EventConferenceDayRecord, UUID>
        get() = db.toDay

    override val toTrack: JoinerBinding<EventRecord, EventConferenceTrackRecord, UUID>
        get() = db.toTrack

    override val toRoom: JoinerBinding<EventRecord, EventConferenceRoomRecord, UUID>
        get() = db.toRoom

    override val toGroup: JoinerBinding<KnowledgeEntryRecord, KnowledgeGroupRecord, UUID>
        get() = db.toGroup

    override val toImage: JoinerBinding<MapRecord, ImageRecord, UUID>
        get() = db.toImage

    override val toEvent: JoinerBinding<UUID, EventRecord, UUID>
        get() = db.toEvent

    override fun clear() {
        db.clear()
    }
}


/**
 * Direct database implementation.
 */
class RootDb(context: Context) : Stored(context), Db {

    override var date by storedValue<Date>()

    override var version by storedValue<String>()

    override val announcements = storedSource(AnnouncementRecord::getId)

    override val dealers = storedSource(DealerRecord::getId)

    override val days = storedSource(EventConferenceDayRecord::getId)

    override val rooms = storedSource(EventConferenceRoomRecord::getId)

    override val tracks = storedSource(EventConferenceTrackRecord::getId)

    override val events = storedSource(EventRecord::getId)

    override var faves by storedValues<UUID>()

    override val images = storedSource(ImageRecord::getId)

    override val knowledgeEntries = storedSource(KnowledgeEntryRecord::getId)

    override val knowledgeGroups = storedSource(KnowledgeGroupRecord::getId)

    override val maps = storedSource(MapRecord::getId)

    override val toThumbnail = JoinerBinding(
            DealerRecord::getArtistThumbnailImageId join ImageRecord::getId,
            dealers, images)

    override val toArtistImage = JoinerBinding(
            DealerRecord::getArtistImageId join ImageRecord::getId,
            dealers, images)

    override val toPreview = JoinerBinding(
            DealerRecord::getArtPreviewImageId join ImageRecord::getId,
            dealers, images)

    override val toDay = JoinerBinding(
            EventRecord::getConferenceDayId join EventConferenceDayRecord::getId,
            events, days)

    override val toTrack = JoinerBinding(
            EventRecord::getConferenceTrackId join EventConferenceTrackRecord::getId,
            events, tracks)

    override val toRoom = JoinerBinding(
            EventRecord::getConferenceRoomId join EventConferenceRoomRecord::getId,
            events, rooms)

    override val toGroup = JoinerBinding(
            KnowledgeEntryRecord::getKnowledgeGroupId join KnowledgeGroupRecord::getId,
            knowledgeEntries, knowledgeGroups)

    override val toImage = JoinerBinding(
            MapRecord::getImageId join ImageRecord::getId,
            maps, images)

    override val toEvent = JoinerBinding(
            { u: UUID -> u } join EventRecord::getId,
            IdSource(), events)

    override fun clear() {
        date = null
        version = null
        announcements.delete()
        dealers.delete()
        days.delete()
        rooms.delete()
        tracks.delete()
        events.delete()
        faves = listOf()
        images.delete()
        knowledgeEntries.delete()
        knowledgeGroups.delete()
        maps.delete()
    }
}


/**
 * Sees if an event is happening during the specified datetime
 */
fun Db.eventIsHappening(event: EventRecord, date: DateTime) =
        eventInterval(event).contains(date)

/**
 * Checks whether an event is upcoming in the next x minutes.
 * @param event: Event to check
 * @param date: Date that event might be upcoming
 * @param minutes: Amount of minutes you want to check ahead
 */
fun Db.eventIsUpcoming(event: EventRecord, date: DateTime, minutes: Int) =
        Interval(date, date.plusMinutes(minutes)).contains(eventStart(event))


/**
 * Gets the day of the event.
 */
fun Db.eventDayNumber(eventEntry: EventRecord) =
        DateTime(eventEntry[toDay]?.date)

/**
 * Gets the start time and day of the event.
 */
fun Db.eventStart(eventEntry: EventRecord): DateTime =
        eventDayNumber(eventEntry).withTime(LocalTime.parse(eventEntry.startTime))


/**
 * Gets the end time and day of the event. Special behavior: if end time is smaller than the start time, it is
 * assumed that the next day is meant.
 */
fun Db.eventEnd(eventEntry: EventRecord): DateTime {
    val st = LocalTime.parse(eventEntry.startTime)
    val et = LocalTime.parse(eventEntry.endTime)

    if (et < st)
        return eventDayNumber(eventEntry).plusDays(1).withTime(et)
    else
        return eventDayNumber(eventEntry).withTime(et)
}


/**
 * Gets the time interval this event is happening in
 */
fun Db.eventInterval(eventEntry: EventRecord): Interval =
        Interval(eventStart(eventEntry), eventEnd(eventEntry))

/**
 * You input an event and it will check it it overlaps with a favourited event
 */
fun Db.eventIsConflicting(eventEntry: EventRecord): Boolean =
        events.items
                .filter { it.conferenceDayId == eventEntry.conferenceDayId }
                .filter { it.id in faves }
                .filter { it.id != eventEntry.id }
                .filter { eventInterval(eventEntry).overlaps(eventInterval(it)) }
                .isNotEmpty() // If this list is bigger then 0, we have conflicting events

fun Db.filterEvents() =
        EventList(this)

/**
 * see if today is an event day
 * Returns the current number of the day from 1
 * returns 0 if before or after the event
 */
fun Db.eventDayNumber(): Int {
    Log.d("DB", "Calulating day of the event")
    val firstDay = DateTime(this.days.items.first().date.time)
    val lastDay = DateTime(this.days.items.last().date.time)

    if (firstDay.isAfterNow or lastDay.isBeforeNow) {
        Log.d("DB", "Today is before or after the event! Returning zero")
        return 0
    }
    val days = Days.daysBetween(firstDay, DateTime.now()).days + 1
    Log.d("DB", "Today is day $days")
    return days
}

/**
 * Finds a link fragment that matches the target string.
 * Thank you luchs for absolutely killing me with this shitty data model
 * I mean, what the fuck even is this? this is basically a triple nested for loop that could have been handled by 2 refs
 * Well I guess you enjoy us searching for a needle in this gaystack
 * t. retarduinard
 */
@AddTrace(name = "Db.findLinkFragment", enabled = true)
fun Db.findLinkFragment(target: String): Map<String, Any?> {
    maps.items.forEach {
        val map = it
        it.entries.forEach {
            val entry = it
            it.links.forEach {
                if (it.target.equals(target)) {
                    return mapOf(
                            "map" to map,
                            "entry" to entry
                    )
                }
            }
        }
    }

    return mapOf(
            "map" to null,
            "entry" to null
    )
}
