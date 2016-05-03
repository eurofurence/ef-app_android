package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.EventConferenceDay
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.get
import us.feras.mdv.MarkdownView

/**
 * Event view recycler to hold the viewpager items
 */
class EventView(val page: Int, val eventDay: EventConferenceDay) : Fragment() {

    // Event view holder finds and memorizes the views in an event card
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage by view(ImageView::class.java)
        val eventTitle by view(TextView::class.java)
        val eventDate by view(TextView::class.java)
        val eventHosts by view(TextView::class.java)
        val eventDescription by view(MarkdownView::class.java)
    }

    inner class DataAdapter : RecyclerView.Adapter<EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int) =
                EventViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.fragment_main_event, parent, false))

        override fun getItemCount() =
                effectiveEvents.size

        override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {
            // Get the event for the position
            val event = effectiveEvents[pos]

            // Assign the properties of the view
            holder.eventTitle.text = event.title
            holder.eventDate.text = event.startTime
            holder.eventHosts.text = event.panelHosts
            if (event.description.length > 200)
                holder.eventDescription.loadMarkdown(event.description.substring(0, 200) + " . . .")
            else
                holder.eventDescription.loadMarkdown(event.description)

            // Load image
            imageService.load(database.imageDb[event.imageId], holder.eventImage)

            // Assign the on-click action
            holder.itemView.setOnClickListener {
                applyOnRoot { navigateToEvent(event) }
            }
        }
    }

    val events by view(RecyclerView::class.java)

    var effectiveEvents = emptyList<EventEntry>()
    lateinit var database: Database

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_fragment_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Database(activity)

        effectiveEvents = database.eventEntryDb.items.filter { it.conferenceDayId == eventDay.id }
        // Configure the recycler
        events.adapter = DataAdapter()
        events.layoutManager = LinearLayoutManager(activity)
        events.itemAnimator = DefaultItemAnimator()
    }
}