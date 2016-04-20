package org.eurofurence.connavigator.gcm

import android.R
import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID
import com.google.android.gms.iid.InstanceIDListenerService
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 14-4-2016.
 */
class MyInstanceIDListenerService: InstanceIDListenerService() {
    companion object {
        val UPDATE_COMPLETE = "org.eurofurence.connavigator.gcm.REGISTERED"

        /**
         * Dispatches an update
         * @param context The host context for the service
         */
        fun dispatchUpdate(context: Context) {
            logv("UIS") { "Dispatching GCM Instance Listener" }
            context.startService(Intent(context, MyInstanceIDListenerService::class.java))
            context.startService(Intent(context, RegistrationIntentService::class.java))
        }
    }

    override fun onTokenRefresh() {
        logd { "REfreshing GCM token" }
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
    }
}
