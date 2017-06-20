package org.eurofurence.connavigator.ui.fragments

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
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.then
import nl.komponents.kovenant.ui.promiseOnUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.*
import org.eurofurence.connavigator.ui.FragmentViewEvent
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.dialogs.EventDialog
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.pref.RemoteConfig
import org.eurofurence.connavigator.util.TouchVibrator
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.letRoot
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*
import org.joda.time.DateTime

/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment(val eventList: EventList) : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    // Event view holder finds and memorizes the views in an event card
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage: ImageView by view()
        val eventTitle: TextView by view()
        val eventStartTime: TextView by view()
        val eventEndTime: TextView by view()
        val eventRoom: TextView by view()
        val eventCard: LinearLayout by view("layout") // TODO Layout mismatch
        val layout: LinearLayout by view()
    }

    inner class DataAdapter : RecyclerView.Adapter<EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): EventViewHolder =
                EventViewHolder(SingleEventUi().createView(AnkoContext.Companion.create(activity.baseContext, parent)))

        override fun getItemCount() =
                effectiveEvents.size

        override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {
            // Get the event for the position
            val event = effectiveEvents[pos]

            val isFavourite = event.id in faves

            SpannableStringBuilder()
                    .apply {
                        if (remoteConfig.showEventGlyphs) {
                            if (isFavourite) {
                                this.append(" ")
                                this.setSpan(
                                        ImageSpan(activity, R.drawable.icon_like_filled_small),
                                        this.length - 1,
                                        this.length, 0)
                                this.append(" ")
                            }
                        }
                    }
                    .append(Formatter.eventTitle(event))
                    .apply { holder.eventTitle.text = this }

            when {
                eventIsHappening(event, DateTime.now()) -> { // It's happening now
                    holder.eventStartTime.text = "NOW"
                    holder.eventCard.setBackgroundColor(getColor(context, R.color.accentLighter))
                }
                eventStart(event).isBeforeNow -> // It's already happened
                    holder.eventStartTime.text = "DONE"
                eventIsUpcoming(event, DateTime.now(), 30) -> { //it's happening in 30 minutes
                    // It's upcoming, so we give a timer
                    val countdown = eventStart(event).minus(DateTime.now().millis).millis / 1000 / 60
                    holder.eventStartTime.text = "IN $countdown MIN"
                }
                eventEnd(event).isBeforeNow -> {// Event end is before the current time, so it has already occurred thus it is gray
                    holder.eventCard.setBackgroundColor(getColor(context, R.color.backgroundGrey))
                }
                isFavourite -> {// Event is in favourites, thus it is coloured in primary
                    holder.eventCard.setBackgroundColor(getColor(context, R.color.primaryLighter))
                }
                else -> {
                    holder.eventStartTime.text = Formatter.shortTime(event.startTime)
                    holder.eventCard.setBackgroundColor(getColor(context, R.color.cardview_light_background))
                }
            }

            holder.eventEndTime.text = "until ${Formatter.shortTime(event.endTime)}"
            holder.eventRoom.text = Formatter.roomFull(event[toRoom]!!)

            // Load image
            /* TODO
            imageService.load( database.imageDb[event.imageId], holder.eventImage)
            */

            // Assign the on-click action
            holder.itemView.setOnClickListener {
                logd { "Short event click" }
                applyOnRoot { navigateToEvent(event) }
            }
            holder.itemView.setOnLongClickListener {
                EventDialog(event).show(activity.supportFragmentManager, "Kek")
                vibrator.long().let { true }
            }
        }
    }

    val eventsView: RecyclerView by view("events") // TODO: Anko the shit outta this

    val progress: ProgressBar by view()

    var effectiveEvents = emptyList<EventRecord>()

    val eventsTitle: TextView by view()

    val remoteConfig: RemoteConfig get() = letRoot { it.remotePreferences }!!

    val vibrator by lazy { TouchVibrator(context) }

    lateinit var updateReceiver: EmbeddedLocalBroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_events, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure the recycler
        eventsView.setHasFixedSize(true)
        eventsView.adapter = DataAdapter()

        // Filter the data
        dataUpdated()

        eventsView.layoutManager = LinearLayoutManager(activity)

        eventsView.itemAnimator = DefaultItemAnimator()

        updateReceiver = context.localReceiver(FragmentViewEvent.EVENT_STATUS_CHANGED) {
            dataUpdated()
        }

        updateReceiver.register()
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
        promiseOnUi {
            eventsView.visibility = View.GONE
            eventsTitle.visibility = View.GONE
            progress.visibility = View.VISIBLE
        } then {
            effectiveEvents = eventList.applyFilters()
        } successUi {
            eventsView.adapter.notifyDataSetChanged()
            progress.visibility = View.GONE
            eventsView.visibility = View.VISIBLE
            eventsTitle.visibility = View.GONE
        }
    }
}

class SingleEventUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            id = R.id.layout
            lparams(matchParent, wrapContent)
            isClickable = true
            isLongClickable = true
            verticalLayout {
                lparams(matchParent, wrapContent)
                backgroundResource = R.color.cardview_light_background
                linearLayout {
                    lparams(matchParent, matchParent)
                    weightSum = 10f
                    verticalLayout {
                        lparams(wrapContent, wrapContent)
                        padding = dip(15)

                        textView {
                            id = R.id.eventStartTime
                            maxLines = 1
                        }.applyRecursively { android.R.style.TextAppearance_Medium }

                        textView {
                            id = R.id.eventEndTime
                            maxLines = 1
                        }.applyRecursively { android.R.style.TextAppearance_Small }
                    }
                    verticalLayout {
                        lparams(wrapContent, wrapContent)
                        textView { id = R.id.eventTitle }.applyRecursively { android.R.style.TextAppearance_Large }
                        textView { id = R.id.eventRoom }.applyRecursively { android.R.style.TextAppearance_Small }
                    }
                }
            }

            imageView {
                maxWidth = matchParent
                maxHeight = dip(200)
                scaleType = ImageView.ScaleType.CENTER_CROP
                id = R.id.eventImage
            }
        }
    }
}