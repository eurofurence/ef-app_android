package org.eurofurence.connavigator.gcm

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.database.dispatchUpdate
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * Created by David on 14-4-2016.
 */
class PushListenerService : FirebaseMessagingService(), AnkoLogger {
    private val factory by lazy { NotificationFactory(applicationContext) }

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
            "Sync" -> syncData(message)
            "Notification" -> createNotification(message)
            "Announcement" -> createAnnouncement(message)
            else -> warn("Message did not contain a valid event. Abandoning!")
        }
    }

    private fun syncData(message: RemoteMessage) {
        info { "Received request to sync data" }

        dispatchUpdate(applicationContext)
        RemotePreferences.update()
    }


    private val RemoteMessage.title get() = data["Title"]

    private val RemoteMessage.text get() = data["Text"]

    private val RemoteMessage.message get() = data["Message"]

    private val RemoteMessage.relatedId get() = data["RelatedId"]

    private val RemoteMessage.fallbackId get() = hashCode().toString()

    private fun createNotification(message: RemoteMessage) {
        info { "Received request to create generic notification" }

        val notification = factory.createBasicNotification()

        factory.addRegularText(notification, message.title ?: "", message.message ?: "")
        factory.setActivity(notification)

        factory.broadcastNotification(notification, message.relatedId ?: message.fallbackId)
    }

    private fun createAnnouncement(message: RemoteMessage) {
        info { "Received request to create announcement notification" }

        val notification = factory.createBasicNotification()

        factory.addRegularText(notification, "A new announcement from Eurofurence", message.title ?: "")
        factory.addBigText(notification, message.text)
        factory.setActivity(notification)

        if (AppPreferences.notificationsEnabled) factory.broadcastNotification(notification, message.relatedId
                ?: message.fallbackId)
    }
}