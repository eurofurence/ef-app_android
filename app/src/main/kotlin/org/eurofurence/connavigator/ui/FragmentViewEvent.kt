package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
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

    /**
     * Constructs the info view with an assigned bundle
     */
    constructor(eventEntry: EventEntry) : this() {
        arguments = Bundle()
        arguments.jsonObjects["eventEntry"] = eventEntry
    }

    val title by view(TextView::class.java)
    val description by view(MarkdownView::class.java)
    val image by view(ImageView::class.java)
    val organizers by view(TextView::class.java)
    val room by view(TextView::class.java)
    val time by view(TextView::class.java)

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_view_event, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.changeScreenName("View Event")

        if ("eventEntry" in arguments) {
            val eventEntry = arguments.jsonObjects["eventEntry", EventEntry::class.java]

            val conferenceRoom = database.eventConferenceRoomDb.keyValues[eventEntry.conferenceRoomId]
            val conferenceDay = database.eventConferenceDayDb.keyValues[eventEntry.conferenceDayId]

            title.text = eventEntry.title

            description.loadMarkdown(eventEntry.description)

            time.text = "%s %s-%s".format(conferenceDay?.date.toString(), eventEntry.startTime, eventEntry.endTime)
            organizers.text = "%s".format(eventEntry.panelHosts)
            room.text = "%s".format(conferenceRoom?.name)

            imageService.load(database.imageDb[eventEntry.imageId], image)
        }
    }

}