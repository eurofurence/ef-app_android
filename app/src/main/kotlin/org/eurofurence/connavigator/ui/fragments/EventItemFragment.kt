package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pawegio.kandroid.IntentFor
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.events.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.descriptionFor
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.preferences.AppPreferences
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.services.AnalyticsService.Action
import org.eurofurence.connavigator.services.AnalyticsService.Category
import org.eurofurence.connavigator.ui.LayoutConstants
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.ui.filters.start
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.alert
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Created by David on 4/9/2016.
 */
class EventItemFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()
    val args: EventItemFragmentArgs by navArgs()
    var subscriptions = Disposables.empty()
    val eventId by lazy { UUID.fromString(args.eventId) }
    val ui by lazy { EventUi() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.CID != null && !args.CID.equals(BuildConfig.CONVENTION_IDENTIFIER, true)) {
            alert("This item is not for this convention", "Wrong convention!").show()

            findNavController().popBackStack()
        }

        subscriptions += db.subscribe {
            fillUi()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    private fun fillUi() {
        if (eventId != null) {
            val event: EventRecord = db.events[eventId] ?: return

            AnalyticsService.event(Category.EVENT, Action.OPENED, event.title)

            val conferenceRoom = event[toRoom]

            ui.title.text = event.fullTitle()

            (event.description.markdownLinks() ?: "").let {
                if (it != ui.description.tag) {
                    ui.description.tag = it
                    ui.description.loadMarkdown(it)
                }
            }

            ui.time.text = getString(R.string.event_weekday_from_to, event.start.dayOfWeek().asText, event.startTimeString(), event.endTimeString())
            ui.organizers.text = event.ownerString()
            ui.room.text = getString(R.string.misc_room_number, conferenceRoom?.name
                    ?: getString(R.string.event_unable_to_locate_room))

            (event.posterImageId ?: event.bannerImageId).let {
                if (it != ui.image.tag) {
                    ui.image.tag = it
                    if (it != null) {
                        ui.image.visibility = View.VISIBLE
                        ImageService.load(db.images[it], ui.image)
                    } else
                        ui.image.visibility = View.GONE
                }
            }

            val description = descriptionFor(event).joinToString("\r\n\r\n")
            description.let {
                if (it != ui.extrasContent.tag) {
                    ui.extrasContent.tag = it
                    ui.extrasContent.text = it
                    ui.extras.visibility = if (it == "") View.GONE else View.VISIBLE
                }
            }

            changeFabIcon()

            ui.favoriteButton.setOnClickListener {
                if (AppPreferences.dialogOnEventPress) {
                    showDialog(event)
                } else {
                    favoriteEvent(event)
                }
            }

            ui.favoriteButton.setOnLongClickListener {
                if (AppPreferences.dialogOnEventPress) {
                    favoriteEvent(event)
                } else {
                    showDialog(event)
                }
                true
            }

            ui.feedbackButton.apply {
                visibility = if (event.isAcceptingFeedback) View.VISIBLE else View.GONE

                setImageDrawable(context.createFADrawable(R.string.fa_comment))

                setOnClickListener {
                    val action = EventItemFragmentDirections.actionFragmentViewEventToEventFeedbackFragment(args.eventId)

                    findNavController().navigate(action)
                }
            }

            childFragmentManager.beginTransaction()
                    .replace(R.id.event_map, MapDetailFragment().withArguments(conferenceRoom?.id, true), "mapDetails")
                    .commit()
        }
    }

    private fun showDialog(event: EventRecord) {
        context?.let {
            eventDialog(it, event, db)
        }
    }

    private fun favoriteEvent(event: EventRecord) {
        context?.let {
            it.sendBroadcast(IntentFor<EventFavoriteBroadcast>(it).apply { jsonObjects["event"] = event })
        }
    }

    /**
     * Changes the FAB based on if the current event is liked or not
     */
    private fun changeFabIcon() {
        (eventId in db.faves).let {
            if (it != ui.favoriteButton.tag) {
                ui.favoriteButton.tag = it
                if (it)
                    ui.favoriteButton.setImageDrawable(context?.createFADrawable(R.string.fa_heart_solid))
                else
                    ui.favoriteButton.setImageDrawable(context?.createFADrawable(R.string.fa_heart))
            }
        }
    }
}

class EventUi : AnkoComponent<Fragment> {
    lateinit var splitter: LinearLayout

    lateinit var extras: LinearLayout

    lateinit var extrasContent: TextView

    lateinit var image: PhotoView

    lateinit var title: TextView

    lateinit var time: TextView

    lateinit var room: TextView

    lateinit var organizers: TextView

    lateinit var description: MarkdownView

    lateinit var favoriteButton: FloatingActionButton
    lateinit var feedbackButton: FloatingActionButton

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        coordinatorLayout {
            backgroundResource = R.color.backgroundGrey
            isClickable = true

            scrollView {
                verticalLayout {
                    image = ankoView(::PhotoView, 0) {
                        contentDescription = "Event"
                        imageResource = R.drawable.placeholder_event
                        backgroundResource = R.drawable.image_fade
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        visibility = View.GONE
                        adjustViewBounds = true
                    }.lparams(matchParent, wrapContent)

                    verticalLayout {
                        id = R.id.event_splitter
                        backgroundResource = R.color.primaryDarker
                        padding = dip(15)
                        title = textView {
                            textResource = R.string.event
                            compatAppearance = android.R.style.TextAppearance_Large_Inverse
                            setPadding(0, dip(15), 0, dip(15))
                            gravity = Gravity.CENTER_HORIZONTAL
                        }.lparams(matchParent, wrapContent)

                        time = textView {
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            setPadding(0, 0, 0, dip(10))
                        }.lparams(matchParent, wrapContent, weight = 5F)

                        room = textView {
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            setPadding(0, 0, 0, dip(10))
                        }.lparams(matchParent, wrapContent, weight = 5F)

                        organizers = textView {
                            textResource = R.string.event_run_by
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            setPadding(0, 0, 0, dip(10))
                        }.lparams(matchParent, wrapContent, weight = 5F)
                    }.lparams(matchParent, wrapContent) {
                        setMargins(0, 0, 0, dip(10))
                    }

                    extras = verticalLayout {
                        backgroundResource = R.color.cardview_light_background
                        padding = dip(15)

                        extrasContent = fontAwesomeView {
                            text = "{fa-home} Glyphs"
                            singleLine = false
                            maxLines = 6
                        }.lparams(matchParent, wrapContent, weight = 5F)
                    }.lparams(matchParent, wrapContent) {
                        setMargins(0, 0, 0, dip(10))
                    }

                    verticalLayout {
                        backgroundResource = R.color.cardview_light_background
                        padding = dip(25)
                        description = ankoView(::MarkdownView, 0) {
                        }.lparams(matchParent, wrapContent)
                    }.lparams(matchParent, wrapContent)

                    verticalLayout {
                        id = R.id.event_map
                    }
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, matchParent)

            // Icon Layout

            linearLayout {
                feedbackButton = floatingActionButton {
                    imageResource = R.drawable.icon_menu
                    backgroundColorResource = R.color.accent
                }.lparams {
                    margin = dip(LayoutConstants.MARGIN_SMALL)
                }
                favoriteButton = floatingActionButton {
                }.lparams {
                    margin = dip(LayoutConstants.MARGIN_SMALL)
                }
            }.lparams(wrapContent, wrapContent) {
                anchorGravity = Gravity.BOTTOM or Gravity.END
                anchorId = R.id.event_splitter
                margin = dip(LayoutConstants.MARGIN_LARGE)
            }


        }
    }

}