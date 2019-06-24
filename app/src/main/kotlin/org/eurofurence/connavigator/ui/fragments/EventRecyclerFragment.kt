package org.eurofurence.connavigator.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joanzapata.iconify.widget.IconTextView
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.glyphsFor
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.ui.filters.FilterChain
import org.eurofurence.connavigator.ui.filters.isHappening
import org.eurofurence.connavigator.ui.filters.isUpcoming
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.*
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder.Side.*
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import org.joda.time.DateTime
import org.joda.time.Minutes


/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment : Fragment(), HasDb, AnkoLogger {
    private val results = ArrayList<EventRecord>()

    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()

    val ui by lazy { EventListView() }

    var filters = FilterChain.empty
    var title = ""
    var mainList = true
    var daysInsteadOfGlyphs = false

    /**
     * Assigns the [arguments] with the given parameters.
     * @param filters If given, the filter chain to apply on the events.
     * @param title If given, the title to use.
     * @param mainList If true, this is a main list.
     * @param daysInsteadOfGlyphs If true, short day names are displayed instread of glyphs.
     */
    fun withArguments(
            filters: FilterChain = FilterChain.empty,
            title: String = "",
            mainList: Boolean = true,
            daysInsteadOfGlyphs: Boolean = false) = apply {
        info { "Constructing event recycler $title" }

        arguments = Bundle().apply {
            putParcelable("filters", filters)
            putString("title", title)
            putBoolean("mainList", mainList)
            putBoolean("daysInsteadOfGlyphs", daysInsteadOfGlyphs)
        }
    }

    // Event view holder finds and memorizes the views in an event card
    inner class EventViewHolder(val ui: SingleEventUi, view: View) : RecyclerView.ViewHolder(view) {
        /**
         * Assigns the to glyphs via the text.
         */
        fun assignGlyph(to: String?) {
            if (ui.glyphs.text?.toString() == to)
                return
            ui.glyphs.text = to
        }

        fun assignGlyphOverflow(to: String?) {
            if (ui.glyphs2.text?.toString() == to)
                return
            ui.glyphs2.text = to
        }

        fun assignGlyphsFrom(glyphs: List<String>) {
            when (glyphs.size) {
                0 -> {
                    assignGlyph(null)
                    assignGlyphOverflow(null)
                }
                1 -> {
                    assignGlyph(glyphs[0])
                    assignGlyphOverflow(null)
                }
                2 -> {
                    assignGlyph("${glyphs[0]} ${glyphs[1]}")
                    assignGlyphOverflow(null)
                }
                3 -> {
                    assignGlyph("${glyphs[0]} ${glyphs[1]}")
                    assignGlyphOverflow(glyphs[2])
                }
                else -> {
                    assignGlyph("${glyphs[0]} ${glyphs[1]}")
                    assignGlyphOverflow("${glyphs[2]} ${glyphs[3]}")
                }
            }
        }
    }

    inner class DataAdapter : RecyclerView.Adapter<EventViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int): EventViewHolder {
            // Get UI, create it's views.
            val ui = SingleEventUi()
            val view = UI { ui.createView(this) }.view

            // Return event view holder on that UI.
            return EventViewHolder(ui, view)
        }

        override fun getItemCount() =
                results.size

        override fun onBindViewHolder(holder: EventViewHolder, pos: Int) {

            // Get the event for the position
            val event = results[pos]

            // Reveal separator if different start time.
            holder.ui.separator.visibility = if (pos > 0 && results[pos - 1].start != event.start)
                View.VISIBLE
            else
                View.GONE

            // Detect some flags for glyph computation.
            val isFavorite = event.id in faves
            val isDeviatingFromConBook = event.isDeviatingFromConBook

            // Assign glyphs.
            holder.assignGlyphsFrom(glyphsFor(event) + listOfNotNull(
                    "{fa-heart}".takeIf { isFavorite },
                    "{fa-pencil}".takeIf { isDeviatingFromConBook }))


            // Override glyphs for day names if necessary.
            if (daysInsteadOfGlyphs)
                holder.assignGlyphOverflow(event.start.dayOfWeek().asShortText)

            // Set title.
            holder.ui.title.text = event.fullTitle()

            // Disambiguate on start time.
            when {
                // Event is happening right now.
                event.isHappening -> {
                    holder.ui.start.textResource = R.string.misc_now
                }

                // It's already happened.
                event.start.isBeforeNow -> {
                    holder.ui.start.textResource = R.string.misc_done
                }

                // It's about to happen.
                event.isUpcoming -> {
                    // Compute how many minutes are left.
                    val countdown = Minutes.minutesBetween(DateTime.now(), event.start).minutes

                    // Assign countdown.
                    holder.ui.start.text = getString(R.string.event_countdown_minutes, countdown)
                }
                else -> {
                    // Nothing special about the event, format time only.
                    holder.ui.start.text = event.startTimeString()
                }
            }

            // Simply assign end time.
            holder.ui.end.text = "{fa-caret-right} ${event.endTimeString()}"

            // Assign room.
            val name = db.rooms[event.conferenceRoomId]?.name
            if (name != null)
                holder.ui.room.text = name
            else
                holder.ui.room.textResource = R.string.misc_unknown


            // Load image.
            val image = db.images[event.bannerImageId]
            imageService.load(image, holder.ui.image)

            // Assign the on-click action.
            holder.itemView.setOnClickListener {
                debug { "Short event click" }
                val action = when (findNavController().currentDestination?.id) {
                    R.id.fragmentViewHome -> FragmentViewHomeDirections.actionFragmentViewHomeToFragmentViewEvent(event.id.toString())
                    R.id.eventListFragment -> EventListFragmentDirections.actionFragmentViewEventsToFragmentViewEvent(event.id.toString())
                    else -> null
                }

                action?.apply {
                    findNavController().navigate(this)
                }
            }

            // Assign the on-long-click action.
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

        // Reading arguments
        arguments?.let {
            filters = it.getParcelable<FilterChain>("filters") ?: FilterChain.empty
            title = it.getString("title") ?: "Could not get title"
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
        ui.bigLayout.visibility = if (results.any()) View.VISIBLE else View.GONE
    }

    private fun configureList() {
        info { "Configuring recycler" }
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

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
            info { "${System.currentTimeMillis()} Refiltering data" }


            // Resolve filter results into list.
            filters.resolve(db)
        } successUi { resolved ->
            info { "${System.currentTimeMillis()} Revealing new data" }

            // Clear existing results and add new.
            results.clear()
            resolved.forEach {
                results.add(it)
            }

            // Notify change.
            ui.eventList.adapter?.notifyDataSetChanged()

            configureTitle()
        } failUi {
            info { "${System.currentTimeMillis()} Failed to get data" }
            configureTitle()
        }
    }
}

//class SingleEventUi : AnkoComponent<Fragment> {
//    lateinit var separator: View
//    lateinit var image: ImageView
//    lateinit var title: TextView
//    lateinit var glyphs: IconTextView
//    lateinit var glyphs2: IconTextView
//    lateinit var start: TextView
//    lateinit var end: IconTextView
//    lateinit var room: TextView
//
//    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
//        constraintLayout {
//            separator = view {
//                id = R.id.se_separator
//                setBackgroundResource(R.drawable.wave_separator)
//
//            }.lparams(wrapContent, dip(10)) {
//                setMargins(8, 8, 8, 8)
//            }
//
//            start = textView {
//                id = R.id.se_start
//                compatAppearance = android.R.style.TextAppearance_Medium
//                singleLine = true
//            }.lparams(dip(50), wrapContent) {
//                setMargins(8, 8, 8, 8)
//            }
//
//            glyphs = fontAwesomeView {
//                id = R.id.se_glyphs
//                gravity = Gravity.END
//                compatAppearance = android.R.style.TextAppearance_Small
//                singleLine = true
//            }.lparams(dip(30), wrapContent) {
//                setMargins(8, 8, 8, 8)
//            }
//
//            title = textView {
//                id = R.id.se_title
//                compatAppearance = android.R.style.TextAppearance_Medium
//                singleLine = false
//                maxLines = 3
//            }.lparams(width = matchParent) {
//                setMargins(8, 8, 8, 8)
//            }
//
//            end = fontAwesomeView {
//                id = R.id.se_end
//                gravity = Gravity.END
//                compatAppearance = android.R.style.TextAppearance_Small
//                singleLine = true
//            }.lparams(dip(50), wrapContent) {
//                setMargins(8, 8, 8, 8)
//            }
//
//            glyphs2 = fontAwesomeView {
//                id = R.id.se_glyphs2
//                gravity = Gravity.END
//                compatAppearance = android.R.style.TextAppearance_Small
//                singleLine = true
//            }.lparams(dip(30), wrapContent) {
//                setMargins(8, 8, 8, 8)
//            }
//
//            room = textView {
//                id = R.id.se_room
//                compatAppearance = android.R.style.TextAppearance_Small
//                singleLine = false
//                maxLines = 3
//            }
//
//            image = imageView {
//                id = R.id.se_image
//                scaleType = ImageView.ScaleType.FIT_CENTER
//            }.lparams(0, wrapContent) {
//                setMargins(0, 8, 0, 0)
//            }
//
//            applyConstraintSet {
//                separator {
//                    connect(
//                            TOP to TOP of PARENT_ID,
//                            START to START of PARENT_ID,
//                            END to END of PARENT_ID)
//
//                }
//
//                start {
//                    connect(BASELINE to BASELINE of title,
//                            START to START of PARENT_ID)
//                }
//
//                end {
//                    connect(BASELINE to BASELINE of room,
//                            START to START of PARENT_ID)
//                }
//
//                glyphs {
//                    connect(
//                            BASELINE to BASELINE of title,
//                            START to END of start)
//                }
//
//                glyphs2 {
//                    connect(
//                            BASELINE to BASELINE of room,
//                            START to END of end)
//                }
//
//                title {
//                    connect(
//                            TOP to BOTTOM of separator,
//                            END to END of PARENT_ID,
//                            START to END of glyphs)
//                }
//
//                room {
//                    connect(
//                            END to END of PARENT_ID,
//                            TOP to BOTTOM of title,
//                            START to END of glyphs2
//                    )
//                }
//
//                image {
//                    connect(
//                            START to START of PARENT_ID,
//                            END to END of PARENT_ID,
//                            TOP to BOTTOM of room
//                    )
//                }
//
//            }
//        }
//    }
//}

class SingleEventUi : AnkoComponent<Fragment> {
    lateinit var separator: View
    lateinit var image: ImageView
    lateinit var title: TextView
    lateinit var glyphs: IconTextView
    lateinit var glyphs2: IconTextView
    lateinit var start: TextView
    lateinit var end: IconTextView
    lateinit var room: TextView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            isClickable = true
            isLongClickable = true

            lparams(matchParent, wrapContent)
            backgroundResource = R.color.cardview_light_background

            separator = linearLayout {
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
                        start = textView {
                            id = R.id.eventStartTime
                            minMaxWidth = fw(15.percent())
                            gravity = Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Medium
                            singleLine = true
                        }

                        glyphs = fontAwesomeView {
                            lparams(matchParent, matchParent)
                            id = R.id.eventGlyph
                            minMaxWidth = fw(15.percent())
                            horizontalPadding = dip(5)
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }

                        title = textView {
                            id = R.id.eventTitle
                            gravity = Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Medium
                            singleLine = false
                            maxLines = 3
                        }.lparams(width = matchParent)
                    }

                    tableRow {
                        end = fontAwesomeView {
                            id = R.id.eventEndTime
                            minMaxWidth = fw(15.percent())
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }

                        glyphs2 = fontAwesomeView {
                            lparams(matchParent, matchParent)
                            id = R.id.eventGlyphOverflow
                            minMaxWidth = fw(15.percent())
                            horizontalPadding = dip(5)
                            gravity = Gravity.END or Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = true
                        }

                        room = textView {
                            id = R.id.eventRoom
                            gravity = Gravity.CENTER_VERTICAL
                            compatAppearance = android.R.style.TextAppearance_Small
                            singleLine = false
                            maxLines = 3
                        }
                    }
                }

                image = imageView {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    id = R.id.eventImage
                }.lparams(matchParent, wrapContent)
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