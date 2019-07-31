package org.eurofurence.connavigator.events

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.preferences.*
import org.eurofurence.connavigator.services.ImageService
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
        ImageService.clear()

        info { "Bombarding user settings" }
        AppPreferences.clear()
        DebugPreferences.clear()
        AnalyticsPreferences.clear()
        BackgroundPreferences.clear()

        context.longToast(context.getString(R.string.clear_completed_closing))

        BackgroundPreferences.observer.onNext(LoadingState.UNINITIALIZED)
    }
}