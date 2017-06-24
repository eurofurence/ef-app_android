package org.eurofurence.connavigator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pawegio.kandroid.IntentFor
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.dialogs.EventDialog
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.letRoot
import us.feras.mdv.MarkdownView

/**
 * Created by David on 4/9/2016.
 */
class FragmentViewEvent() : Fragment() {
    companion object {
        val EVENT_STATUS_CHANGED = "org.eurofurence.connavigator.ui.EVENT_STATUS_CHANGED"
    }

    /**
     * Constructs the info view with an assigned bundle
     */
    constructor(eventEntry: EventEntry) : this() {
        arguments = Bundle()
        arguments.jsonObjects["eventEntry"] = eventEntry
    }

    val title: TextView by view()
    val description: MarkdownView by view()
    val image: ImageView by view()
    val organizers: TextView by view()
    val room: TextView by view()
    val time: TextView by view()
    val buttonSave: FloatingActionButton by view()

    val preferences: SharedPreferences get() = letRoot { it.preferences }!!

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_event, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.screen(activity, "Event Specific")


        if ("eventEntry" in arguments) {
            val eventEntry: EventEntry = arguments.jsonObjects["eventEntry"]

            Analytics.event(Analytics.Category.EVENT, Analytics.Action.OPENED, eventEntry.title)

            val conferenceRoom = database.eventConferenceRoomDb.keyValues[eventEntry.conferenceRoomId]
            val conferenceDay = database.eventConferenceDayDb.keyValues[eventEntry.conferenceDayId]

            title.text = Formatter.eventTitle(eventEntry)

            description.loadMarkdown(eventEntry.description)

            time.text = Formatter.eventToTimes(eventEntry, database, preferences.getBoolean(context.getString(R.string.date_short), true))
            organizers.text = Formatter.eventOwner(eventEntry)
            room.text = Formatter.roomFull(conferenceRoom!!)

            imageService.load(database.imageDb[eventEntry.imageId], image)

            changeFabIcon(eventEntry)

            buttonSave.setOnClickListener {
                EventDialog(eventEntry).show(activity.supportFragmentManager, "Event Dialog").let { true }
            }

            buttonSave.setOnLongClickListener {
                context.sendBroadcast(IntentFor<EventFavoriteBroadcast>(context).apply { jsonObjects["eventEntry"] = eventEntry })

                changeFabIcon(eventEntry)

                true
            }
        }
    }

    /**
     * Changes the FAB based on if the current event is liked or not
     */
    private fun changeFabIcon(eventEntry: EventEntry?) {
        if (database.favoritedDb.items.contains(eventEntry))
            buttonSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_like_filled))
        else
            buttonSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_menu))
    }

}