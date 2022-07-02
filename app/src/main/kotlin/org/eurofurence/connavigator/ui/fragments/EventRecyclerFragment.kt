package org.eurofurence.connavigator.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
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
import com.pawegio.kandroid.runDelayed
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.glyphsFor
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.ui.filters.FilterChain
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.DatetimeProxy
import org.eurofurence.connavigator.util.ListAutoAdapter
import org.eurofurence.connavigator.util.extensions.*
import org.joda.time.Hours
import org.joda.time.Minutes


/**
 * Event view recycler to hold the viewpager items
 * TODO: Refactor the everliving fuck out of this shitty software
 */
class EventRecyclerFragment : DisposingFragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()
    lateinit var uiTitle: TextView
    lateinit var eventList: RecyclerView
    lateinit var bigLayout: LinearLayout

    var filters = FilterChain.empty
    var title = ""
    var mainList = true
    var daysInsteadOfGlyphs = false

    val layoutManager get() = eventList?.layoutManager

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
        daysInsteadOfGlyphs: Boolean = false
    ) = apply {
        info { "Constructing event recycler $title" }

        arguments = Bundle().apply {
            putParcelable("filters", filters)
            putString("title", title)
            putBoolean("mainList", mainList)
            putBoolean("daysInsteadOfGlyphs", daysInsteadOfGlyphs)
        }
    }

    val autoAdapter = ListAutoAdapter<EventRecord>({ id }) {
        receiver.createView {
            verticalLayout {
                // Set background to white.
                backgroundColorResource = R.color.backgroundGrey

                // Set own layout params.
                layoutParams = viewGroupLayoutParams(matchParent, wrapContent)

                // The group header, if top element.
                textView {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    setPadding(dip(5), dip(5), dip(5), dip(2))
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
                                hours in -4..-2 -> "Started ${-hours} hours ago"
                                hours == -1 -> "Started more than an hour ago"
                                minutes in -59..-1 -> "Started in the last hour"
                                minutes in 1..30 -> "Starting in less than 30 minutes"
                                minutes in 31..59 -> "Starting in less than an hour"
                                hours in 1..12 -> "Starting in $hours hours"
                                hours > 12 -> "At ${start.toString("HH:mm")}"
                                else -> "Now"
                            }
                            visibility = View.VISIBLE
                        } else {
                            visibility = View.GONE
                        }
                    }
                }

                // The title of the event.
                fontAwesomeView{
                    setPadding(dip(15), dip(10), dip(15), dip(5))
                    backgroundColorResource = R.color.lightBackground
                    compatAppearance = android.R.style.TextAppearance_Medium
                    singleLine = true
                    ellipsize = TextUtils.TruncateAt.END

                    // Bind from event's title.
                    from { isDeviatingFromConBook to fullTitle() } into { (deviating, title) ->
                        text = if (deviating) "${getString(R.string.fa_pencil_alt_solid)} $title" else title
                    }
                }

                // The location of the event.
                textView {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    setPadding(dip(15), dip(5), dip(15), dip(10))
                    backgroundColorResource = R.color.lightBackground
                    isSingleLine = true

                    // Bind from room or use default.
                    from { db.rooms[conferenceRoomId]?.name } into {
                        if (it is String)
                            text = it
                        else
                            textResource = R.string.misc_unknown
                    }

                }

                // Banner, if it's present
                imageView {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    backgroundColorResource = R.color.lightBackground
                    adjustViewBounds = true
                    visibility = View.GONE
                    from { bannerImageId } into {
                        ImageService.load(db.images[it], this)
                    }
                }

                // Horizontal stack of times and extra info.
                linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent) {
                        topMargin = 1
                    }

                    // Display day if in a view that has no day tab.
                    textView {
                        layoutParams = linearLayoutParams(wrapContent, matchParent) {
                            rightMargin = 1
                        }

                        setPadding(dip(15), dip(10), dip(15), dip(10))
                        setTypeface(null, Typeface.BOLD)

                        backgroundColorResource = R.color.backgroundGrey
                        isSingleLine = true
                        gravity = Gravity.CENTER

                        from { daysInsteadOfGlyphs to start.dayOfWeek().asShortText } into { (visible, day) ->
                            visibility = if (visible) View.VISIBLE else View.GONE
                            text = day
                        }
                    }

                    // Start time text.
                    textView {
                        layoutParams = linearLayoutParams(wrapContent, wrapContent) {
                            minimumWidth = dip(60)
                        }
                        setPadding(dip(15), dip(10), dip(10), dip(10))
                        padding = dip(10)
                        backgroundColorResource = R.color.lightBackground
                        isSingleLine = true
                        gravity = Gravity.CENTER

                        // TODO: special formats, happening now etc.
                        from { startTimeString() } into { text = it }
                    }

                    // Separator block
                    textView {
                        layoutParams = linearLayoutParams(wrapContent, matchParent)
                        setPadding(0, dip(10), 0, dip(10))
                        backgroundColorResource = R.color.lightBackground
                        gravity = Gravity.CENTER
                        text = "⋯"
                    }

                    // End time text.
                    textView {
                        layoutParams = linearLayoutParams(wrapContent, matchParent) {
                            rightMargin = 1
                            minimumWidth = dip(60)
                        }
                        setPadding(dip(10), dip(10), dip(15), dip(10))
                        backgroundColorResource = R.color.lightBackground
                        isSingleLine = true
                        gravity = Gravity.CENTER

                        from { endTimeString() } into { text = it }
                    }

                    fontAwesomeView {
                        layoutParams = linearLayoutParams(wrapContent, matchParent) {
                            rightMargin = 1
                        }
                        padding = dip(10)
                        backgroundColorResource = R.color.lightBackground
                        isSingleLine = true
                        gravity = Gravity.CENTER
                        text = getString(R.string.fa_heart)
                        textColorResource = R.color.favorite

                        from { id in faves } into {
                            visibility = if (it) View.VISIBLE else View.GONE
                        }
                    }

                    fontAwesomeView {
                        layoutParams = linearLayoutParams(matchParent, matchParent)
                        setPadding(dip(10), dip(10), dip(15), dip(10))
                        backgroundColorResource = R.color.lightBackground
                        isSingleLine = true
                        gravity = Gravity.CENTER_VERTICAL

                        // Bind from glyphs plus some extra glyphs.
                        from { glyphsFor(this) } into { text = it.joinToString(" ") }
                    }
                }

                // Add short click listener.
                from { id.toString() } into { id ->
                    isClickable = true
                    setOnClickListener {
                        val action = when (findNavController().currentDestination?.id) {
                            R.id.navHome -> HomeFragmentDirections.actionFragmentViewHomeToFragmentViewEvent(
                                id,
                                null
                            )
                            R.id.navEventList -> EventListFragmentDirections.actionFragmentViewEventsToFragmentViewEvent(
                                id,
                                null
                            )
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
                            eventDialog(this, record, db) { dataUpdated() }
                        }
                        true
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        bigLayout = verticalLayout {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)
            backgroundResource = R.color.lightBackground

            uiTitle = textView("") {
                layoutParams = linearLayoutParams(matchParent, wrapContent) {
                    setMargins(0, 0, 0, 0)
                }
                compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                padding = dip(15)
            }

            eventList = recycler {
                layoutParams = linearLayoutParams(matchParent, matchParent)
            }
        }
    }

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
        db.subscribe { dataUpdated() }.collectOnDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        layoutManager?.also { lm ->
            outState.putParcelable("lm_key", lm.onSaveInstanceState())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        layoutManager?.also { lm ->
            runDelayed(200) {
                savedInstanceState
                    ?.getParcelable<Parcelable>("lm_key")
                    ?.let(lm::onRestoreInstanceState)
            }
        }
    }

    private fun configureTitle() {
        info { "Configuring title" }

        uiTitle.text = title

        uiTitle.visibility = if (title.isNotEmpty()) View.VISIBLE else View.GONE
        bigLayout.visibility = if (autoAdapter.list.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun configureList() {
        info { "Configuring recycler" }
        eventList?.adapter = autoAdapter
        eventList?.layoutManager =
            if (mainList) LinearLayoutManager(activity) else NonScrollingLinearLayout(activity)
        eventList?.itemAnimator = DefaultItemAnimator()

        // Change top margins for nested lists.
        (bigLayout.layoutParams as? LinearLayout.LayoutParams)
            ?.setMargins(0, if (mainList) 0 else bigLayout.dip(10), 0, 0)


        // Add top padding only if in main list.
        eventList?.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
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
