package org.eurofurence.connavigator.notifications

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.notificationManager
import org.joda.time.DateTime
import java.util.*

fun NotificationManager.cancelFromRelated(identity: UUID) =
        cancel(identity.toString(), 0)

/**
 * Creates a basic notification
 */
class NotificationFactory(var context: Context) {
    var builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    fun setupChannels() {
        EFNotificationChannel.values().map {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // If we're debugging, remove the notification channels
                if (BuildConfig.DEBUG) context.notificationManager.deleteNotificationChannel(it.toString())

                context.notificationManager.createNotificationChannel(this.getChannel(it))
            }
        }
    }

    /**
     * Creates a basic notification that features the EF logo, colours and vibration
     */
    fun createBasicNotification() = this.apply {
        builder = builder.setSmallIcon(R.drawable.ic_launcher_negative)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setLights(Color.argb(255, 0, 100, 89), 1000, 1000)
                .setVibrate(longArrayOf(250, 100, 250, 100))
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            context.notificationManager.createNotificationChannel(channel)
            builder = builder.setChannelId(channel.id)
        }
    }

    /**
     * Sets an activity to launch on notification taps
     */
    fun setPendingIntent(pendingIntent: PendingIntent) = this.apply {
        builder = builder.setContentIntent(pendingIntent)
    }

    fun addBigText(bigText: String) = this.apply {
        builder = builder.setStyle(NotificationCompat.BigTextStyle()
                .bigText(bigText))
    }

    fun addRegularText(title: String, text: String) = this.apply {
        builder = builder.setContentTitle(title)
        builder = builder.setContentText(text)
    }

    fun countdownTo(date: DateTime) = this.apply {
        builder = builder.setWhen(date.millis)
                .setUsesChronometer(true)
    }

    fun setChannel(channel: EFNotificationChannel) = this.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = builder.setChannelId(getChannel(channel).id)
        }
    }

    @SuppressLint("NewApi")
    private fun getChannel(channel: EFNotificationChannel) = when (channel) {
        EFNotificationChannel.EVENT -> NotificationChannel(channel.toString(), "Event Reminders", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Receive a reminder when an event you added to your favorites is about to happen."
        }
        EFNotificationChannel.ANNOUNCEMENT -> NotificationChannel(channel.toString(), "Announcements", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Receive a notification when EF sends convention wide announcements."
        }
        EFNotificationChannel.PRIVATE_MESSAGE -> NotificationChannel(channel.toString(), "Private Messages", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Receive a notification when you have logged in and received a private message."
        }
        else -> NotificationChannel(channel.toString(), "Default", NotificationManager.IMPORTANCE_LOW)
    }


    fun build(): Notification = builder.build()
}

enum class EFNotificationChannel {
    DEFAULT,
    EVENT,
    ANNOUNCEMENT,
    PRIVATE_MESSAGE
}