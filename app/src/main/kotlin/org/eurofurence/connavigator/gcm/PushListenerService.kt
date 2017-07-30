package org.eurofurence.connavigator.gcm

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.pref.RemotePreferences
import org.jetbrains.anko.*

/**
 * Created by David on 14-4-2016.
 */
class PushListenerService : FirebaseMessagingService(), AnkoLogger {
    val factory by lazy { NotificationFactory(applicationContext) }

    fun subscribe() {
        val messaging = FirebaseMessaging.getInstance()

        var topics = listOf(
                "live-all",
                "live-android"
        )

        if (BuildConfig.DEBUG) {
            topics += listOf("test-all", "test-android")
        }

        topics.forEach { messaging.subscribeToTopic(it) }

        info { "Push token: " + FirebaseInstanceId.getInstance().token }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        info { "Received push message" }
        debug { "Message payload :" + message.data }

        when (message.data["Event"]) {
            "Announcement" -> createAnnouncement(message)
            "ImportantAnnouncement" -> createHighPriorityAnnouncement(message)
            "Sync" -> syncData(message)
            "Notification" -> createNotification(message)
            else -> warn("Message did not contain a valid event. Abandoning!")
        }
    }

    private fun syncData(message: RemoteMessage) {
        info { "Received request to sync data" }

        applicationContext.sendBroadcast(intentFor<UpdateIntentService>())
        RemotePreferences.update()
    }

    private fun createAnnouncement(message: RemoteMessage) {
        info { "Received request to create announcement notification" }

        val notification = factory.createBasicNotification()

        factory.addRegularText(notification, "A new announcement from Eurofurence", message.data["Title"] ?: "")
        factory.addBigText(notification, message.data["Text"] ?: "")
        factory.setActivity(notification)

        factory.broadcastNotification(notification)
    }

    private fun createNotification(message: RemoteMessage) {
        info { "Received request to create generic notification" }

        val notification = factory.createBasicNotification()

        factory.addRegularText(notification, message.data["Title"] ?: "", message.data["Message"] ?: "")
        factory.setActivity(notification)

        factory.broadcastNotification(notification)
    }

    private fun createHighPriorityAnnouncement(message: RemoteMessage) {
        info { "Received request to make high priority announcement" }

        val notification = factory.createBasicNotification()


        factory.addRegularText(notification, message.data["Title"] ?: "", message.data["Message"] ?: "")
        factory.addBigText(notification, message.data["Message"] ?: "")
        factory.makeHighPriority(notification)
        factory.setActivity(notification)

        factory.broadcastNotification(notification)
    }
}