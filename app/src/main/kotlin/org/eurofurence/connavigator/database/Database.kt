package org.eurofurence.connavigator.database

import android.content.Context
import io.swagger.client.model.*
import org.eurofurence.connavigator.store.cached
import org.eurofurence.connavigator.store.createGson
import org.eurofurence.connavigator.store.createSerialized
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
            createSerialized(File(context.cacheDir, "date.db"), Date::class.java).cached()

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
        eventConferenceDayDb.delete()
        eventConferenceRoomDb.delete()
        eventConferenceTrackDb.delete()
        eventEntryDb.delete()
        imageDb.delete()
        infoDb.delete()
        infoGroupDb.delete()
    }
}