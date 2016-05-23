package org.eurofurence.connavigator.gcm

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import com.google.android.gms.gcm.GcmListenerService
import org.eurofurence.connavigator.util.NotificationFactory
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 14-4-2016.
 */
class MyGCMListenerService : GcmListenerService() {
    override fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data?.getString("message")
        logv { "GCM Message received" }
        logv { "Message from: %s".format(from) }
        logv { "Message data: %s".format(message) }

        sendNotification(message ?: "No message received")
    }

    fun sendNotification(message: String) {
        val notification = NotificationFactory(applicationContext).buildNotification("Broadcast from EF", message)
        val notMan: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notMan.notify(0, notification)
    }
}