package org.eurofurence.connavigator.ui.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColor
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.FragmentViewEvent
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.dialogs.EventDialog
import org.eurofurence.connavigator.ui.filters.AnyEventFilter
import org.eurofurence.connavigator.ui.filters.AnyFavoritedEventFilter
import org.eurofurence.connavigator.ui.filters.IEventFilter
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.RemoteConfig
import org.eurofurence.connavigator.util.TouchVibrator
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.joda.time.DateTime

/**
 * Event view recycler to hold the viewpager items
 */
class EventRecyclerFragment(val filterStrategy: IEventFilter, var filterVal: Any = Unit) : Fragment(), ContentAPI {

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

            val conflicting = if (remoteConfig.showConflictingEvents) database.eventIsConflicting(event) else false

            val favourite = database.favoritedDb[event.id] != null

            val builder = SpannableStringBuilder()

            var titleText = Formatter.eventTitle(event)

            if (conflicting || favourite && remoteConfig.showEventGlyphs == true) {
                if (conflicting) {
                    builder.append(" ")
                    builder.setSpan(ImageSpan(activity, R.drawable.icon_attention_small), builder.length - 1, builder.length, 0)
                    builder.append(" ")
                }
                if (favourite) {
                    builder.append(" ")
                    builder.setSpan(ImageSpan(activity, R.drawable.icon_like_filled_small), builder.length - 1, builder.length, 0)
                    builder.append(" ")
                }
            }

            builder.append(titleText)

            // Assign the properties of the view
            holder.eventTitle.text = builder

            if (database.eventIsHappening(event, DateTime.now())) {
                // It's happening now
                holder.eventStartTime.text = "NOW"
            } else if (database.eventStart(event).isBeforeNow) {
                // It's already happened
                holder.eventStartTime.text = "DONE"
            } else if (database.eventIsUpcoming(event, DateTime.now(), 30)) {
                // It's upcoming, so we give a timer
                val countdown = database.eventStart(event).minus(DateTime.now().millis).millis / 1000 / 60
                holder.eventStartTime.text = "IN $countdown MIN"
            } else if (filterStrategy is AnyFavoritedEventFilter) {
                holder.eventStartTime.text = Formatter.shortTime(event.startTime, database.eventDay(event))
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
                vibrator.short().let { true }
            }
            holder.itemView.setOnLongClickListener {
                EventDialog(event).show(activity.supportFragmentManager, "Kek")
                vibrator.long().let { true }
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
            } else if (conflicting) {
                holder.eventCard.setBackgroundColor(getColor(context, R.color.alert))
            } else {
                holder.eventCard.setBackgroundColor(getColor(context, R.color.cardview_light_background))
            }
        }
    }

    val events by view(RecyclerView::class.java)
    val progress by view(ProgressBar::class.java)

    var effectiveEvents = emptyList<EventEntry>()

    val eventsTitle by view(TextView::class.java)

    val database: Database get() = letRoot { it.database }!!

    val remoteConfig: RemoteConfig get() = letRoot { it.remotePreferences }!!

    val vibrator by lazy { TouchVibrator(context) }

    lateinit var updateReceiver: EmbeddedLocalBroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle()

        // Configure the recycler
        events.setHasFixedSize(true)
        events.adapter = DataAdapter()

        // Filter the data
        dataUpdatedLong()

        if (filterStrategy.scrolling)
            events.layoutManager = LinearLayoutManager(activity)
        else
            events.layoutManager = NonScrollingLinearLayout(activity)

        events.itemAnimator = DefaultItemAnimator()

        updateReceiver = context.localReceiver(FragmentViewEvent.EVENT_STATUS_CHANGED) {
            events.adapter.notifyDataSetChanged()
        }

        updateReceiver.register()
    }

    private fun setTitle() {
        if (filterStrategy.getTitle() == "")
            eventsTitle.visibility = View.GONE
        else
            eventsTitle.text = filterStrategy.getTitle()
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

        if (effectiveEvents.isEmpty()) {
            eventsTitle.visibility = View.GONE
            events.visibility = View.GONE
        } else {
            setTitle()
            events.visibility = View.VISIBLE
        }
    }

    fun dataUpdatedLong() {
        object : AsyncTask<Unit, Unit, Iterable<EventEntry>>() {
            override fun onPreExecute() {
                logd { "Starting long data update" }
                try {
                    progress.visibility = View.VISIBLE
                    events.visibility = View.GONE
                } catch (throwable: Throwable) {
                    Analytics.exception(throwable)
                }
            }

            override fun doInBackground(vararg params: Unit?): Iterable<EventEntry>? {
                try {
                    return filterStrategy.filter(database, filterVal)
                } catch(throwable: Throwable) {
                    Analytics.exception(throwable)
                    return emptyList()
                }
            }

            override fun onPostExecute(result: Iterable<EventEntry>) {
                logd { "Completed long data update" }
                try {
                    effectiveEvents = result.toList()
                    events.adapter.notifyDataSetChanged()

                    progress.visibility = View.GONE

                    if (effectiveEvents.isEmpty()) {
                        eventsTitle.visibility = View.GONE
                        events.visibility = View.GONE
                    } else {
                        setTitle()
                        events.visibility = View.VISIBLE
                    }
                } catch (throwable: Throwable) {
                    Analytics.exception(throwable)
                }
            }
        }.execute()
    }
}