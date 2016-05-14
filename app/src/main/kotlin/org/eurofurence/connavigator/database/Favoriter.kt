package org.eurofurence.connavigator.database

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.analytics.HitBuilders
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.gcm.NotificationPublisher
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * Created by David on 5/14/2016.
 */
object Favoriter {
    /**
     * Handles logic for favoriting events
     * Return: True if element was inserted, false is element was removed
     */
    fun event(database: Database, eventEntry: EventEntry): Boolean {
        logv { "Favoriting event" }

        Analytics.trackEvent(
                HitBuilders.EventBuilder()
                        .setAction("Favorited event %s; uid %s".format(eventEntry.title, eventEntry.id))
                        .setLabel("Event")
                        .setCategory("Favorited")
        )

        if (database.favoritedDb.items.contains(eventEntry)) {
            removeEventNotification(eventEntry, database.context)
            logv { "Removing event %s".format(eventEntry.title) }
            database.favoritedDb.items = database.favoritedDb.items.filter { it.id != eventEntry.id }
            return false
        } else {
            scheduleEventNotification(eventEntry, database.context)

            logv { "Entering event %s".format(eventEntry.title) }
            val newFavourited = LinkedList<EventEntry>()
            newFavourited.addAll(database.favoritedDb.items)
            newFavourited.add(eventEntry)

            database.favoritedDb.items = newFavourited
            return true
        }
    }

    /**
     * Removes an event notification.
     * Perfectly tries to recreate a pending intent, and then removes it
     */
    fun removeEventNotification(eventEntry: EventEntry, context: Context) {
        logv { "Removing notification for event" }
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notification = buildEventNotification(eventEntry, context)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, eventEntry.id)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Schedules an event to notify
     */
    fun scheduleEventNotification(eventEntry: EventEntry, context: Context) {
        logv { "Scheduling notification for event" }
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notification = buildEventNotification(eventEntry, context)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, eventEntry.id)
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val eventTime = eventTimeInMillis(eventEntry, context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, eventTime, pendingIntent)
    }

    /**
     * Calculates the event time in millis so that we can notify the user
     */
    fun eventTimeInMillis(eventEntry: EventEntry, context: Context): Long {
        logv { "Calculating event time in millis" }
        val database = Database(context)

        val event_date = database.eventConferenceDayDb[eventEntry.conferenceDayId]
        val event_start_time = eventEntry.startTime

        //format will be "yyyy-MM-dd hh:mm:ss"
        val eventDateTimeString = "%s %s".format(event_date!!.date, event_start_time)
        val eventDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

        val eventDateTime = eventDateTimeFormatter.parseDateTime(eventDateTimeString)

        eventDateTime.minusMinutes(30)

        logd { "Event time in millis is %s".format(eventDateTime.millis.toString()) }

        return eventDateTime.millis
    }

    /**
     * Builds an event notification
     */
    fun buildEventNotification(eventEntry: EventEntry, context: Context): Notification {
        logd { "Building notification for event %s".format(eventEntry.id) }
        val database = Database(context)
        val builder = Notification.Builder(database.context)

        builder.setContentTitle("Upcoming eurofurence event!")
                .setAutoCancel(true)
                .setContentText("%s is happening soon! Go to %s".format(eventEntry.title, database.eventConferenceRoomDb[eventEntry.conferenceRoomId]!!.name))
                .setSmallIcon(R.mipmap.ic_launcher)

        return builder.build()
    }
}