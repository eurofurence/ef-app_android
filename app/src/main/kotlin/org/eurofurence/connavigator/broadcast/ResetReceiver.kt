package org.eurofurence.connavigator.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.DebugPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast

/**
 * Created by requinard on 7/30/17.
 */
class ResetReceiver : BroadcastReceiver(), AnkoLogger {
    override fun onReceive(context: Context, intent: Intent?) {
        clearData(context)
    }

    fun clearData(context: Context) {
        info { "Clearing all data" }

        info { "Emptying DB" }
        RootDb(context).clear()

        info { "Purging images" }
        imageService.clear()

        info { "Bombarding user settings" }
        AppPreferences.clear()
        DebugPreferences.clear()
        AnalyticsPreferences.clear()

        context.longToast(context.getString(R.string.clear_completed_closing))

        task {
            Thread.sleep(1000)
        } success {
            info { "Committing ritualistic suicide" }
            System.exit(621)
        }
    }
}