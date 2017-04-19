package org.eurofurence.connavigator.database

import android.app.AlarmManager
import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import io.swagger.client.model.Announcement
import io.swagger.client.model.Image
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.store.SyncIDB
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.NotificationFactory
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.webapi.apiService
import org.joda.time.DateTime
import java.util.*

/**
 * Updates the database on request.
 */
class UpdateIntentService : IntentService("UpdateIntentService") {
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

        private fun schedule(context: Context) {
            Log.d("UIS", "Scheduling the next data update")

            val database = Database(context)

            var nextUpdate = DateTime.now()

            if (database.eventConferenceDayDb.items
                    .map { DateTime.parse(it.date) }
                    .filter { it.toDate().equals(DateTime.now().toDate()) }
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
    }

    val database by lazy { Database(this) }

    val preferences by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    // TODO: Sticky intent since there should only be one pending update
    override fun onHandleIntent(intent: Intent?) {
        logv("UIS") { "Handling update intent service intent" }

        // Get the old date
        val oldDate = database.dateDb.items.firstOrNull()

        logv("UIS") { "Old date in database: $oldDate" }

        // Initialize the response, the following code is net and IO oriented, it could fail
        val response = {
            // Get the current endpoint status and its date
            val endpoint = apiService.api.endpointGet()
            val newDate = endpoint.currentDateTimeUtc

            logv("UIS") { "New date on server: $newDate" }

            /**
             * Checks if the table entity contains a completely different version, then write instead of sync
             * @param name The corresponding table name
             * @param db The database to synchronize
             * @param provider The provider of data, relative to a delta
             */
            fun <T> checkedUpdate(name: String, db: SyncIDB<T>, provider: (Date?) -> List<T>) {
                if (oldDate lt endpoint[name]?.deltaStartDateTimeUtc)
                    db.items = provider(null)
                else if (oldDate lt endpoint[name]?.lastChangeDateTimeUtc) {
                    logd { "New data for $name" }
                    db.syncWith(provider(oldDate))
                } else
                    logd { "No new data for $name" }
            }

            // Update or replace tables
            checkedUpdate("Announcement", database.announcementDb, { loadAnnouncements(it) })
            checkedUpdate("Dealer", database.dealerDb, { loadDealers(it) })
            checkedUpdate("EventConferenceDay", database.eventConferenceDayDb, { loadConferenceDays(it) })
            checkedUpdate("EventConferenceRoom", database.eventConferenceRoomDb, { loadConferenceRooms(it) })
            checkedUpdate("EventConferenceTrack", database.eventConferenceTrackDb, { loadConferenceTracks(it) })
            checkedUpdate("EventEntry", database.eventEntryDb, { loadEvents(it) })
            checkedUpdate("Image", database.imageDb, { loadImages(it) })
            checkedUpdate("Info", database.infoDb, { loadInfos(it) })
            checkedUpdate("InfoGroup", database.infoGroupDb, { loadInfoGroups(it) })
            checkedUpdate("MapEntry", database.mapEntryDb, { loadMapEntry(it) })
            checkedUpdate("Map", database.mapEntityDb, { loadMapEntity(it) })
            // Set the new server date
            database.dateDb.items = listOf(newDate)


            // Make the success response message
            logv("UIS") { "Completed update successfully" }
            UPDATE_COMPLETE.toIntent {
                booleans["success"] = true
                objects["time"] = newDate
            }
        } catchAlternative { ex: Throwable ->
            // Make the fail response message, transfer exception
            loge("UIS", ex) { "Completed update with error" }
            UPDATE_COMPLETE.toIntent {
                booleans["success"] = false
                objects["time"] = oldDate
                objects["reason"] = ex
            }
        }

        schedule(this)

        // Send a broadcast notifying completion of this action
        LocalBroadcastManager.getInstance(this).sendBroadcast(response)
    }

    private fun loadMapEntity(oldDate: Date?) = apiService.api.mapGet(oldDate)

    private fun loadMapEntry(oldDate: Date?) = apiService.api.mapEntryGet(oldDate)

    private fun loadAnnouncements(oldDate: Date?): List<Announcement> {
        val announcements = apiService.api.announcementGet(oldDate)

        announcements.filter { DateTime.now().isAfter(it.validFromDateTimeUtc.time) }
                .filter { DateTime.now().isBefore(it.validUntilDateTimeUtc.time) }
                .forEach { NotificationFactory(this).showNotification(it.title, it.content) }

        return announcements
    }

    private fun loadDealers(oldDate: Date?) = apiService.api.dealerGet(oldDate)

    private fun loadConferenceDays(oldDate: Date?) =
            if (preferences.getBoolean(resources.getString(R.string.debug_date_enabled), false)) {
                logd { "Changing dates instead of updating" }
                val dates = database.eventConferenceDayDb.items

                val currentDate = DateTime.now()
                val offset = preferences.getString(resources.getString(R.string.debug_date_setting), "0").toInt()

                var i = 0

                for (index in dates) {
                    index.date = currentDate.plusDays(i - offset).toString("yyyy-MM-dd")
                    i++
                }
                dates.toList()
            } else {
                // If the setting is not set we'll just add the regular days
                apiService.api.eventConferenceDayGet(oldDate)
            }

    private fun loadConferenceRooms(oldDate: Date?) =
            apiService.api.eventConferenceRoomGet(oldDate)


    private fun loadConferenceTracks(oldDate: Date?) = apiService.api.eventConferenceTrackGet(oldDate)

    private fun loadEvents(oldDate: Date?) = apiService.api.eventEntryGet(oldDate)

    private fun loadImages(oldDate: Date?): List<Image> {
        val images = apiService.api.imageGet(oldDate)
        images.forEach { imageService.recache(it) }

        return images
    }

    private fun loadInfos(oldDate: Date?) = apiService.api.infoGet(oldDate)

    private fun loadInfoGroups(oldDate: Date?) = apiService.api.infoGroupGet(oldDate)
}