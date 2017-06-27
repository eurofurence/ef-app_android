package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot

/**
 * Created by David on 5/4/2016.
 */
class EventCard(val eventEntry: EventRecord) : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    val eventTitle: TextView by view()
    val eventDate: TextView by view()
    val eventImage: ImageView by view()

    val eventCard: CardView by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_event_card, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventTitle.text = Formatter.eventTitle(eventEntry)
        eventDate.text = Formatter.eventToTimes(eventEntry, db, true)
        /* TODO
        imageService.load(database.imageDb[eventEntry.imageId], eventImage)
        */

        eventCard.setOnClickListener {
            applyOnRoot { navigateToEvent(eventEntry) }
        }

        if (eventEntry.id in faves)
            eventCard.setCardBackgroundColor(ContextCompat.getColor(context, R.color.primaryLighter))
    }
}