package org.eurofurence.connavigator.database

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.gcm.NotificationFactory
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.DebugPreferences
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.convert
import org.eurofurence.connavigator.webapi.apiService
import org.joda.time.DateTime


/**
 * Updates the database on request.
 */
class UpdateIntentService : IntentService("UpdateIntentService"), HasDb {
    companion object {
        val UPDATE_COMPLETE = "org.eurofurence.connavigator.driver.UPDATE_COMPLETE"
        val REQUEST_CODE = 1337

        /**
         * Dispatches an update
         * @param context The host context for the service
         */
        fun dispatchUpdate(context: Context) {
            logv("UIS") { "Dispatching update" }
            context.startService(Intent(context, UpdateIntentService::class.java))
        }

    }

    override val db by lazyLocateDb()

    // TODO: Sticky intent since there should only be one pending update
    override fun onHandleIntent(intent: Intent?) {
        logv("UIS") { "Handling update intent service intent" }

        // Initialize the response, the following code is net and IO oriented, it could fail
        val response = {
            logv("UIS") { "Retrieving sync since $date" }

            // Get sync from server
            val sync = apiService.sync.apiV2SyncGet(date)

            logv("UIS") { sync }

            val shift = DebugPreferences.debugDates
            val shiftOffset = DebugPreferences.eventDateOffset

            if (shift) {
                logd { "Changing dates instead of updating" }
                // Get all dates explicitly
                val base = apiService.days.apiV2EventConferenceDaysGet()

                // Shift by offset
                val currentDate = DateTime.now()
                for ((i, day) in base.withIndex())
                    day.date = currentDate.plusDays(i - shiftOffset).toDate()

                // Make a new sync package entry
                sync.eventConferenceDays.removeAllBeforeInsert = true
                sync.eventConferenceDays.changedEntities = base
                sync.eventConferenceDays.deletedEntities = emptyList()
            }

            sync.announcements.changedEntities.filter { DateTime.now().isAfter(it.validFromDateTimeUtc.time) }
                    .filter { DateTime.now().isBefore(it.validUntilDateTimeUtc.time) }
                    .forEach { NotificationFactory(this).showNotification(it.title, it.content) }

            for (image in sync.images.changedEntities)
                imageService.recache(image)


            // Apply sync
            announcements.apply(sync.announcements.convert())
            dealers.apply(sync.dealers.convert())
            days.apply(sync.eventConferenceDays.convert())
            rooms.apply(sync.eventConferenceRooms.convert())
            tracks.apply(sync.eventConferenceTracks.convert())
            events.apply(sync.events.convert())
            images.apply(sync.images.convert())
            knowledgeEntries.apply(sync.knowledgeEntries.convert())
            knowledgeGroups.apply(sync.knowledgeGroups.convert())
            maps.apply(sync.maps.convert())

            // Store new time
            date = sync.currentDateTimeUtc

            logv("UIS") { "Synced, current sync time is $date" }

            // Make the success response message
            logv("UIS") { "Completed update successfully" }
            UPDATE_COMPLETE.toIntent {
                booleans["success"] = true
                objects["time"] = date
            }
        } catchAlternative { ex: Throwable ->
            // Make the fail response message, transfer exception
            loge("UIS", ex) { "Completed update with error" }
            UPDATE_COMPLETE.toIntent {
                booleans["success"] = false
                objects["time"] = date
                objects["reason"] = ex
            }
        }

        // Reschedule the update
        schedule(this)

        // Send a broadcast notifying completion of this action
        LocalBroadcastManager.getInstance(this).sendBroadcast(response)
    }

    private fun schedule(context: Context) {
        // TODO, this could use some anko refactoring I guess

        Log.d("UIS", "Scheduling the next data update")

        var nextUpdate = DateTime.now()

        if (days.items
                .map { DateTime(it.date) }
                .filter { it.toDate() == DateTime.now().toDate() }
                .isNotEmpty()) {
            nextUpdate = nextUpdate.plusMinutes(1)
        } else {
            nextUpdate = nextUpdate.plusDays(1)
        }

        val intent = Intent(context, UpdateIntentService::class.java)
        val pendingIntent = PendingIntent.getService(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.set(AlarmManager.RTC_WAKEUP, nextUpdate.millis, pendingIntent)

        Log.d("UIS", "Next update scheduled at ${nextUpdate.toString()}")
    }


    /*
    suspend fun initialize(db: Db) {
        // Initialize job spawner
        val jobs = Jobs()

        // Launch jobs for all retrievals
        jobs.launch { db.announcements.items = async(apiService.announcements::apiV2AnnouncementsGet) }
        jobs.launch { db.dealers.items = async(apiService.dealers::apiV2DealersGet) }
        jobs.launch { db.days.items = async(apiService.days::apiV2EventConferenceDaysGet) }
        jobs.launch { db.rooms.items = async(apiService.rooms::apiV2EventConferenceRoomsGet) }
        jobs.launch { db.tracks.items = async(apiService.tracks::apiV2EventConferenceTracksGet) }
        jobs.launch { db.events.items = async(apiService.events::apiV2EventsGet) }
        jobs.launch { db.images.items = async(apiService.images::apiV2ImagesGet) }
        jobs.launch { db.knowledgeEntries.items = async(apiService.knowledgeEntries::apiV2KnowledgeEntriesGet) }
        jobs.launch { db.knowledgeGroups.items = async(apiService.knowledgeGroups::apiV2KnowledgeGroupsGet) }
        jobs.launch { db.maps.items = async(apiService.maps::apiV2MapsGet) }

        // Wait until all are finished
        jobs.joinAll()
    }*/
}