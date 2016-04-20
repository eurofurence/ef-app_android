package org.eurofurence.connavigator.gcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.google.android.gms.gcm.GcmListenerService
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 14-4-2016.
 */
class MyGCMListenerService : GcmListenerService() {
    override fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data?.getString("message")
        logv { "GCM Message received" }
        logv { "Message from: %s".format(from) }
        logv { "Message data: %s".format(message) }

        sendNotification(message ?: "No message received")
    }

    fun sendNotification(message: String) {
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("New message from EF")
                .setContentText(message)

        val notMan: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notMan.notify(0, notificationBuilder.build())
    }
}