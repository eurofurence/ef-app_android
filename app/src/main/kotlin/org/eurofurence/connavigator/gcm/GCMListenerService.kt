package org.eurofurence.connavigator.gcm

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.util.NotificationFactory
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 14-4-2016.
 */
class MyGCMListenerService : FirebaseMessagingService() {
    fun subscribe() {
        val messaging = FirebaseMessaging.getInstance()
        if (BuildConfig.DEBUG) {
            messaging.subscribeToTopic("test")
        }
        messaging.subscribeToTopic("announcements")
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        logd { "Received message" }

        if (p0.data.containsKey("message") && p0.data.containsKey("title")) {
            sendNotification(p0.data.get("message").toString(), p0.data.get("title").toString())
        } else if (p0.data.containsKey("message")) {
            sendNotification(p0.data.get("message").toString())
        } else if (p0.notification != null) {
            if (p0.notification.title == null) {
                sendNotification(p0.notification.body)
            } else {
                sendNotification(p0.notification.body, p0.notification.title)
            }
        }
    }

    fun sendNotification(message: String, title: String = "Broadcast from EF") {
        val notification = NotificationFactory(applicationContext).buildNotification(title, message)
        val notMan: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notMan.notify(message.hashCode(), notification)
    }
}