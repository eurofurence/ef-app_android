package org.eurofurence.connavigator.gcm

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pawegio.kandroid.d
import org.jetbrains.anko.notificationManager

/**
 * Notification Publisher
 *
 * Manages the receiving of a broadcast for a notifiction and exporting said notification
 *
 *
 */
class NotificationPublisher : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = "notification-id"
        const val NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        d { "Received intent to send a notification" }
        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)

        d { "Sending notification to system" }
        context.notificationManager.notify(id, notification)
    }
}