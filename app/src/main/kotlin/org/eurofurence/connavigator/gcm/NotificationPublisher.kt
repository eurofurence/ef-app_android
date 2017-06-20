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
        val NOTIFICATION_ID = "notification-id"
        val NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = intent.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent.getIntExtra(NOTIFICATION_ID, 0)

        logd{ "NOT: notification ID is $id"}
        notificationManager.notify(id, notification)
    }
}