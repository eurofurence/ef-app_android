package org.eurofurence.connavigator.database

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import io.swagger.client.model.MapEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.store.SyncIDB
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.webapi.apiService
import org.joda.time.DateTime
import java.util.*

/**
 * Updates the database on request.
 */
class UpdateIntentService() : IntentService("UpdateIntentService") {
    companion object {
        val UPDATE_COMPLETE = "org.eurofurence.connavigator.driver.UPDATE_COMPLETE"

        /**
         * Dispatches an update
         * @param context The host context for the service
         */
        fun dispatchUpdate(context: Context) {
            logv("UIS") { "Dispatching update" }
            context.startService(Intent(context, UpdateIntentService::class.java))
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

        // Initialize the response
        val response = Intent(UPDATE_COMPLETE)

        // The following code is net and IO oriented, it could fail
        try {
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
            fun  <T> checkedUpdate(name: String, db: SyncIDB<T>, provider: (Date?) -> List<T>) {
                if (oldDate lt endpoint[name]?.deltaStartDateTimeUtc)
                    db.items = provider(null)
                else
                    db.syncWith(provider(oldDate))
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

            // Set the new server date
            database.dateDb.items = listOf(newDate)

            // Make the success response message
            response.booleans["success"] = true
            response.objects["time"] = newDate

            logv("UIS") { "Completed update successfully" }
        } catch(ex: Throwable) {
            // Make the fail response message, transfer exception
            response.booleans["success"] = false
            response.objects["time"] = oldDate
            response.objects["reason"] = ex

            loge("UIS", ex) { "Completed update with error" }
        }

        // Send a broadcast notifying completion of this action
        LocalBroadcastManager.getInstance(this).sendBroadcast(response)
    }

    private fun loadMapEntry(oldDate: Date?) = apiService.api.mapEntryGet(oldDate)

    private fun loadAnnouncements(oldDate: Date?) = apiService.api.announcementGet(oldDate)

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

    private fun loadImages(oldDate: Date?) = apiService.api.imageGet(oldDate)

    private fun loadInfos(oldDate: Date?) = apiService.api.infoGet(oldDate)

    private fun loadInfoGroups(oldDate: Date?) = apiService.api.infoGroupGet(oldDate)
}