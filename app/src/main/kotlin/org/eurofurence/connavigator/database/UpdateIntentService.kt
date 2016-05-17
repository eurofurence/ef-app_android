package org.eurofurence.connavigator.database

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import io.swagger.client.api.DefaultApi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.*
import org.joda.time.DateTime
import java.util.*

/**
 * Updates the database on request.
 */
class UpdateIntentService(val api: DefaultApi = DefaultApi()) : IntentService("UpdateIntentService") {
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
        val oldDate = database.dateDb.items.firstOrNull() ?: Date(0)

        logv("UIS") { "Old date in database: $oldDate" }

        // Initialize the response
        val response = Intent(UPDATE_COMPLETE)

        // The following code is net and IO oriented, it could fail
        try {
            // Get the current endpoint status and its date
            val endpoint = api.endpointGet()
            val newDate = endpoint.currentDateTimeUtc

            logv("UIS") { "New date on server: $newDate" }

            // Update the databases with the new data
            database.announcementDb.syncWith(loadAnnouncements(oldDate))
            database.dealerDb.syncWith(loadDealers(oldDate))
            database.eventConferenceDayDb.syncWith(loadConferenceDays(oldDate))
            database.eventConferenceRoomDb.syncWith(loadConferenceRooms(oldDate))
            database.eventConferenceTrackDb.syncWith(loadConferenceTracks(oldDate))
            database.eventEntryDb.syncWith(loadEvents(oldDate))
            database.imageDb.syncWith(loadImages(oldDate))
            database.infoDb.syncWith(loadInfos(oldDate))
            database.infoGroupDb.syncWith(loadInfoGroups(oldDate))

            // Set the new server date
            database.dateDb.items = listOf(newDate)

            // Make the success response message
            response.booleans["success"] = true
            response.objects["time"] = newDate

            logv("UIS") { "Completed update successfully" }
        } catch(ex: Throwable) {
            // Make the fail response message, transfer exception
            response.booleans["success"] = true
            response.objects["time"] = oldDate
            response.objects["reason"] = ex

            loge("UIS", ex) { "Completed update with error" }
        }

        // Send a broadcast notifying completion of this action
        LocalBroadcastManager.getInstance(this).sendBroadcast(response)
    }

    private fun loadAnnouncements(oldDate: Date) = api.announcementGet(oldDate)

    private fun loadDealers(oldDate: Date) = api.dealerGet(oldDate)

    private fun loadConferenceDays(oldDate: Date) =
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
                api.eventConferenceDayGet(oldDate)
            }

    private fun loadConferenceRooms(oldDate: Date) =
            api.eventConferenceRoomGet(oldDate)


    private fun loadConferenceTracks(oldDate: Date) = api.eventConferenceTrackGet(oldDate)

    private fun loadEvents(oldDate: Date) = api.eventEntryGet(oldDate)

    private fun loadImages(oldDate: Date) = api.imageGet(oldDate)

    private fun loadInfos(oldDate: Date) = api.infoGet(oldDate)

    private fun loadInfoGroups(oldDate: Date) = api.infoGroupGet(oldDate)
}