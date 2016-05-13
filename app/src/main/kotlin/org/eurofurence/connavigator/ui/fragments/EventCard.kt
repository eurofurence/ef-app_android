package org.eurofurence.connavigator.ui.fragments

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
import org.eurofurence.connavigator.ui.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.get
import java.util.regex.Pattern

/**
 * Created by David on 5/4/2016.
 */
class EventCard(val eventEntry: EventEntry) : Fragment() {
    val eventTitle by view(TextView::class.java)
    val eventDate by view(TextView::class.java)
    val eventImage by view(ImageView::class.java)
    val timePattern = Pattern.compile("(\\d+:\\d+):(\\d+)")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_event_card, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = Database(activity)

        eventTitle.text = eventEntry.title
        eventDate.text = eventEntry.startTime
        imageService.load(database.imageDb[eventEntry.imageId], eventImage)
    }
}