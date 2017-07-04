package org.eurofurence.connavigator.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.perf.metrics.AddTrace
import com.pawegio.kandroid.d
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.gcm.NotificationFactory
import org.eurofurence.connavigator.gcm.NotificationPublisher
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alarmManager
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import org.joda.time.DurationFieldType

/**
 * Created by requinard on 4/17/17.
 */
class EventFavoriteBroadcast : BroadcastReceiver(), AnkoLogger {
    @AddTrace(name = "EventFavoriteBroadcast:onReceive", enabled = true)
    override fun onReceive(context: Context, intent: Intent) {
        val event: EventRecord = intent.jsonObjects["event"]
        val db = RootDb(context)

        info("Changing status of event ${event.title}")

        val notificationTime = db.eventStart(event).withFieldAdded(DurationFieldType.minutes(), -(AppPreferences.notificationMinutesBefore))

        info("Notification time is $notificationTime")

        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notification = NotificationFactory(context)
                .buildNotification(
                        "${event.title} is happening soon!",
                        "Go to ${event[db.toRoom]?.name}")

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, event.id.hashCode())
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, event.id.hashCode(), notificationIntent, 0)

        if (event.id in db.faves) {
            // Remove item from favorites
            info("Event is already favorited. Removing from favorites")
            context.longToast("Removed ${event.title} from favorites")
            context.alarmManager.cancel(pendingIntent)
            db.faves = db.faves.filter { it != event.id }
        } else {
            info("Event is not yet favorited. Adding it to favorites")
            context.longToast("Added ${event.title} to favorites")
            context.alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.millis, pendingIntent)
            db.faves += event.id
        }

        DataChanged.fire(context, "Favorites")
    }
}