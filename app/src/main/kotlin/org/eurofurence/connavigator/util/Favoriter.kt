package org.eurofurence.connavigator.util

import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.google.android.gms.analytics.HitBuilders
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.gcm.NotificationPublisher
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.logv
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * Created by David on 5/14/2016.
 */
class Favoriter {
    lateinit var context: Context
    lateinit var database: Database

    constructor(context: Context) {
        this.context = context
        this.database = Database(context)
    }

    /**
     * Handles logic for favoriting events
     * Return: True if element was inserted, false is element was removed
     */
    fun event(eventEntry: EventEntry): Boolean {
        logv { "Favoriting event" }

        Analytics.trackEvent(
                HitBuilders.EventBuilder()
                        .setAction("Favorited event %s; uid %s".format(eventEntry.title, eventEntry.id))
                        .setLabel("Event")
                        .setCategory("Favorited")
        )

        if (database.favoritedDb.items.contains(eventEntry)) {
            removeEventNotification(eventEntry)
            logv { "Removing event %s".format(eventEntry.title) }
            database.favoritedDb.items = database.favoritedDb.items.filter { it.id != eventEntry.id }
            return false
        } else {
            scheduleEventNotification(eventEntry)

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
    fun removeEventNotification(eventEntry: EventEntry) {
        logv { "Removing notification for event" }
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notification = buildEventNotification(eventEntry)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, eventEntry.id.hashCode())
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, eventEntry.id.hashCode(), notificationIntent, 0)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /**
     * Schedules an event to notify
     */
    fun scheduleEventNotification(eventEntry: EventEntry) {
        logv { "Scheduling notification for event" }
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notification = buildEventNotification(eventEntry)

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, eventEntry.id.hashCode())
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification)

        val pendingIntent = PendingIntent.getBroadcast(context, eventEntry.id.hashCode(), notificationIntent, 0)

        var eventTime = eventTimeInMillis(eventEntry, context)

        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.resources.getString(R.string.debug_notifications_schedule), false)) {
            eventTime = DateTime.now().plusSeconds(5).millis
        }
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, eventTime, pendingIntent)
    }

    /**
     * Calculates the event time in millis so that we can notify the user
     */
    fun eventTimeInMillis(eventEntry: EventEntry, context: Context): Long {
        val database = Database(context)

        val event_date = database.eventConferenceDayDb[eventEntry.conferenceDayId]
        val event_start_time = eventEntry.startTime

        //format will be "yyyy-MM-dd hh:mm:ss"
        val eventDateTimeString = "%s %s".format(event_date!!.date, event_start_time)
        val eventDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

        var eventDateTime = eventDateTimeFormatter.parseDateTime(eventDateTimeString)

        eventDateTime = eventDateTime.minusMinutes(30)

        return eventDateTime.millis
    }

    /**
     * Builds an event notification
     */
    fun buildEventNotification(eventEntry: EventEntry): Notification {
        return NotificationFactory(context).buildNotification(eventEntry.title, database.eventConferenceRoomDb[eventEntry.conferenceRoomId]!!.name)
    }
}