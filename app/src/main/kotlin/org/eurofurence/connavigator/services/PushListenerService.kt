package org.eurofurence.connavigator.services

import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.swagger.client.model.PostFcmDeviceRegistrationRequest
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.notifications.EFNotificationChannel
import org.eurofurence.connavigator.notifications.NotificationFactory
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.ui.activities.NavActivity
import org.eurofurence.connavigator.ui.fragments.HomeFragmentDirections
import org.eurofurence.connavigator.workers.DataUpdateWorker
import org.eurofurence.connavigator.workers.FetchPrivateMessageWorker
import org.eurofurence.connavigator.dropins.AnkoLogger
import org.eurofurence.connavigator.preferences.AuthPreferences

/**
 * Created by David on 14-4-2016.
 */
class PushListenerService : FirebaseMessagingService(), AnkoLogger {
    private val factory by lazy { NotificationFactory(applicationContext) }

    fun subscribe() {
        val messaging = FirebaseMessaging.getInstance()
        messaging.subscribeToTopic("${BuildConfig.CONVENTION_IDENTIFIER}")
        messaging.subscribeToTopic("${BuildConfig.CONVENTION_IDENTIFIER}-android")
    }

    fun fetch() {
        // Force update token.
        FirebaseMessaging.getInstance().token.let { task ->
            task.addOnSuccessListener {
                info("Token received successfully: $it")
                onNewToken(it)
            }
            task.addOnFailureListener {
                warn("Token not received: ${it.stackTraceToString()}")
            }
        }
    }

    override fun onNewToken(token: String) {
        info { "Submitting private message tokens: $token" }

        if (AuthPreferences.isLoggedIn) {
            info { "User is logged in, associating token with user" }
            apiService.pushNotifications.addHeader("Authorization", AuthPreferences.asBearer())
        } else {
            warn { "User is not logged in, will not send token" }
            apiService.pushNotifications.addHeader("Authorization", "")
        }

        task {
            var sendTopics = listOf(
                "android",
                "version-${BuildConfig.VERSION_NAME}",
                "cid-${BuildConfig.CONVENTION_IDENTIFIER}"
            )

            if (BuildConfig.DEBUG) sendTopics += "debug"

            info { "Making network request" }
            apiService.pushNotifications.apiPushNotificationsFcmDeviceRegistrationPost(
                PostFcmDeviceRegistrationRequest().apply {
                    deviceId = token
                    topics = sendTopics
                }
            )
        } success {
            info { "Token was successfully registered" }
            AuthPreferences.lastReportedFirebaseToken = token
        } fail {
            warn { "Token registration failed!" }
            warn { it.toString() }
        }
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
            .addRegularText(
                message.title ?: "No title was sent!", message.message
                    ?: "No message was sent!"
            )
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
            .addRegularText(
                message.title ?: "No title was sent!", message.text
                    ?: "No extra text supplied"
            )
            .addBigText(message.text ?: "No big text was supplied")
            .setPendingIntent(intent)
            .setChannel(EFNotificationChannel.ANNOUNCEMENT)
            .broadcast(message.relatedId ?: message.fallbackId)
    }
}