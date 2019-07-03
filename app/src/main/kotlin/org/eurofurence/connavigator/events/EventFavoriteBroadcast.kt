package org.eurofurence.connavigator.events

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.perf.metrics.AddTrace
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.*
import org.eurofurence.connavigator.notifications.EFNotificationChannel
import org.eurofurence.connavigator.notifications.NotificationFactory
import org.eurofurence.connavigator.notifications.NotificationPublisher
import org.eurofurence.connavigator.preferences.AppPreferences
import org.eurofurence.connavigator.preferences.DebugPreferences
import org.eurofurence.connavigator.services.dispatchUpdate
import org.eurofurence.connavigator.ui.activities.NavActivity
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.ui.fragments.HomeFragmentDirections
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.now
import org.eurofurence.connavigator.util.extensions.startTimeString
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alarmManager
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import org.joda.time.DateTime
import org.joda.time.DurationFieldType
import java.util.*

/**
 * Created by requinard on 4/17/17.
 */
class EventFavoriteBroadcast : BroadcastReceiver(), AnkoLogger {
    @AddTrace(name = "EventFavoriteBroadcast:onReceive", enabled = true)
    override fun onReceive(context: Context, intent: Intent) {
        createNewNotification(intent, context)
    }

    private fun createNewNotification(intent: Intent, context: Context) {
        val event: EventRecord = intent.jsonObjects["event"]
        val db = RootDb(context)

        info("Changing status of event ${event.title}")

        val notificationTime = getNotificationTime(context, db, event)

        info("Notification time is $notificationTime")

        val notificationIntent = createNotification(context, event, notificationTime)

        val pendingIntent = PendingIntent.getBroadcast(context, event.id.hashCode(), notificationIntent, 0)

        when {
            event.id in db.faves -> {
                // Remove item from favorites
                info("Event is already favorited. Removing from favorites")
                context.longToast("Removed ${event.title} from favorites")
                context.alarmManager.cancel(pendingIntent)
                db.faves = db.faves.filter { it != event.id }
            }
            notificationTime < now() -> context.longToast("This event has already occurred!")
            else -> {
                info("Event is not yet favorited. Adding it to favorites")
                context.longToast("Added ${event.title} to favorites")
                context.alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.millis, pendingIntent)
                db.faves += event.id
            }
        }

        db.observer.onNext(db)

        dispatchUpdate(context)
    }

    private fun getNotificationTime(context: Context, db: Db, event: EventRecord): DateTime {
        return if (DebugPreferences.scheduleNotificationsForTest) {
            context.longToast("Notification should show up in 30 seconds")
            DateTime.now().withFieldAdded(DurationFieldType.seconds(), 30)
        } else {
            event.start.withFieldAdded(DurationFieldType.minutes(), -(AppPreferences.notificationMinutesBefore))
        }
    }

    private fun createNotification(context: Context, event: EventRecord, time: DateTime): Intent {
        val notificationIntent = Intent(context, NotificationPublisher::class.java)
        val notificationFactory = NotificationFactory(context)

        val action = HomeFragmentDirections
                .actionFragmentViewHomeToFragmentViewEvent(event.id.toString())

        val pendingIntent = NavDeepLinkBuilder(context)
                .setComponentName(NavActivity::class.java)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.navEventItem)
                .setArguments(action.arguments)
                .createPendingIntent()

        val notification = notificationFactory.createBasicNotification()
                .addRegularText(
                        "Upcoming event: ${event.title}",
                        "Starting at ${event.startTimeString()}")
                .setPendingIntent(pendingIntent)
                .setChannel(EFNotificationChannel.EVENT)
                .countdownTo(time)

        notificationIntent.putExtra(NotificationPublisher.TAG, event.id.toString())
        notificationIntent.putExtra(NotificationPublisher.ITEM, notification.build())

        return notificationIntent
    }

    private fun updateNotification(context: Context, eventId: UUID) {
        info { "Updating notification for $eventId" }
        val db = context.locateDb()
        val event = db.events[eventId] ?: return
        info { "Event found: ${event.title}" }
        val notificationTime = getNotificationTime(context, db, event)

        if (notificationTime < now()) {
            info { "Not rescheduling notification as it was in the past" }
            return
        }

        val notification = createNotification(context, event, notificationTime)
        val pendingIntent = PendingIntent.getBroadcast(context, event.id.hashCode(), notification, PendingIntent.FLAG_UPDATE_CURRENT)

        context.alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime.millis, pendingIntent)
        info { "Updated pending activity" }
    }

    fun updateNotifications(context: Context, eventIds: List<UUID>) = eventIds.map { updateNotification(context, it) }
}