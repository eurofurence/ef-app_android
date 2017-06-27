package org.eurofurence.connavigator.broadcast

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.firebase.perf.metrics.AddTrace
import com.pawegio.kandroid.d
import com.pawegio.kandroid.i
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.gcm.NotificationPublisher
import org.eurofurence.connavigator.gcm.NotificationFactory
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.jetbrains.anko.alarmManager
import org.joda.time.DurationFieldType

/**
 * Created by requinard on 4/17/17.
 */
class EventFavoriteBroadcast : BroadcastReceiver() {
    @AddTrace(name="EventFavoriteBroadcast:onReceive",enabled = true)
    override fun onReceive(context: Context, intent: Intent) {
        val eventEntry: EventEntry = intent.jsonObjects["eventEntry"]
        val database = Database(context)

        i("Changing status of event ${eventEntry.title}")

        val notificationTime = database.eventStart(eventEntry).withFieldAdded(DurationFieldType.minutes(), -30)

        d("Notification time is $notificationTime")

        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notification = NotificationFactory(context)
                .buildNotification(
                        "${eventEntry.title} is happening soon!",
                        "Go to ${database.eventConferenceRoomDb[eventEntry.conferenceRoomId]!!.name}"
                )

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, eventEntry.id.hashCode())
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, eventEntry.id.hashCode(), notificationIntent, 0)

        if (database.favoritedDb.items.contains(eventEntry)) {
            // Remove item from favorites
            d("Event is already favorited. Removing from favorites")
            context.alarmManager.cancel(pendingIntent)
            database.favoritedDb.items = database.favoritedDb.items.filter { it != eventEntry }
        } else {
            d("Event is not yet favorited. Adding it to favorites")
            context.alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.millis, pendingIntent)
            database.favoritedDb.items += eventEntry
        }
    }
}