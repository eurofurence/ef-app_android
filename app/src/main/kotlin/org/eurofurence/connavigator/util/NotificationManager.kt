package org.eurofurence.connavigator.util

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import com.google.common.base.Preconditions
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.ActivityRoot

/**
 * Handles the creation of notications as a general entrypoint for more concurrent notifications
 */
class NotificationManager {
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
                .setSmallIcon(R.mipmap.ic_launcher)
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
}