package org.eurofurence.connavigator.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.eventFavouriter
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
        when (i) {
            0 -> {
                eventFavouriter(context).toNotifications(event)

                Snackbar.make(activity.findViewById(R.id.content), "Favourited event!", Snackbar.LENGTH_SHORT)
            }
            1 -> {
                logd { "Write to calendar pls" }
            }
            2 -> {
                //share
                val shareIntent = Intent();

                shareIntent.setAction(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_TEXT, Formatter.shareEvent(event))
                shareIntent.setType("text/plain")
                startActivity(shareIntent)
            }
        }
    }

}