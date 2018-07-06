package org.eurofurence.connavigator.ui.dialogs

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.support.v4.content.ContextCompat.startActivity
import com.pawegio.kandroid.IntentFor
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.eventEnd
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.logd
import org.jetbrains.anko.selector
import org.jetbrains.anko.share
import org.jetbrains.anko.toast

/**
 * Shows an event dialog
 */
fun eventDialog(context: Context, event: EventRecord, db: Db) {
    val options = listOf(
            if (!db.faves.contains(event.id))
                "Add event to favorites"
            else
                "Remove event from favorites",
            "Export to calendar",
            "Share"
    )

    return context.selector("Event Options for event ${event.title}", options) { _, position ->
        when (position) {
            0 -> {
                logd { "Favouriting event for user" }
                context.sendBroadcast(IntentFor<EventFavoriteBroadcast>(context).apply { jsonObjects["event"] = event })

                context.toast("Changed event status!")
            }
            1 -> {
                logd { "Writing event to calendar" }

                val calendarIntent = Intent(Intent.ACTION_INSERT)

                Analytics.event(Analytics.Category.EVENT, Analytics.Action.EXPORT_CALENDAR, event.title)

                calendarIntent.type = "vnd.android.cursor.item/event"
                calendarIntent.putExtra(CalendarContract.Events.TITLE, event.title)
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, db.eventStart(event).millis)
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, db.eventEnd(event).millis)
                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, db.toRoom(event)?.name)
                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.description)


                startActivity(context, calendarIntent, null)
            }
            2 -> {
                logd { "Sharing event" }

                Analytics.event(Analytics.Category.EVENT, Analytics.Action.SHARED, event.title)
                //share
                context.share(Formatter.shareEvent(event), "Share event")
            }
        }
    }
}