package org.eurofurence.connavigator.gcm

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pawegio.kandroid.d
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.notificationManager

/**
 * Notification Publisher
 *
 * Manages the receiving of a broadcast for a notifications and exporting said notification
 *
 *
 */
class NotificationPublisher : BroadcastReceiver(), AnkoLogger {
    companion object {
        const val TAG = "org.eurofurence.connavigator.gcm.NotificationPublisher.TAG"
        const val ITEM = "org.eurofurence.connavigator.gcm.NotificationPublisher.ITEM"
    }

    override fun onReceive(context: Context, intent: Intent) {
        info { "Received intent to send a notification" }
        val notification = intent.getParcelableExtra<Notification>(ITEM)
        val tag = intent.getStringExtra(TAG) ?: ""

        info { "Sending notification to system" }
        context.notificationManager.notify(tag, tag.hashCode(), notification)
        info { "Done sending notifications"}
    }
}