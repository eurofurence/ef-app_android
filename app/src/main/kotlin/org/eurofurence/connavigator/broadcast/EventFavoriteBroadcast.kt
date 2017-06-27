package org.eurofurence.connavigator.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pawegio.kandroid.d
import com.pawegio.kandroid.i
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.gcm.NotificationPublisher
import org.eurofurence.connavigator.util.NotificationFactory
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.alarmManager
import org.joda.time.DurationFieldType

/**
 * Created by requinard on 4/17/17.
 */
class EventFavoriteBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val event: EventRecord = intent.jsonObjects["event"]
        val db = RootDb(context)

        i("Changing status of event ${event.title}")

        val notificationTime = db.eventStart(event).withFieldAdded(DurationFieldType.minutes(), -30)

        d("Notification time is $notificationTime")

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
            d("Event is already favorited. Removing from favorites")
            context.alarmManager.cancel(pendingIntent)
            db.faves = db.faves.filter { it != event.id }
        } else {
            d("Event is not yet favorited. Adding it to favorites")
            context.alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.millis, pendingIntent)
            db.faves += event.id
        }
    }
}