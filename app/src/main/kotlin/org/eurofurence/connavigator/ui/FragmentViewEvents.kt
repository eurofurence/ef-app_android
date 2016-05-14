package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
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
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.letRoot
import us.feras.mdv.MarkdownView

/**
 * Fragment displaying the events.
 */
class FragmentViewEvents : Fragment(), ContentAPI {

    // Event view holder finds and memorizes the views in an event card
    inner class EventViewHolder(itemView: View) : ViewHolder(itemView) {
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


    /**
     * The click listener repopulates the events by application of filters, then sends
     * a notification to the adapter of the list.
     */
    val listener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) =
                populateEvents()

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) =
                populateEvents()
    }

    val roomAdapter by lazy { ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item) }

    val dayAdapter  by lazy { ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item) }

    val database: Database get() = letRoot { it.database }!!

    // Views
    val events by view(RecyclerView::class.java)
    val roomSelector by view(Spinner::class.java)
    val daySelector by view(Spinner::class.java)
    val swipe by view(SwipeRefreshLayout::class.java)

    // Store of currently displayed events
    var effectiveEvents = emptyList<EventEntry>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Events")

        // Configure the recycler
        events.adapter = DataAdapter()
        events.layoutManager = LinearLayoutManager(context)
        events.itemAnimator = DefaultItemAnimator()

        // Add listener for update swipe
        swipe.setOnRefreshListener { UpdateIntentService.dispatchUpdate(context) }

        // Configure spinners
        roomSelector.adapter = roomAdapter
        roomSelector.onItemSelectedListener = listener

        daySelector.adapter = dayAdapter
        daySelector.onItemSelectedListener = listener

        // Update data first time
        dataUpdated()
    }

    /**
     * Called when the data is updated or the filters have changed.
     */
    override fun dataUpdated() {
        // Stop the swipe updater if running
        swipe.isRefreshing = false

        // Populate the spinners
        populateRoomSelector()
        populateDaySelector()

        // Populate the events, this is a more complicated procedure, since
        // filters are going to be applied
        populateEvents()
    }

    private fun populateRoomSelector() {
        // TODO: This behavior is not smart but works, will be replaced by a new pattern
        roomSelector.setSelection(0)

        roomAdapter.clear()
        roomAdapter.add("All")
        roomAdapter.addAll(database.eventConferenceRoomDb.items.map { it.name.split("—")[0] })
        roomAdapter.notifyDataSetChanged()
    }

    private fun populateDaySelector() {
        daySelector.setSelection(0)

        dayAdapter.clear()
        dayAdapter.add("All")
        dayAdapter.addAll(database.eventConferenceDayDb.items.map { it.date })
        dayAdapter.notifyDataSetChanged()
    }

    private fun populateEvents() {
        // Start filtering from entire dataset
        effectiveEvents = database.eventEntryDb.asc { it.startTime }

        // Get selected filters
        val roomSelected = roomSelector.selectedItem
        val daySelected = daySelector.selectedItem

        // If room filter selected, filter by room
        if (roomSelected != null && roomSelected != "All") {
            val room = database.eventConferenceRoomDb.items.filter { it.name.contains(roomSelected.toString()) }.first()
            effectiveEvents = effectiveEvents.filter { it.conferenceRoomId == room.id }
        }

        // If room filter selected, filter by day
        if (daySelected != null && daySelected != "All") {
            val day = database.eventConferenceDayDb.items.filter { it.date == daySelected }.first()
            effectiveEvents = effectiveEvents.filter { it.conferenceDayId == day.id }
        }

        events.adapter.notifyDataSetChanged()
    }
}