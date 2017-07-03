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
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.FragmentViewEvent
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.dialogs.EventDialog
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.recycler
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*
import org.joda.time.DateTime

/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment() : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    val ui by lazy { EventListView() }
    val updateReceiver: EmbeddedLocalBroadcastReceiver by lazy {
        context.localReceiver(FragmentViewEvent.EVENT_STATUS_CHANGED) {
            dataUpdated()
        }
    }

    lateinit var eventList: EventList
    var title = ""
    var scrolling = true


    var effectiveEvents = emptyList<EventRecord>()

    constructor(eventList: EventList, title: String = "", scrolling: Boolean = true) : this() {
        info { "Constructing event recycler $title" }
        this.eventList = eventList
        this.title = title
        this.scrolling = scrolling
    }


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
                        if (RemotePreferences.showEventGlyphs) {
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
                true
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            ui.createView(AnkoContext.Companion.create(container!!.context, container))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        info { "Filling in view" }

        // Configure the recycler
        configureList()

        configureTitle()

        // Filter the data
        dataUpdated()

        updateReceiver.register()
    }

    private fun configureTitle() {
        info { "Configuring title" }
        if (this.title.isNotEmpty()) {
            info { "Showing title $title" }
            ui.title.text = this.title
            ui.title.visibility = View.VISIBLE
        } else {
            info { "No title presents" }
            ui.title.visibility = View.GONE
        }
    }

    private fun configureList() {
        info { "Configuring recycler" }
        ui.eventList.setHasFixedSize(true)
        ui.eventList.adapter = DataAdapter()
        ui.eventList.layoutManager = if (scrolling) LinearLayoutManager(activity) else NonScrollingLinearLayout(activity)
        ui.eventList.itemAnimator = DefaultItemAnimator()
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
        info { "Data was updated, redoing UI" }
        promiseOnUi {
            info { "Hiding critical UI elements" }
            ui.eventList.visibility = View.GONE
            ui.title.visibility = View.GONE
            ui.loading.visibility = View.VISIBLE
        } then {
            info { "Refiltering data" }
            effectiveEvents = eventList.applyFilters()
        } successUi {
            info { "Revealing new data" }
            ui.eventList.adapter.notifyDataSetChanged()
            ui.loading.visibility = View.GONE
            ui.eventList.visibility = View.VISIBLE

            configureTitle()
        } fail {
            ui.loading.visibility = View.GONE
            ui.eventList.visibility = View.VISIBLE

            configureTitle()
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
            verticalPadding = dip(10)
            backgroundResource = R.color.cardview_light_background
            verticalLayout {
                lparams(matchParent, wrapContent)
                linearLayout {
                    lparams(matchParent, matchParent)
                    weightSum = 10f
                    verticalLayout {
                        lparams(wrapContent, wrapContent)

                        textView {
                            id = R.id.eventStartTime
                            maxLines = 1
                            setTextAppearance(ctx, android.R.style.TextAppearance_Medium)
                        }

                        textView {
                            id = R.id.eventEndTime
                            maxLines = 1
                            setTextAppearance(ctx, android.R.style.TextAppearance_Small)
                        }
                    }
                    verticalLayout {
                        lparams(wrapContent, wrapContent)
                        textView {
                            id = R.id.eventTitle
                            setTextAppearance(ctx, android.R.style.TextAppearance_Medium)
                        }
                        textView {
                            id = R.id.eventRoom
                            setTextAppearance(ctx, android.R.style.TextAppearance_Small)
                        }
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

class EventListView : AnkoComponent<ViewGroup> {
    lateinit var title: TextView
    lateinit var loading: ProgressBar
    lateinit var eventList: RecyclerView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            title = textView("") {
                padding = dip(15)
            }.lparams(matchParent, wrapContent)

            loading = progressBar().lparams(matchParent, wrapContent)

            eventList = recycler {}.lparams(matchParent, matchParent)
        }
    }
}