package org.eurofurence.connavigator.gcm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.ActivityRoot
import org.jetbrains.anko.intentFor
import java.util.*

fun NotificationManager.cancelFromRelated(identity: UUID) =
        cancel(identity.toString(), 0)

/**
 * Creates a basic notification
 */
class NotificationFactory(var context: Context) {
    fun broadcastNotification(builder: NotificationCompat.Builder, tag: String) {
        val notification = builder.build()

        val intent = context.intentFor<NotificationPublisher>(
                NotificationPublisher.TAG to tag,
                NotificationPublisher.ITEM to notification
        )

        context.sendBroadcast(intent)
    }

    /**
     * Creates a basic notification that features the EF logo, colours and vibration
     */
    fun createBasicNotification(): NotificationCompat.Builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_launcher_negative)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
            .setLights(Color.argb(255, 0, 100, 89), 1000, 1000)
            .setVibrate(longArrayOf(250, 100, 250, 100))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

    /**
     * Sets an activity to launch on notification taps
     */
    fun setActivity(builder: NotificationCompat.Builder): NotificationCompat.Builder {
        // On a click event we want to start an activity
        val intentToExecute = Intent(context, ActivityRoot::class.java)

        // Attach the intent to a pending intent that our app can consume
        val pendingIntent = PendingIntent.getActivity(context, 0, intentToExecute, 0)

        return builder.setContentIntent(pendingIntent)
    }

    /**
     * Makes a notification high priority and persistent
     */
    fun makeHighPriority(builder: NotificationCompat.Builder) = builder.apply {
        priority = Notification.PRIORITY_HIGH
        setOngoing(true)
    }

    fun addBigText(builder: NotificationCompat.Builder, bigText: String?): NotificationCompat.Builder = builder.setStyle(
            NotificationCompat.BigTextStyle()
                    .bigText(bigText)
    )

    fun addRegularText(builder: NotificationCompat.Builder, title: String, text: String) = builder.apply {
        this.setContentTitle(title)
        this.setContentText(text)
    }
}