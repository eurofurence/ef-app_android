package org.eurofurence.connavigator.database

import android.content.Context
import io.swagger.client.model.*
import org.eurofurence.connavigator.store.cached
import org.eurofurence.connavigator.store.gsonDBs
import org.eurofurence.connavigator.store.serializableDBs
import org.eurofurence.connavigator.util.extensions.cachedApiDB
import java.io.File
import java.util.*

/**
 * Provides an interface to all stored databases and saves a timestamp of their last update time. The databases are
 * lazily loaded and cached with invalidation on underlying file system changes, so multiple instances may be created.
 *
 * @param context The context used to operate the background services
 */
class Database(val context: Context) {
    /**
     * Database storing dates relevant to database state.
     */
    val dateDb =
            serializableDBs.create(File(context.cacheDir, "date.db"), Date::class.java).cached()

    /**
     * Database of conference days.
     */
    val eventConferenceDayDb =
            gsonDBs.create(File(context.cacheDir, "eventconday.db"), EventConferenceDay::class.java).cachedApiDB()

    /**
     * Database of conference rooms.
     */
    val eventConferenceRoomDb =
            gsonDBs.create(File(context.cacheDir, "eventconroom.db"), EventConferenceRoom::class.java).cachedApiDB()

    /**
     * Database of conference tracks.
     */
    val eventConferenceTrackDb =
            gsonDBs.create(File(context.cacheDir, "eventcontrack.db"), EventConferenceTrack::class.java).cachedApiDB()

    /**
     * Database of events.
     */
    val eventEntryDb =
            gsonDBs.create(File(context.cacheDir, "events.db"), EventEntry::class.java).cachedApiDB()

    /**
     * Database of images (meta information).
     */
    val imageDb =
            gsonDBs.create(File(context.cacheDir, "image.db"), Image::class.java).cachedApiDB()

    /**
     * Database of infos.
     */
    val infoDb =
            gsonDBs.create(File(context.cacheDir, "info.db"), Info::class.java).cachedApiDB()

    /**
     * Database of info groups.
     */
    val infoGroupDb =
            gsonDBs.create(File(context.cacheDir, "infogroup.db"), InfoGroup::class.java).cachedApiDB()

    fun clear() {
        dateDb.elements = emptyList()
        eventConferenceDayDb.elements = emptyList()
        eventConferenceRoomDb.elements = emptyList()
        eventConferenceTrackDb.elements = emptyList()
        eventEntryDb.elements = emptyList()
        imageDb.elements = emptyList()
        infoDb.elements = emptyList()
        infoGroupDb.elements = emptyList()
    }
}