package org.eurofurence.connavigator.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.*
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.filters.FilterType
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import org.joda.time.DateTime
import org.joda.time.Minutes
import kotlin.coroutines.experimental.buildSequence


/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()

    val ui by lazy { EventListView() }

    lateinit var eventList: EventList
    var title = ""
    var mainList = true
    var daysInsteadOfGlyphs = false
    var effectiveEvents = emptyList<EventRecord>()

    fun withArguments(eventList: EventList? = null, title: String = "", mainList: Boolean = true, daysInsteadOfGlyphs: Boolean = false) = apply {
        info { "Constructing event recycler $title" }

        arguments = Bundle().apply {
            if (eventList != null) {
                val map = eventList.filters.keys.map { it.toString() } + eventList.filters.values
                putStringArray("eventList", map.toTypedArray())
            } else {
                putStringArray("eventList", arrayOf())
            }

            putString("title", title)
            putBoolean("mainList", mainList)
            putBoolean("daysInsteadOfGlyphs", daysInsteadOfGlyphs)
        }
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
        val layout: LinearLayout by view()
    }

    inner class DataAdapter : RecyclerView.Adapter<EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): EventViewHolder =
                EventViewHolder(UI { SingleEventUi().createView(this) }.view)
//                        SingleEventUi()
//                        .createView(AnkoContext.createReusable(activity.applicationContext, parent)))

        override fun getItemCount() =
                effectiveEvents.size

        private fun EventViewHolder.setGlyphs(glyphs: Sequence<String>) {
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
                yieldAll(glyphsFor(event))
                if (isFavorite) yield("{fa-heart}")
                if (isDeviatingFromConBook == true) yield("{fa-pencil}")
            })

            if (daysInsteadOfGlyphs) holder.eventGlyphOverflow.text = db.eventStart(event).dayOfWeek().asShortText

            holder.eventTitle.text = event.fullTitle()

            val glyphEnd = "{fa-caret-right}"

            when {
                eventIsHappening(event, DateTime.now()) -> { // It's happening now
                    holder.eventStartTime.textResource = R.string.misc_now
                }
                eventStart(event).isBeforeNow -> // It's already happened
                    holder.eventStartTime.textResource = R.string.misc_done
                eventIsUpcoming(event, DateTime.now(), 30) -> { //it's happening in 30 minutes
                    // It's upcoming, so we give a timer
                    val countdown = Minutes.minutesBetween(DateTime.now(), eventStart(event)).minutes
                    holder.eventStartTime.text = getString(R.string.event_countdown_minutes, countdown)
                }
                else -> {
                    holder.eventStartTime.text = event.startTimeString()
                }
            }

            holder.eventEndTime.text = "$glyphEnd ${event.endTimeString()}"
            holder.eventRoom.text = db.rooms[event.conferenceRoomId]?.name ?: getString(R.string.misc_unknown)

            // Load image

            val image = db.images[event.bannerImageId]
            imageService.load(image, holder.eventImage)

            // Assign the on-click action
            holder.itemView.setOnClickListener {
                debug { "Short event click" }
                val action = when(findNavController().currentDestination?.id) {
                    R.id.fragmentViewHome -> FragmentViewHomeDirections.actionFragmentViewHomeToFragmentViewEvent(event.id.toString())
                    R.id.eventListFragment -> EventListFragmentDirections.actionFragmentViewEventsToFragmentViewEvent(event.id.toString())
                    else -> null
                }

                action?.apply {
                    findNavController().navigate(this)
                }
            }
            holder.itemView.setOnLongClickListener {
                context?.apply {
                    eventDialog(this, event, db)
                }
                true
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize events list.
        eventList = EventList(db)

        // Reading arguments
        arguments?.let {
            // Put all filters.
            val filters = it.getStringArray("eventList")
            for (i in 0 until filters.size / 2) {
                eventList.filters[FilterType.valueOf(filters[i])] = filters[i + filters.size / 2]
            }

            title = it.getString("title")
            mainList = it.getBoolean("mainList")
            daysInsteadOfGlyphs = it.getBoolean("daysInsteadOfGlyphs")
        }


        info { "Filling in view" }

        // Configure the recycler
        configureList()

        configureTitle()

        // Filter the data
        subscriptions += db.subscribe { dataUpdated() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    private fun configureTitle() {
        info { "Configuring title" }

        ui.title.text = title

        ui.title.visibility = if (title.isNotEmpty()) View.VISIBLE else View.GONE
        ui.bigLayout.visibility = if (effectiveEvents.any()) View.VISIBLE else View.GONE
    }

    private fun configureList() {
        info { "Configuring recycler" }
        ui.eventList.setHasFixedSize(true)
        ui.eventList.adapter = DataAdapter()
        ui.eventList.layoutManager = if (mainList) LinearLayoutManager(activity) else NonScrollingLinearLayout(activity)
        ui.eventList.itemAnimator = DefaultItemAnimator()

        // Change top margins for nested lists.
        (ui.bigLayout.layoutParams as? LinearLayout.LayoutParams)
                ?.setMargins(0, if (mainList) 0 else dip(10), 0, 0)


        // Add top padding only if in main list.
        ui.eventList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            val padding by lazy {
                val metrics = context!!.resources.displayMetrics
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15F, metrics).toInt()
            }

            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
                val itemPosition = parent.getChildAdapterPosition(view)
                if (itemPosition == RecyclerView.NO_POSITION) {
                    return
                }

                if (itemPosition == 0 && mainList) {
                    outRect.top = padding
                }

                val adapter = parent.adapter
                if (adapter != null && itemPosition == adapter.itemCount - 1) {
                    outRect.bottom = padding
                }
            }
        })
    }


    fun dataUpdated() {
        info { "Data was updated, redoing UI" }
        task {
            info { "Refiltering data" }
            effectiveEvents = eventList.applyFilters()
        } successUi {
            info { "Revealing new data" }
            ui.eventList.adapter.notifyDataSetChanged()

            configureTitle()
        } failUi {
            info { "Failed to get data" }
            configureTitle()
        }
    }
}

class SingleEventUi : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
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
                setPadding(dip(15), dip(3), dip(15), dip(3))
                id = R.id.layout


                tableLayout {
                    setColumnStretchable(2, true)
                    setColumnShrinkable(2, true)
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
                            singleLine = false
                            maxLines = 3
                        }.lparams(width = matchParent)
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
                            singleLine = false
                            maxLines = 3
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

class EventListView : AnkoComponent<Fragment> {
    lateinit var title: TextView
    lateinit var eventList: RecyclerView
    lateinit var bigLayout: LinearLayout

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        bigLayout = verticalLayout {
            backgroundResource = R.color.cardview_light_background
            lparams(matchParent, matchParent)

            title = textView("") {
                compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                padding = dip(15)
            }.lparams(matchParent, wrapContent) {
                setMargins(0, 0, 0, 0)
            }

            eventList = recycler {
            }.lparams(matchParent, matchParent)
        }
        bigLayout
    }
}