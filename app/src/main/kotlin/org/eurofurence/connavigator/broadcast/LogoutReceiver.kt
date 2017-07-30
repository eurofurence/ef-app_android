package org.eurofurence.connavigator.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import android.support.v4.content.LocalBroadcastManager
import org.eurofurence.connavigator.gcm.InstanceIdService
import org.eurofurence.connavigator.pref.AuthPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by requinard on 7/30/17.
 */
class LogoutReceiver: BroadcastReceiver(), AnkoLogger {
    /**
     * Removes user registration details and sends dataupdate
     */
    override fun onReceive(context: Context, intent: Intent) {
        info {"Logging user out"}

        AuthPreferences.clear()
        InstanceIdService().reportToken()
        DataChanged.fire(context, "User logged out")
    }

    companion object {
        fun fire(context: Context) = context.sendBroadcast(Intent(context, LogoutReceiver::class.java))
    }
}