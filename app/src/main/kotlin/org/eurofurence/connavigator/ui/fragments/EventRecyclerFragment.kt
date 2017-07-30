package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.then
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.promiseOnUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.DataChanged
import org.eurofurence.connavigator.database.*
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.*
import org.jetbrains.anko.*
import org.joda.time.DateTime
import org.joda.time.Minutes
import kotlin.coroutines.experimental.buildSequence

fun HasDb.glyphFor(event: EventRecord): List<String> {
    // Show icon for the cockroach
    if (event.panelHosts?.contains("onkel kage", true) ?: false)
        return listOf("{fa-bug}", "{fa-glass}")

    // Decide for glyph based on name
    val name = event[toRoom]?.name ?: return emptyList()
    return when {
        "Art Show" in name -> listOf("{fa-photo}")
        "Dealer" in name -> listOf("{fa-shopping-cart}")
        "Main Stage" in name -> listOf("{fa-asterisk}")
        "Photoshoot" in name -> listOf("{fa-camera}")
        else -> emptyList()
    }
}

/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment() : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    val ui by lazy { EventListView() }
    val updateReceiver by lazy {
        context.localReceiver(DataChanged.DATACHANGED) {
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
        val eventSeparator: View by view()
        val eventImage: ImageView by view()
        val eventTitle: TextView by view()
        val eventGlyph: TextView by view()
        val eventGlyphOverflow: TextView by view()
        val eventStartTime: TextView by view()
        val eventEndTime: TextView by view()
        val eventRoom: TextView by view()
        val eventCard: LinearLayout by view("layout") // TODO Layout mismatch
        val layout: LinearLayout by view()
    }

    inner class DataAdapter : RecyclerView.Adapter<EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): EventViewHolder =
                EventViewHolder(SingleEventUi()
                        .createView(AnkoContext.createReusable(activity.applicationContext, parent)))

        override fun getItemCount() =
                effectiveEvents.size

        private fun EventViewHolder.setGlyphs(glyphs: Sequence<String>) {
            if (!RemotePreferences.showEventGlyphs) {
                eventGlyph.text = null
                eventGlyphOverflow.text = null
                return
            }

            val glyphList = glyphs.toList()

            when (glyphList.size) {
                0 -> {
                    eventGlyph.text = null
                    eventGlyphOverflow.text = null
                }
                1 -> {
                    eventGlyph.text = glyphList[0]
                    eventGlyphOverflow.text = null
                }
                2 -> {
                    eventGlyph.text = "${glyphList[0]} ${glyphList[1]}"
                    eventGlyphOverflow.text = null
                }
                3 -> {
                    eventGlyph.text = "${glyphList[0]} ${glyphList[1]}"
                    eventGlyphOverflow.text = glyphList[2]
                }
                else -> {
                    eventGlyph.text = "${glyphList[0]} ${glyphList[1]}"
                    eventGlyphOverflow.text = "${glyphList[2]} ${glyphList[3]}"
                }
            }
        }

        override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {

            // Get the event for the position
            val event = effectiveEvents[pos]

            if (pos > 0) {
                val eventBefore = effectiveEvents[pos - 1]
                if (eventBefore.startTime != event.startTime)
                    holder.eventSeparator.visibility = View.VISIBLE
                else
                    holder.eventSeparator.visibility = View.GONE
            } else {
                holder.eventSeparator.visibility = View.GONE
            }

            val isFavorite = event.id in faves
            val isDeviatingFromConBook = event.isDeviatingFromConBook

            holder.setGlyphs(buildSequence {
                yieldAll(glyphFor(event))
                if (isFavorite) yield("{fa-heart}")
                if (isDeviatingFromConBook) yield("{fa-pencil}")
            })

            holder.eventTitle.text = event.fullTitle()

            val glyphEnd = "{fa-caret-right}"

            when {
                eventIsHappening(event, DateTime.now()) -> { // It's happening now
                    holder.eventStartTime.text = "now"
                }
                eventStart(event).isBeforeNow -> // It's already happened
                    holder.eventStartTime.text = "done"
                eventIsUpcoming(event, DateTime.now(), 30) -> { //it's happening in 30 minutes
                    // It's upcoming, so we give a timer
                    val countdown = Minutes.minutesBetween(DateTime.now(), eventStart(event)).minutes
                    holder.eventStartTime.text = "${countdown}min"
                }
                else -> {
                    holder.eventStartTime.text = event.startTimeString()
                }
            }

            holder.eventEndTime.text = "$glyphEnd ${event.endTimeString()}"
            holder.eventRoom.text = Formatter.roomFull(event[toRoom]!!)

            // Load image

            val image = db.images[event.bannerImageId]
            imageService.load(image, holder.eventImage)

            // Assign the on-click action
            holder.itemView.setOnClickListener {
                logd { "Short event click" }
                applyOnRoot { navigateToEvent(event) }
            }
            holder.itemView.setOnLongClickListener {
                eventDialog(context, event, db)
                true
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            if (container == null)
                null
            else
                ui.createView(AnkoContext.create(container.context.applicationContext, container))

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
        } failUi {
            ui.loading.visibility = View.GONE
            ui.eventList.visibility = View.VISIBLE

            configureTitle()
        }
    }
}

class SingleEventUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            isClickable = true
            isLongClickable = true

            lparams(matchParent, wrapContent)
            backgroundResource = R.color.cardview_light_background

            linearLayout {
                id = R.id.eventSeparator
                lparams(matchParent, wrapContent)
                verticalPadding = dip(10)

                linearLayout {
                    lparams(matchParent, matchParent)
                    setBackgroundResource(R.drawable.wave_separator)
                }
            }

            verticalLayout {
                id = R.id.layout

                verticalPadding = dip(3)

                tableLayout {
                    setColumnStretchable(2, true)
                    horizontalPadding = dip(3)

                    lparams(matchParent, wrapContent)


                    tableRow {
                        textView {
                            id = R.id.eventStartTime
                            minMaxWidth = fw(15.percent())
                            gravity = Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Medium
                            singleLine = true
                        }

                        fontAwesomeView {
                            lparams(matchParent, matchParent)
                            id = R.id.eventGlyph
                            minMaxWidth = fw(15.percent())
                            horizontalPadding = dip(5)
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }

                        textView {
                            id = R.id.eventTitle
                            gravity = Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Medium
                            singleLine = true
                        }
                    }

                    tableRow {
                        fontAwesomeView {
                            id = R.id.eventEndTime
                            minMaxWidth = fw(15.percent())
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }

                        fontAwesomeView {
                            lparams(matchParent, matchParent)
                            id = R.id.eventGlyphOverflow
                            minMaxWidth = fw(15.percent())
                            horizontalPadding = dip(5)
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }

                        textView {
                            id = R.id.eventRoom
                            gravity = Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }
                    }
                }

                imageView {
                    lparams(matchParent, wrapContent)
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    id = R.id.eventImage
                }
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

            eventList = recycler {
                backgroundResource = R.color.cardview_light_background
            }.lparams(matchParent, matchParent)
        }
    }
}