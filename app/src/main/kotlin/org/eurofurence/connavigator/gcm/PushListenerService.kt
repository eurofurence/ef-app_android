package org.eurofurence.connavigator.gcm

import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.dispatchUpdate
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.fragments.FragmentViewHomeDirections
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.util.*

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

    private val basicIntent
        get() = NavDeepLinkBuilder(this)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.fragmentViewHome)
                .createPendingIntent()

    private fun createNotification(message: RemoteMessage) {
        info { "Received request to create generic notification" }

        factory.createBasicNotification()
                .addRegularText(message.title ?: "No title was sent!", message.message
                        ?: "No message was sent!")
                .setPendingIntent(basicIntent)
                .broadcast(message.relatedId ?: message.fallbackId)
    }

    private fun createAnnouncement(message: RemoteMessage) {
        info { "Received request to create announcement notification" }

        val intent = try {
            // Parse as a UUID so we're sure it's a uuid before making the intent
            val id = UUID.fromString(message.relatedId)

            val action = FragmentViewHomeDirections
                    .actionFragmentViewHomeToFragmentViewAnnouncement(id.toString())

            NavDeepLinkBuilder(this)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.fragmentViewAnnouncement)
                    .setArguments(action.arguments)
                    .createPendingIntent()
        } catch (_: Exception) {
            basicIntent
        }

        factory.createBasicNotification()
                .addRegularText("A new announcement from Eurofurence", message.title
                        ?: "No title was sent!")
                .addBigText(message.text ?: "No big text was supplied")
                .setPendingIntent(intent)
                .broadcast(message.relatedId ?: message.fallbackId)
    }
}