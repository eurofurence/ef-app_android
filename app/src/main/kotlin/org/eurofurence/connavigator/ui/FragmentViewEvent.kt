package org.eurofurence.connavigator.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.pawegio.kandroid.IntentFor
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.DataChanged
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.dialogs.EventDialog
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.get
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Created by David on 4/9/2016.
 */
class FragmentViewEvent() : Fragment(), HasDb {
    companion object {
        val EVENT_STATUS_CHANGED = "org.eurofurence.connavigator.ui.EVENT_STATUS_CHANGED"
    }

    override val db by lazyLocateDb()
    val dataChanged by lazy {
        context.localReceiver(DataChanged.DATACHANGED) {
            changeFabIcon()
        }
    }

    lateinit var eventId: UUID

    /**
     * Constructs the info view with an assigned bundle
     */
    constructor(event: EventRecord) : this() {
        arguments = Bundle()
        arguments.jsonObjects["event"] = event
    }

    val title: TextView by view()
    val description: MarkdownView by view()
    val image: ImageView by view()
    val organizers: TextView by view()
    val room: TextView by view()
    val time: TextView by view()
    val buttonSave: FloatingActionButton by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_event, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.screen(activity, "Event Specific")

        if ("event" in arguments) {
            val event: EventRecord = arguments.jsonObjects["event"]

            eventId = event.id

            dataChanged.register()

            Analytics.event(Analytics.Category.EVENT, Analytics.Action.OPENED, event.title)

            val conferenceRoom = event[toRoom]
            val conferenceDay = event[toDay]

            title.text = event.fullTitle()

            description.loadMarkdown(event.description)

            time.text = Formatter.eventToTimes(event, db, AppPreferences.shortenDates)
            organizers.text = Formatter.eventOwner(event)
            room.text = Formatter.roomFull(conferenceRoom!!)

            // temporary fix until we get the actual images
            imageService.load(db.images[event.posterImageId], image)

            changeFabIcon()

            buttonSave.setOnClickListener {
                EventDialog(event).show(activity.supportFragmentManager, "Event Dialog").let { true }
            }

            buttonSave.setOnLongClickListener {
                context.sendBroadcast(IntentFor<EventFavoriteBroadcast>(context).apply { jsonObjects["event"] = event })
                true
            }
        }
    }

    /**
     * Changes the FAB based on if the current event is liked or not
     */
    private fun changeFabIcon() {
        if (eventId in db.faves)
            buttonSave.setImageDrawable(IconDrawable(context, FontAwesomeIcons.fa_heart))
        else
            buttonSave.setImageDrawable(IconDrawable(context, FontAwesomeIcons.fa_heart_o))
    }
}