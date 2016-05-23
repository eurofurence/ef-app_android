package org.eurofurence.connavigator.util

import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.preference.PreferenceManager
import com.google.common.base.Preconditions
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.gcm.NotificationPublisher
import org.eurofurence.connavigator.ui.ActivityRoot
import org.joda.time.DateTime

/**
 * Handles the creation of notications as a general entrypoint for more concurrent notifications
 */
class NotificationFactory {
    lateinit var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun buildNotification(title: String, message: String, pendingActivity: Class<*> = ActivityRoot::class.java): Notification {
        Preconditions.checkArgument(Activity::class.java.isAssignableFrom(pendingActivity), "Pending activity is not actually an activity")

        val builder = Notification.Builder(context)

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
                .setPriority(Notification.PRIORITY_HIGH)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setCategory(Notification.CATEGORY_EVENT)
                .setContentIntent(pendingIntent)

        return builder.build()
    }

    /**
     * Creates a pending notification that can be executed in the future
     */
    fun createPendingNotification(notification: Notification, wakeUpTime: Long, notificationId: Int) {
        // Create intent to wake up to that is handled by notificationPublisher
        val pendingIntent = createPendingIntent(notification, notificationId)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.resources.getString(R.string.debug_notifications_schedule), false)) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, DateTime.now().plusSeconds(5).millis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
        }
    }

    /**
     * Cancels a pending notification
     */
    fun cancelPendingNotification(notification: Notification, notificationId: Int) {
        // Create intent to wake up to that is handled by notificationPublisher
        val pendingIntent = createPendingIntent(notification, notificationId)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }

    private fun createPendingIntent(notification: Notification, notificationId: Int): PendingIntent? {
        val notificationIntent = Intent(context, NotificationPublisher::class.java)

        // Give the notification an assignable ID
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 0)

        // Give it the actual notification
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        // Create notification that will be handled
        return PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    }
}