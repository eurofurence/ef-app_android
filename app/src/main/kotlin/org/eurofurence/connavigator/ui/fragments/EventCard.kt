package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.letRoot
import android.support.v7.widget.CardView

/**
 * Created by David on 5/4/2016.
 */
class EventCard(val eventEntry: EventEntry) : Fragment(), ContentAPI {
    val eventTitle: TextView by view()
    val eventDate: TextView by view()
    val eventImage: ImageView by view()

    val eventCard: CardView by view()

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_event_card, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventTitle.text = Formatter.eventTitle(eventEntry)
        eventDate.text = Formatter.eventToTimes(eventEntry, database, true)
        imageService.load(database.imageDb[eventEntry.imageId], eventImage)


        eventCard.setOnClickListener {
            applyOnRoot { navigateToEvent(eventEntry) }
        }

        if (database.favoritedDb.items.contains(eventEntry))
            eventCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primaryLighter))
    }
}