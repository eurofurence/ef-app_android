package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI
import org.eurofurence.connavigator.util.delegators.viewInFragment
import org.eurofurence.connavigator.util.delegators.viewInHolder
import org.eurofurence.connavigator.util.extensions.applyRoot
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.letRoot
import java.util.*

/**
 * Fragment displaying the events.
 */
class FragmentViewEvents : Fragment(), ContentAPI {

    // Event view holder finds and memorizes the views in an event card
    inner class EventViewHolder(itemView: View) : ViewHolder(itemView) {
        val eventImage by viewInHolder(ImageView::class.java)
        val eventTitle by viewInHolder(TextView::class.java)
        val eventDate by viewInHolder(TextView::class.java)
        val eventHosts by viewInHolder(TextView::class.java)
        val eventDescription by viewInHolder(TextView::class.java)
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
            holder.eventDescription.text = event.description

            // Load image
            imageService.load(database.imageDb[event.imageId], holder.eventImage)

            // Assign the on-click action
            holder.itemView.setOnClickListener {
                applyRoot { navigateToEvent(event) }
            }
        }
    }

    val database: Database get() = letRoot { it.database }

    // Views
    val events by viewInFragment(RecyclerView::class.java)
    val roomSelector by viewInFragment(Spinner::class.java)
    val daySelector by viewInFragment(Spinner::class.java)

    // Store of currently displayed events
    var effectiveEvents = emptyList<EventEntry>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_view_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Initialize the events
        dataInit()

        // Configure the recycler
        events.layoutManager = LinearLayoutManager(context)
        events.itemAnimator = DefaultItemAnimator()
        events.adapter = DataAdapter()

        // Create a click listener
        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) =
                    dataUpdated()

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                    dataUpdated()
        }

        // Set up the rooms in the spinner
        val rooms = ArrayList<String>()
        rooms.add("All")
        rooms.addAll(database.eventConferenceRoomDb.items.map { it.name }.toTypedArray())
        val roomAdapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, rooms)

        roomSelector.adapter = roomAdapter
        roomSelector.onItemSelectedListener = listener

        // Set up con days in the spinner
        val days = ArrayList<String>()
        days.add("All")
        days.addAll(database.eventConferenceDayDb.items.map { it.date }.toTypedArray())
        val dayAdapter = ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, days)

        daySelector.adapter = dayAdapter
        daySelector.onItemSelectedListener = listener
    }

    /**
     * Called when the data is updated or the filters have changed.
     */
    override fun dataUpdated() {
        dataInit()

        // Get selected filters
        val roomSelected = roomSelector.selectedItem
        val daySelected = daySelector.selectedItem

        // If room filter selected, filter by room
        if (roomSelected != "All") {
            val room = database.eventConferenceRoomDb.items.filter { it.name == roomSelected }.first()
            effectiveEvents = effectiveEvents.filter { it.conferenceRoomId == room.id }
        }

        // If room filter selected, filter by day
        if (daySelected != "All") {
            val day = database.eventConferenceDayDb.items.filter { it.date == daySelected }.first()
            effectiveEvents = effectiveEvents.filter { it.conferenceDayId == day.id }
        }

        // Notification to the adapter
        events.adapter.notifyDataSetChanged()
    }

    private fun dataInit() {
        // Start filtering from entire dataset
        effectiveEvents = database.eventEntryDb.items
    }

}