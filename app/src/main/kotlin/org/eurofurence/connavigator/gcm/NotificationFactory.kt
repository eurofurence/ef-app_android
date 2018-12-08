package org.eurofurence.connavigator.gcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.NavActivity
import org.jetbrains.anko.intentFor
import java.util.*

fun NotificationManager.cancelFromRelated(identity: UUID) =
        cancel(identity.toString(), 0)

/**
 * Creates a basic notification
 */
class NotificationFactory(var context: Context) {
    val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
    } else {
        NotificationCompat.Builder(context)
    }

    fun broadcast(tag: String) {
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
    fun createBasicNotification() = this.apply {
        builder.setSmallIcon(R.drawable.ic_launcher_negative)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setLights(Color.argb(255, 0, 100, 89), 1000, 1000)
                .setVibrate(longArrayOf(250, 100, 250, 100))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
    }

    /**
     * Sets an activity to launch on notification taps
     */
    fun setPendingIntent(pendingIntent: PendingIntent) = this.apply {
        builder.setContentIntent(pendingIntent)
    }

    fun addBigText(bigText: String) = this.apply {
        builder.setStyle(NotificationCompat.BigTextStyle()
                .bigText(bigText))
    }

    fun addRegularText(title: String, text: String) = this.apply {
        builder.setContentTitle(title)
        builder.setContentText(text)
    }

    fun build() = builder.build()
}