package org.eurofurence.connavigator.gcm

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 20-4-2016.
 */
class RegistrationIntentService : IntentService("RegistrationIntentService") {
    lateinit var instanceID: InstanceID
    lateinit var token: String;

    override fun onHandleIntent(intent: Intent?) {
        instanceID = InstanceID.getInstance(this)
        token = instanceID.getToken("1003745003618", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)

        logv { "GCM Token is: %s".format(token) }

        sendRegistrationToken()
    }

    /*
    Send registration token to server
     */
    private fun sendRegistrationToken() {
        //todo: Implement
    }
}