package org.eurofurence.connavigator.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pawegio.kandroid.IntentFor
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.DebugPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

/**
 * Created by requinard on 7/30/17.
 */
class ResetReceiver : BroadcastReceiver(), AnkoLogger {
    override fun onReceive(context: Context, intent: Intent?) {
        info { "Clearing all data" }

        info { "Emptying DB" }
        RootDb(context).clear()

        info { "Purging images" }
        imageService.clear()

        info { "Annihilating login information" }
        AuthPreferences.clear()

        info { "Bombarding user settings" }
        AppPreferences.clear()
        DebugPreferences.clear()
        AnalyticsPreferences.clear()

        context.longToast("App reset has been completed. Closing.")

        task {
            Thread.sleep(100)
        } success{
            info { "Committing ritualistic suicide" }
            System.exit(621)
        }


    }

    companion object {
        fun fire(context: Context) = context.sendBroadcast(IntentFor<ResetReceiver>(context))
    }
}