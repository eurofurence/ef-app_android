package org.eurofurence.connavigator.gcm

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.gcm.GcmListenerService;
import org.eurofurence.connavigator.util.extensions.logd

/**
 * Created by David on 14-4-2016.
 */
class MyGCMListenerService: GcmListenerService() {

    override fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data?.getString("message")
        logd { "GCM Message received" }
        logd { "Message from: %s".format(from) }
        logd{ "Message data: %s".format(message) }
    }
}