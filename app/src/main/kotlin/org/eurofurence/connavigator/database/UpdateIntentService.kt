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


    // TODO: Sticky intent since there should only be one pending update

    override fun onHandleIntent(intent: Intent?) {
        logv("UIS") { "Handling update intent service intent" }

        // Driver for loading and storing
        val driver = Database(this)

        // Get the old date
        val oldDate = driver.dateDb.items.firstOrNull() ?: Date(0)

        logv("UIS") { "Old date in database: $oldDate" }

        // Initialize the response
        val response = Intent(UPDATE_COMPLETE)

        // The following code is net and IO oriented, it could fail
        try {
            // Get the current endpoint status and its date
            val endpoint = api.endpointGet()
            val newDate = endpoint.currentDateTimeUtc
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)

            logv("UIS") { "New date on server: $newDate" }

            // Update the databases with the new data

            // Check for debug. this will change the dates so they work with the current dates
            if (preferences.getBoolean(resources.getString(R.string.debug_date_enabled), false)) {
                logd { "Changing dates instead of updating" }
                var dates = driver.eventConferenceDayDb.items

                val currentDate = DateTime.now()
                val offset = preferences.getString(resources.getString(R.string.debug_date_setting), "0").toInt()

                for (index in dates.indices) {
                    dates[index].date = currentDate.plusDays(index - offset).toString("yyyy-MM-dd")
                }
                driver.eventConferenceDayDb.syncWith(dates)
            } else {
                // If the setting is not set we'll just add the regular days
                driver.eventConferenceDayDb.syncWith(api.eventConferenceDayGet(oldDate))
            }
            driver.eventConferenceRoomDb.syncWith(api.eventConferenceRoomGet(oldDate))
            driver.eventConferenceTrackDb.syncWith(api.eventConferenceTrackGet(oldDate))
            driver.eventEntryDb.syncWith(api.eventEntryGet(oldDate))
            driver.imageDb.syncWith(api.imageGet(oldDate))
            driver.infoDb.syncWith(api.infoGet(oldDate))
            driver.infoGroupDb.syncWith(api.infoGroupGet(oldDate))

            // Set the new server date
            driver.dateDb.items = listOf(newDate)

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
}