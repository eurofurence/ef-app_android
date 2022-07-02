

package org.eurofurence.connavigator.database

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.perf.metrics.AddTrace
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import io.swagger.client.model.*
import org.eurofurence.connavigator.util.v2.*
import org.eurofurence.connavigator.util.v2.Stored.StoredSource
import org.eurofurence.connavigator.dropins.AnkoLogger

import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Interval
import org.joda.time.LocalTime
import java.util.*

/**
 * Abstract database interface.
 */
interface Db {
    val observer: Subject<Db>
    /**
     * Last update time.
     */
    var date: Date?

    /**
     * Stored version.
     */
    var version: String?

    /**
     * Stored state.
     */
    var state: String?

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
     * Announcement &rarr; Image join for the thumbnail.
     */
    val toAnnouncementImage: JoinerBinding<AnnouncementRecord, ImageRecord, UUID>

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

    /**
     * Subscribe on main thread
     */
    fun subscribe(function: (db: Db) -> Any?): Disposable
}

/**
 * Locates the database for the current receiver.
 */
fun Any.locateDb(): Db =
        when (this) {
            // If context, make new root DB
            is Context -> RootDb(this)

            // If fragment, check if context is DB, otherwise make new root DB
            is Fragment -> context.let {
                if (it is Db) it else RootDb(context!!)
            }

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
     * Gets the actual delegate.
     */
    val db: Db

    override val observer: Subject<Db>
        get() = db.observer

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

    override var state: String?
        get() = db.state
        set(value) {
            db.state = value
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

    override val toAnnouncementImage: JoinerBinding<AnnouncementRecord, ImageRecord, UUID>
        get() = db.toAnnouncementImage

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

    override fun subscribe(function: (db: Db) -> Any?) = db.subscribe(function)

}


/**
 * Direct database implementation.
 */
class RootDb(context: Context) : Stored(context), Db {
    override val observer: BehaviorSubject<Db> = BehaviorSubject.create<Db>().apply {
        // Autopush a single event to render
        onNext(this@RootDb)
    }

    override var date by storedValue<Date>()

    override var version by storedValue<String>()

    override var state by storedValue<String>()

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

    override val toAnnouncementImage: JoinerBinding<AnnouncementRecord, ImageRecord, UUID>
        get() = JoinerBinding(
                AnnouncementRecord::getImageId join ImageRecord::getId,
                announcements, images)

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
        state = null
        announcements.delete()
        dealers.delete()
        days.delete()
        rooms.delete()
        tracks.delete()
        events.delete()
        images.delete()
        knowledgeEntries.delete()
        knowledgeGroups.delete()
        maps.delete()

        observer.onNext(this)
    }

    override fun subscribe(function: (db: Db) -> Any?): Disposable = observer.observeOn(AndroidSchedulers.mainThread())
            .subscribe { function(it) }
}

/**
 * Finds a link fragment that matches the target string.
 * Thank you luchs for absolutely killing me with this shitty data model
 * I mean, what the fuck even is this? this is basically a triple nested for loop that could have been handled by 2 refs
 * Well I guess you enjoy us searching for a needle in this gaystack
 * t. retarduinard
 */
@AddTrace(name = "Db.findLinkFragment", enabled = true)
fun Db.findLinkFragment(target: String): Map<String?, Any?> {
    for (map in maps.items) {
        for (entry in map.entries.orEmpty())
            if (entry.links != null)
                for (link in entry.links.orEmpty())
                    if (link.target == target)
                        return mapOf(
                                "map" to map,
                                "entry" to entry
                        )
    }

    return mapOf("map" to null, "entry" to null)
}


fun HasDb.glyphsFor(eventEntry: EventRecord) =
        if (eventEntry.tags == null)
            emptyList()
        else
            arrayListOf<String>().apply {
                eventEntry.tags.orEmpty().let { tags ->
                    if ("sponsors_only" in tags)
                        add("{fa-star-half-o @color/sponsor}")
                    if ("supersponsors_only" in tags)
                        add("{fa-star @color/supersponsor}")
                    if ("kage" in tags) {
                        add("{fa-bug}")
                        add("{fa-glass}")
                    }
                    if ("art_show" in tags)
                        add("{fa-photo}")
                    if ("dealers_den" in tags)
                        add("{fa-shopping-cart}")
                    if ("main_stage" in tags)
                        add("{fa-asterisk}")
                    if ("photoshoot" in tags)
                        add("{fa-camera}")
                }
            }.toList()

fun HasDb.descriptionFor(eventEntry: EventRecord) =
        if (eventEntry.tags == null)
            emptyList()
        else
            arrayListOf<String>().apply {
                eventEntry.tags.orEmpty().let { tags ->
                    if ("sponsors_only" in tags)
                        add("{fa-star-half-o} Admittance for Sponsors and Super-Sponsors only")
                    if ("supersponsors_only" in tags)
                        add("{fa-star} Admittance for Super-Sponsors only")
                    if ("kage" in tags)
                        add("{fa-bug} {fa-glass} May contain traces of cockroach and wine")
                    if ("art_show" in tags)
                        add("{fa-photo} Art Show")
                    if ("dealers_den" in tags)
                        add("{fa-shopping-cart} Dealers Den")
                    if ("main_stage" in tags)
                        add("{fa-asterisk} Main Stage Event")
                    if ("photoshoot" in tags)
                        add("{fa-camera} Photoshoot")
                }
            }.toList()
