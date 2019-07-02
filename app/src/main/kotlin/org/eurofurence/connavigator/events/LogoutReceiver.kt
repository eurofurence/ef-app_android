package org.eurofurence.connavigator.events

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.eurofurence.connavigator.services.InstanceIdService
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by requinard on 7/30/17.
 */
class LogoutReceiver: BroadcastReceiver(), AnkoLogger {
    /**
     * Removes user registration details and sends data update
     */
    override fun onReceive(context: Context, intent: Intent) {
        info {"Logging user out"}
        
        AuthPreferences.token = ""
        AuthPreferences.clear()
        InstanceIdService().reportToken()
        DataChanged.fire(context, "User logged out")
    }
}