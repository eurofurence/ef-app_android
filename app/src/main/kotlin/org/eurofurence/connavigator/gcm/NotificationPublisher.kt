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
        val TAG = "org.eurofurence.connavigator.gcm.NotificationPublisher.TAG"
        val ITEM = "org.eurofurence.connavigator.gcm.NotificationPublisher.ITEM"
    }

    override fun onReceive(context: Context, intent: Intent) {
        d { "Received intent to send a notification" }
        val notification = intent.getParcelableExtra<Notification>(ITEM)
        val tag = intent.getStringExtra(TAG) ?: ""

        d { "Sending notification to system" }
        context.notificationManager.notify(tag, 0, notification)
    }
}