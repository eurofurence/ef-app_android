package org.eurofurence.connavigator.db

import android.content.Context
import io.swagger.client.model.*
import org.eurofurence.connavigator.extensions.*
import java.io.File
import java.util.*

/**
 * Contains all databases and provides the update method. The operations
 * are partially context oriented: database reads and writes will delegate
 * to the same files, queries will be handled by the current context.
 *
 * @param context The context used to operate the background services
 */
class DBService(val context: Context) {
    /**
     * Database storing dates relevant to database state.
     */
    lateinit var dateDb: DB<Date>

    /**
     * Database of conference days.
     */
    lateinit var eventConferenceDayDb: SyncDB<EventConferenceDay>

    /**
     * Database of conference rooms.
     */
    lateinit var eventConferenceRoomDb: SyncDB<EventConferenceRoom>

    /**
     * Database of conference tracks.
     */
    lateinit var eventConferenceTrackDb: SyncDB<EventConferenceTrack>

    /**
     * Database of events.
     */
    lateinit var eventEntryDb: SyncDB<EventEntry>

    /**
     * Database of images (meta information).
     */
    lateinit var imageDb: SyncDB<Image>

    /**
     * Database of infos.
     */
    lateinit var infoDb: SyncDB<Info>

    /**
     * Database of info groups.
     */
    lateinit var infoGroupDb: SyncDB<InfoGroup>

    /**
     * The current callback
     */
    private var effectiveCallback: DBCallback = DBCallback.EMPTY

    /**
     * The current requests server time
     */
    private var serverDate: Date = Date(0)

    /**
     * Amount of pending database.
     */
    private var requests = 0

    /**
     * Success status of the current update.
     */
    private var success = false

    /**
     * True if update is pending.
     */
    val isPending: Boolean get() = requests > 0

    /**
     * Updates the database status
     */
    fun update(callback: DBCallback) =
            // If already updating, return
            if (isPending) {
                // Compose the callbacks
                effectiveCallback += callback
            } else {
                // Assign the callback
                effectiveCallback = callback

                // Request the endpoint value
                start()
                context.queryEndpoint("updateService")
            }

    /**
     * Initializes the databases
     */
    fun initialize() {
        // Late init the databases with the given context
        dateDb = serializableDBs.create(File(context.cacheDir, "date.db"), Date::class.java)
        eventConferenceDayDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "eventconday.db"), EventConferenceDay::class.java))
        eventConferenceRoomDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "eventconroom.db"), EventConferenceRoom::class.java))
        eventConferenceTrackDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "eventcontrack.db"), EventConferenceTrack::class.java))
        eventEntryDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "events.db"), EventEntry::class.java))
        imageDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "image.db"), Image::class.java))
        infoDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "info.db"), Info::class.java))
        infoGroupDb = efSyncDB(gsonDBs.create(File(context.cacheDir, "infogroup.db"), InfoGroup::class.java))

        // Main request, this will trigger the sequence of database updates
        context.createEndpointReceiver("updateService") {
            finishWith {
                // Get dates of server and local database
                serverDate = it.currentDateTimeUtc
                val dbDate = dateDb.elements.firstOrNull() ?: Date(0)

                // Query conference info
                start(7)
                context.queryEventConferenceDay("updateService", since = dbDate)
                context.queryEventConferenceRoom("updateService", since = dbDate)
                context.queryEventConferenceTrack("updateService", since = dbDate)
                context.queryEventEntry("updateService", since = dbDate)
                context.queryImage("updateService", since = dbDate)
                context.queryInfo("updateService", since = dbDate)
                context.queryInfoGroup("updateService", since = dbDate)

                // Dispatch performed, notify listener
                effectiveCallback.dispatched()
            }
            // Finish the own request
        }.register() assert true

        // On receiving the conference days
        context.createEventConferenceDayReceiver("updateService") {
            finishWith {
                // Sync DB and finish request
                eventConferenceDayDb.syncWith(it)
                effectiveCallback.gotEventConferenceDays(it)
            }
        }.register() assert true

        // On receiving the conference rooms
        context.createEventConferenceRoomReceiver("updateService") {
            finishWith {
                // Sync DB and finish request
                eventConferenceRoomDb.syncWith(it)
                effectiveCallback.gotEventConferenceRooms(it)
            }
        }.register() assert true

        // On receiving the conference tracks
        context.createEventConferenceTrackReceiver ("updateService") {
            finishWith {
                // Sync DB and finish request
                eventConferenceTrackDb.syncWith(it)
                effectiveCallback.gotEventConferenceTracks(it)
            }
        }.register() assert true

        // On receiving the events
        context.createEventEntryReceiver ("updateService") {
            finishWith {
                // Sync DB and finish request
                eventEntryDb.syncWith(it)
                effectiveCallback.gotEvents(it)
            }
        }.register() assert true

        // On receiving the images
        context.createImageReceiver("updateService") {
            finishWith {
                // Sync DB and finish request
                imageDb.syncWith(it)
                effectiveCallback.gotImages(it)
            }
        }.register() assert true

        // On receiving the info
        context.createInfoReceiver ("updateService") {
            finishWith {
                // Sync DB and finish request
                infoDb.syncWith(it)
                effectiveCallback.gotInfo(it)
            }
        }.register() assert true

        // On receiving the info groups
        context.createInfoGroupReceiver ("updateService") {
            finishWith {
                // Sync DB and finish request
                infoGroupDb.syncWith(it)
                effectiveCallback.gotInfoGroups(it)
            }
        }.register() assert true
    }

    /**
     * Increases the number of current requests by [times].
     * @param times The number of new requests
     */
    private fun start(times: Int = 1) {
        requests += times
        success = true
    }

    /**
     * Decreases the number of requests by one, if this is the last request, write the new database time
     */
    private fun finish(opSuccess: Boolean, times: Int = 1) {
        // If last request to finish, put the new database status
        requests -= times
        success = success && opSuccess

        if ( requests == 0) {
            // Commit server date and reset
            dateDb.elements = listOf(serverDate)
            serverDate = Date(0)

            // Call the callbacks done method and reset it
            effectiveCallback.done(success)
            effectiveCallback = DBCallback.EMPTY
        }
    }

    /**
     * Runs a code block maybe failing in an exception to determine the success level.
     * @param block The block to run
     */
    private fun finishWith(block: () -> Unit) {
        finish(ifSuccess(block))
    }
}