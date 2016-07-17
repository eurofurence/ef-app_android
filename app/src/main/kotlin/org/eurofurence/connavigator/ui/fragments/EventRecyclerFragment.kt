package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.FragmentViewEvent
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.dialogs.EventDialog
import org.eurofurence.connavigator.ui.filters.AnyEventFilter
import org.eurofurence.connavigator.ui.filters.IEventFilter
import org.eurofurence.connavigator.ui.layouts.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.letRoot
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.joda.time.DateTime

/**
 * Event view recycler to hold the viewpager items
 */
class EventRecyclerFragment(val filterStrategy: IEventFilter, val filterVal: Any = Unit) : Fragment(), ContentAPI {

    constructor() : this(AnyEventFilter(), Unit) {
    }

    // Event view holder finds and memorizes the views in an event card
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage by view(ImageView::class.java)
        val eventTitle by view(TextView::class.java)
        val eventStartTime by view(TextView::class.java)
        val eventEndTime by view(TextView::class.java)
        val eventRoom by view(TextView::class.java)
        val eventCard by view(LinearLayout::class.java)
    }

    inner class DataAdapter : RecyclerView.Adapter<EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int) =
                EventViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.fragment_event_card, parent, false))

        override fun getItemCount() =
                effectiveEvents.size

        override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {
            // Get the event for the position
            val event = effectiveEvents[pos]

            // Assign the properties of the view
            holder.eventTitle.text = Formatter.eventTitle(event)

            if (database.eventIsHappening(event, DateTime.now())) {
                holder.eventStartTime.text = "NOW"
            } else if (database.eventStart(event).isBeforeNow) {
                holder.eventStartTime.text = "DONE"
            } else {
                holder.eventStartTime.text = Formatter.shortTime(event.startTime)
            }

            holder.eventEndTime.text = "until ${Formatter.shortTime(event.endTime)}"
            holder.eventRoom.text = Formatter.roomFull(database.eventConferenceRoomDb[event.conferenceRoomId]!!)

            // Load image
            imageService.load(database.imageDb[event.imageId], holder.eventImage)

            holder.itemView.isClickable = true

            // Assign the on-click action
            holder.itemView.setOnClickListener {
                applyOnRoot { navigateToEvent(event) }
            }
            holder.itemView.setOnLongClickListener {
                EventDialog(event).show(activity.supportFragmentManager, "Kek").let { true }
            }

            // Colour the event cards according to if they've already occured, are ocurring or are favourited
            if (database.eventIsHappening(event, DateTime.now())) {
                // Event is happening, so we colour it light accent
                holder.eventCard.setBackgroundColor(getColor(context, R.color.accentLighter))
            } else if (database.favoritedDb[event.id] != null) {
                // Event is in favourites, thus it is coloured in primary
                holder.eventCard.setBackgroundColor(getColor(context, R.color.primaryLighter))
            } else if (database.eventEnd(event).isBeforeNow) {
                // Event end is before the current time, so it has already occurred thus it is gray
                holder.eventCard.setBackgroundColor(getColor(context, R.color.backgroundGrey))
            } else {
                holder.eventCard.setBackgroundColor(getColor(context, R.color.cardview_light_background))
            }
        }
    }

    val events by view(RecyclerView::class.java)

    var effectiveEvents = emptyList<EventEntry>()

    val eventsTitle by view(TextView::class.java)

    val database: Database get() = letRoot { it.database }!!

    lateinit var updateReceiver: EmbeddedLocalBroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (filterStrategy.getTitle() == "")
            eventsTitle.visibility = View.GONE
        else
            eventsTitle.text = filterStrategy.getTitle()

        effectiveEvents = filterStrategy.filter(database, filterVal).toList()

        // Configure the recycler
        events.setHasFixedSize(true)
        events.adapter = DataAdapter()

        if (filterStrategy.scrolling)
            events.layoutManager = LinearLayoutManager(activity)
        else
            events.layoutManager = NonScrollingLinearLayout(activity)

        events.itemAnimator = DefaultItemAnimator()

        updateReceiver = context.localReceiver(FragmentViewEvent.EVENT_STATUS_CHANGED) {
            events.adapter.notifyDataSetChanged()
        }

        updateReceiver.register()

        if (effectiveEvents.isEmpty()) {
            eventsTitle.visibility = View.GONE
            events.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        updateReceiver.unregister()
    }

    override fun onResume() {
        super.onResume()
        updateReceiver.register()
    }

    override fun dataUpdated() {
        effectiveEvents = filterStrategy.filter(database, filterVal).toList()
        events.adapter.notifyDataSetChanged()
    }
}