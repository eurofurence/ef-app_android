package org.eurofurence.connavigator.gcm

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.eurofurence.connavigator.util.NotificationFactory
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 14-4-2016.
 */
class MyGCMListenerService : FirebaseMessagingService() {
    fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data?.getString("message")
        logv { "GCM Message received" }
        logv { "Message from: %s".format(from) }
        logv { "Message data: %s".format(message) }

        sendNotification(message ?: "No message received")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        sendNotification(p0.data.get("message").toString())
    }

    fun sendNotification(message: String) {
        val notification = NotificationFactory(applicationContext).buildNotification("Broadcast from EF", message)
        val notMan: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notMan.notify(message.hashCode(), notification)
    }
}