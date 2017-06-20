package org.eurofurence.connavigator.gcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pawegio.kandroid.d
import com.pawegio.kandroid.i
import com.pawegio.kandroid.w
import org.eurofurence.connavigator.BuildConfig

/**
 * Created by David on 14-4-2016.
 */
class PushListenerService : FirebaseMessagingService() {
    val factory by lazy { NotificationFactory(applicationContext) }

    fun subscribe() {
        val messaging = FirebaseMessaging.getInstance()
        if (BuildConfig.DEBUG) {
            messaging.subscribeToTopic("test")
        }
        messaging.subscribeToTopic("announcements")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        i { "Received push message" }
        d { "Message payload :" + message.data }

        when (message.data["Event"]) {
            "Announcement" -> createAnnouncement(message)
            "Lassie" -> createHighPriorityAnnouncement(message)
            "Sync" -> syncData(message)
            else -> w("Message did not contain a valid event. Abandoning!")
        }


    }

    private fun syncData(message: RemoteMessage) {
        i { "Received request to sync data" }
    }

    private fun createAnnouncement(message: RemoteMessage) {
        i { "Received request to create announcement notification" }

        val notification = factory.createBasicNotification()
                .setContentTitle(message.data["Title"])
                .setContentText(message.data["Text"])

        factory.broadcastNotification(notification)
    }

    private fun createHighPriorityAnnouncement(message: RemoteMessage) {
        i { "Received request to make high priority announcement" }

        val notification = factory.createBasicNotification()
                .setContentTitle(message.data["Title"])
                .setContentText(message.data["Text"])

        factory.broadcastNotification(factory.makeHighPriority(notification))
    }
}