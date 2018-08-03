package org.eurofurence.connavigator.gcm

import android.app.Activity
import android.app.Notification
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

/**
 * Creates a basic notificaiton
 */
class NotificationFactory(var context: Context) {
    @Deprecated("This function should no longer be used")
    fun buildNotification(title: String, message: String, pendingActivity: Class<*> = ActivityRoot::class.java): Notification {
        if (!Activity::class.java.isAssignableFrom(pendingActivity))
            error("Pending activity is not actually an activity")

        val builder = NotificationCompat.Builder(context)

        // On a click event we want to start an activity
        val intentToExecute = Intent(context, pendingActivity)

        // Attach the intent to a pending intent that our app can consume
        val pendingIntent = PendingIntent.getActivity(context, 0, intentToExecute, 0)

        // Do a lot of building
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_negative)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setLights(Color.argb(255, 0, 100, 89), 1000, 1000)
                .setVibrate(longArrayOf(250, 100, 250, 100))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

        return builder.build()
    }


    @Deprecated("Deprecated in favor of composable builders and it's broadcaster")
    fun showNotification(title: String, content: String) {
        val notification = buildNotification(title, content)

        val intent = Intent(context, NotificationPublisher::class.java)

        intent.putExtra(NotificationPublisher.NOTIFICATION_ID, notification.hashCode())
        intent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        context.sendBroadcast(intent)
    }

    fun broadcastNotification(builder: NotificationCompat.Builder) {
        val notification = builder.build()

        val intent = context.intentFor<NotificationPublisher>(
                NotificationPublisher.NOTIFICATION_ID to notification.hashCode(),
                NotificationPublisher.NOTIFICATION to notification
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
     * Sets an activity to launch on notication taps
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

    fun addBigText(builder: NotificationCompat.Builder, bigText: String?) = builder.setStyle(
            NotificationCompat.BigTextStyle()
                    .bigText(bigText)
    )

    fun addRegularText(builder: NotificationCompat.Builder, title: String, text:String) = builder.apply {
        this.mContentTitle = title
        this.mContentText = text
    }
}