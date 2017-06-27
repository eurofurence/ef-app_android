package org.eurofurence.connavigator.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import com.pawegio.kandroid.IntentFor
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.eventEnd
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.SharingUtility
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.v2.get

/**
 * Created by David on 6/5/2016.
 */
class EventDialog(val event: EventRecord) : DialogFragment(), HasDb {
    override val db by lazyLocateDb()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle("Event Options for event ${event.title}")

        builder.setItems(R.array.event_options, DialogInterface.OnClickListener { dialogInterface, i -> update(dialogInterface, i) })

        return builder.create()
    }

    private fun update(dialogInterface: DialogInterface, i: Int) {
        logd { "Selected event option: $i" }

        when (i) {
            0 -> {
                logd { "Favouriting event for user" }
                context.sendBroadcast(IntentFor<EventFavoriteBroadcast>(context).apply { jsonObjects["event"] = event })

                Snackbar.make(activity.findViewById(R.id.content), "Favourited event!", Snackbar.LENGTH_SHORT)
            }
            1 -> {
                logd { "Writing event to calendar" }

                val calendarIntent = Intent(Intent.ACTION_INSERT)

                Analytics.event(Analytics.Category.EVENT, Analytics.Action.EXPORT_CALENDAR, event.title)

                calendarIntent.type = "vnd.android.cursor.item/event"
                calendarIntent.putExtra(CalendarContract.Events.TITLE, event.title)
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventStart(event).millis)
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventEnd(event).millis)
                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, event[toRoom]?.name)
                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.description)


                startActivity(calendarIntent)
            }
            2 -> {
                logd { "Sharing event" }

                Analytics.event(Analytics.Category.EVENT, Analytics.Action.SHARED, event.title)
                //share
                startActivity(Intent.createChooser(Intent(SharingUtility.share(Formatter.shareEvent(event))), "Share Event"))
            }
        }
    }

}