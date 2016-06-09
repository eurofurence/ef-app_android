package org.eurofurence.connavigator.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.design.widget.Snackbar
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.util.EventFavouriter
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.SharingUtility
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.logd

/**
 * Created by David on 6/5/2016.
 */
class EventDialog(val event: EventEntry) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(event.title)

        builder.setItems(R.array.event_options, DialogInterface.OnClickListener { dialogInterface, i -> update(dialogInterface, i) })

        return builder.create()
    }

    private fun update(dialogInterface: DialogInterface, i: Int) {
        logd { "Selected event option: $i" }
        val database = Database(context)
        when (i) {
            0 -> {
                logd { "Favouriting event for user" }
                EventFavouriter(context).toNotifications(event)

                Snackbar.make(activity.findViewById(R.id.content), "Favourited event!", Snackbar.LENGTH_SHORT)
            }
            1 -> {
                logd { "Writing event to calendar" }

                val calendarIntent = Intent(Intent.ACTION_INSERT)

                calendarIntent.setType("vnd.android.cursor.item/event");
                calendarIntent.putExtra(CalendarContract.Events.TITLE, event.title);
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, database.eventStart(event).millis)
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, database.eventEnd(event).millis)
                calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, database.eventConferenceRoomDb[event.conferenceRoomId]!!.name);
                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, event.description);


                startActivity(calendarIntent)
            }
            2 -> {
                logd { "Sharing event" }
                //share
                startActivity(SharingUtility.share(Formatter.shareEvent(event)))
            }
        }
    }

}