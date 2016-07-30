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
        if(p0.notification.title == null) {
            sendNotification(p0.notification.body)
        } else {
            sendNotification(p0.notification.body, p0.notification.title)
        }
    }

    fun sendNotification(message: String, title: String = "Broadcast from EF") {
        val notification = NotificationFactory(applicationContext).buildNotification(title, message)
        val notMan: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notMan.notify(message.hashCode(), notification)
    }
}