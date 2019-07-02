package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.glyphsFor
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.ui.filters.FilterChain
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.DatetimeProxy
import org.eurofurence.connavigator.util.ListAutoAdapter
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.dip
import org.joda.time.Hours
import org.joda.time.Minutes


/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment : Fragment(), HasDb, AnkoLogger {
    //private val results = ArrayList<EventRecord>()

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

    val autoAdapter = ListAutoAdapter<EventRecord>({ id }) {
        verticalLayout {
            // Set background to white.
            backgroundColorResource = R.color.backgroundGrey

            // Set own layout params.
            lparams(
                    width = matchParent,
                    height = wrapContent)

            // The group header, if top element.
            textView {
                setPadding(dip(5), dip(5), dip(5), dip(5))
                textColorResource = R.color.mutedText
                backgroundColorResource = R.color.backgroundGrey

                fromAll { before, _ ->
                    (before.firstOrNull()?.start?.equals(start) != true) to start
                } into { (visible, start) ->
                    if (visible && mainList) {
                        val now = DatetimeProxy.now()
                        val hours = Hours.hoursBetween(now, start).hours
                        val minutes = Minutes.minutesBetween(now, start).minutes
                        text = when {
                            hours < -4 -> "At ${start.toString("HH:mm")}"
                            hours in -4..1 -> "Started ${-hours} hours ago"
                            minutes in -59..-1 -> "Started in the last hour"
                            minutes in 1..59 -> "Starting in $minutes minutes"
                            hours in 1..12 -> "Starting in $hours hours"
                            hours > 12 -> "At ${start.toString("HH:mm")}"
                            else -> "Now"
                        }
                        visibility = View.VISIBLE
                    } else {
                        visibility = View.GONE
                    }
                }
            }.lparams(
                    width = matchParent,
                    height = wrapContent)

            // The title of the event.
            fontAwesomeView {
                setPadding(dip(15), dip(15), dip(15), dip(5))
                backgroundColorResource = R.color.cardview_light_background
                compatAppearance = android.R.style.TextAppearance_Medium
                singleLine = true

                // Bind from event's title.
                from { isDeviatingFromConBook to fullTitle() } into { (deviating, title) ->
                    text = if (deviating) "{fa-pencil} $title" else title
                }
            }

            // The location of the event.
            textView {
                setPadding(dip(15), dip(5), dip(15), dip(15))
                backgroundColorResource = R.color.cardview_light_background
                isSingleLine = true

                // Bind from room or use default.
                from { db.rooms[conferenceRoomId]?.name } into {
                    content = it ?: R.string.misc_unknown
                }

            }.lparams(
                    width = matchParent,
                    height = wrapContent) {
            }

            // Banner, if it's present
            imageView {
                backgroundColorResource = R.color.cardview_light_background
                visibility = View.GONE
                from { bannerImageId } into {
                    ImageService.load(db.images[it], this)
                }
            }.lparams(matchParent, wrapContent) {
                topMargin = dip(-10)
            }

            // Horizontal stack of times and extra info.
            linearLayout {

                // Start time text.
                textView {
                    setPadding(dip(15), dip(10), dip(10), dip(10))
                    backgroundColorResource = R.color.cardview_light_background
                    isSingleLine = true
                    gravity = Gravity.CENTER

                    // TODO: special formats, happening now etc.
                    from { startTimeString() } into { text = it }
                }.lparams(
                        width = wrapContent,
                        height = wrapContent) {
                    minimumWidth = dip(60)
                }

                // Separator block
                textView {
                    verticalPadding = dip(10)
                    backgroundColorResource = R.color.cardview_light_background
                    gravity = Gravity.CENTER
                    text = "⋯"
                }.lparams(
                        width = wrapContent,
                        height = matchParent)

                // End time text.
                textView {
                    padding = dip(10)
                    backgroundColorResource = R.color.cardview_light_background
                    isSingleLine = true
                    gravity = Gravity.CENTER

                    from { endTimeString() } into { text = it }
                }.lparams(
                        width = wrapContent,
                        height = matchParent) {
                    rightMargin = 1
                    minimumWidth = dip(60)
                }

                // Display day if in a view that has no day tab.
                fontAwesomeView {
                    padding = dip(10)
                    backgroundColorResource = R.color.cardview_light_background
                    isSingleLine = true
                    gravity = Gravity.CENTER

                    from { daysInsteadOfGlyphs to start.dayOfWeek().asShortText } into { (visible, day) ->
                        visibility = if (visible) View.VISIBLE else View.GONE
                        text = day
                    }
                }.lparams(
                        width = wrapContent,
                        height = matchParent) {
                    rightMargin = 1
                }

                fontAwesomeView {
                    padding = dip(10)
                    backgroundColorResource = R.color.cardview_light_background
                    isSingleLine = true
                    gravity = Gravity.CENTER
                    text = "{fa-heart}"

                    from { id in faves } into {
                        visibility = if (it) View.VISIBLE else View.GONE
                    }
                }.lparams(
                        width = wrapContent,
                        height = matchParent) {
                    rightMargin = 1
                }

                fontAwesomeView {
                    setPadding(dip(10), dip(10), dip(15), dip(10))
                    backgroundColorResource = R.color.cardview_light_background
                    isSingleLine = true
                    gravity = Gravity.CENTER_VERTICAL

                    // Bind from glyphs plus some extra glyphs.
                    from { glyphsFor(this) } into { text = it.joinToString(" ") }
                }.lparams(
                        width = matchParent,
                        height = matchParent)
            }.lparams(
                    width = matchParent,
                    height = wrapContent) {
                topMargin = 1
            }//dip(40))

            // Add short click listener.
            from { id.toString() } into { id ->
                isClickable = true
                setOnClickListener {
                    val action = when (findNavController().currentDestination?.id) {
                        R.id.navHome -> HomeFragmentDirections.actionFragmentViewHomeToFragmentViewEvent(id)
                        R.id.navEventList -> EventListFragmentDirections.actionFragmentViewEventsToFragmentViewEvent(id)
                        else -> null
                    }

                    action?.apply {
                        findNavController().navigate(this)
                    }
                }
            }

            // Add long click listener.
            from { this } into { record ->
                isLongClickable = true
                setOnLongClickListener {
                    context?.apply {
                        eventDialog(this, record, db)
                    }
                    true
                }
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
        ui.bigLayout.visibility = if (autoAdapter.list.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun configureList() {
        info { "Configuring recycler" }
        ui.eventList.adapter = autoAdapter
        ui.eventList.layoutManager = if (mainList) LinearLayoutManager(activity) else NonScrollingLinearLayout(activity)
        ui.eventList.itemAnimator = DefaultItemAnimator()

        // Change top margins for nested lists.
        (ui.bigLayout.layoutParams as? LinearLayout.LayoutParams)
                ?.setMargins(0, if (mainList) 0 else dip(10), 0, 0)


        // Add top padding only if in main list.
        ui.eventList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
//        ui.eventList.addItemDecoration(object : RecyclerView.ItemDecoration() {
//            val padding by lazy {
//                val metrics = context!!.resources.displayMetrics
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15F, metrics).toInt()
//            }
//
//            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//                val itemPosition = parent.getChildAdapterPosition(view)
//                if (itemPosition == RecyclerView.NO_POSITION) {
//                    return
//                }
//
//                if (itemPosition == 0 && mainList) {
//                    outRect.top = padding
//                }
//
//                val adapter = parent.adapter
//                if (adapter != null && itemPosition == adapter.itemCount - 1) {
//                    outRect.bottom = padding
//                }
//            }
//        })
    }


    fun dataUpdated() {
        task {
            info { "Updating UI from data change" }

            filters.resolve(db)
        } successUi {
            info { "Data retrieved, presenting" }

            autoAdapter.list.clear()
            it.forEach { item ->
                autoAdapter.list.add(item)
            }
            autoAdapter.notifyDataSetChanged()

            info { "Updating title" }

            configureTitle()
        } failUi {
            info { "Data failed, updating title" }

            configureTitle()
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