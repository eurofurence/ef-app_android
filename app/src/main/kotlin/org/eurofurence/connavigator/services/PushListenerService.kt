package org.eurofurence.connavigator.services

import androidx.navigation.NavDeepLinkBuilder
import androidx.work.WorkManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.notifications.EFNotificationChannel
import org.eurofurence.connavigator.notifications.NotificationFactory
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.ui.activities.NavActivity
import org.eurofurence.connavigator.ui.fragments.HomeFragmentDirections
import org.eurofurence.connavigator.workers.DataUpdateWorker
import org.eurofurence.connavigator.workers.FetchPrivateMessageWorker
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import org.joda.time.Duration

/**
 * Created by David on 14-4-2016.
 */
class PushListenerService : FirebaseMessagingService(), AnkoLogger {
    private val factory by lazy { NotificationFactory(applicationContext) }

    fun subscribe() {
        val messaging = FirebaseMessaging.getInstance()

        val topics = listOf(
                "${BuildConfig.CONVENTION_IDENTIFIER}",
                "${BuildConfig.CONVENTION_IDENTIFIER}-android"
        )
        topics.forEach { messaging.subscribeToTopic(it) }

        info { "Push token: " + FirebaseInstanceId.getInstance().token }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        info { "Received push message" }
        debug { "Message payload :" + message.data }

        when (message.data["Event"]) {
            "Sync" -> syncData()
            "Notification" -> createNotification(message)
            "Announcement" -> createAnnouncement(message)
            else -> warn("Message did not contain a valid event. Abandoning!")
        }
    }

    private fun syncData() {
        info { "Received request to sync data" }

        DataUpdateWorker.execute(this)
        RemotePreferences.update()
    }


    private val RemoteMessage.title get() = data["Title"]

    private val RemoteMessage.text get() = data["Text"]

    private val RemoteMessage.message get() = data["Message"]

    private val RemoteMessage.relatedId get() = data["RelatedId"]

    private val RemoteMessage.fallbackId get() = hashCode().toString()

    private val basicIntent
        get() = NavDeepLinkBuilder(this)
                .setComponentName(NavActivity::class.java)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.navHome)
                .createPendingIntent()

    private fun createNotification(message: RemoteMessage) {
        info { "Received request to create generic notification" }

        // Fetch in background on receiving, also assume that the cache is invalid every time.
        FetchPrivateMessageWorker.execute(this)

        val action = HomeFragmentDirections
                .actionFragmentViewHomeToFragmentViewMessageItem(message.relatedId!!)

        val intent = NavDeepLinkBuilder(this)
                .setComponentName(NavActivity::class.java)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.navMessageItem)
                .setArguments(action.arguments)
                .createPendingIntent()


        factory.createBasicNotification()
                .addRegularText(message.title ?: "No title was sent!", message.message
                        ?: "No message was sent!")
                .setPendingIntent(intent)
                .setChannel(EFNotificationChannel.PRIVATE_MESSAGE)
                .broadcast(message.relatedId ?: message.fallbackId)
    }

    private fun createAnnouncement(message: RemoteMessage) {
        info { "Received request to create announcement notification" }

        syncData()

        val intent = try {
            val action = HomeFragmentDirections
                    .actionFragmentViewHomeToFragmentViewAnnouncement(message.relatedId!!)

            NavDeepLinkBuilder(this)
                    .setComponentName(NavActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.navAnnouncementItem)
                    .setArguments(action.arguments)
                    .createPendingIntent()
                    .apply {
                        info { "Created pending activity" }
                    }
        } catch (_: Exception) {
            warn { "Creating basic intent! You failed!!!" }
            basicIntent
        }

        factory.createBasicNotification()
                .addRegularText(message.title ?: "No title was sent!", message.text
                        ?: "No extra text supplied")
                .addBigText(message.text ?: "No big text was supplied")
                .setPendingIntent(intent)
                .setChannel(EFNotificationChannel.ANNOUNCEMENT)
                .broadcast(message.relatedId ?: message.fallbackId)
    }
}