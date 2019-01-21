package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.pawegio.kandroid.IntentFor
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.descriptionFor
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.tracking.Analytics.Action
import org.eurofurence.connavigator.tracking.Analytics.Category
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.support.v4.UI
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Created by David on 4/9/2016.
 */
class EventItemFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()

    private val eventId
        get () = try {
            UUID.fromString(EventItemFragmentArgs.fromBundle(arguments).eventId)
        } catch (e: Exception) {
            null
        }

    val ui by lazy { EventUi() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

            Analytics.event(Category.EVENT, Action.OPENED, event.title)

            val conferenceRoom = event[toRoom]

            ui.title.text = event.fullTitle()

            (event.description.markdownLinks() ?: "").let {
                if (it != ui.description.tag) {
                    ui.description.tag = it
                    ui.description.loadMarkdown(it)
                }
            }

            ui.time.text = getString(R.string.event_weekday_from_to, db.eventStart(event).dayOfWeek().asText, event.startTimeString(), event.endTimeString())
            ui.organizers.text = event.ownerString()
            ui.room.text = getString(R.string.misc_room_number, conferenceRoom?.name ?: getString(R.string.event_unable_to_locate_room))

            (event.posterImageId ?: event.bannerImageId).let {
                if (it != ui.image.tag) {
                    ui.image.tag = it
                    if (it != null) {
                        ui.image.visibility = View.VISIBLE
                        imageService.load(db.images[it], ui.image)
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

            ui.buttonSave.setOnClickListener {
                if (AppPreferences.dialogOnEventPress) {
                    showDialog(event)
                } else {
                    favoriteEvent(event)
                }
            }

            ui.buttonSave.setOnLongClickListener {
                if (AppPreferences.dialogOnEventPress) {
                    favoriteEvent(event)
                } else {
                    showDialog(event)
                }
                true
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
            if (it != ui.buttonSave.tag) {
                ui.buttonSave.tag = it
                if (it)
                    ui.buttonSave.setImageDrawable(IconDrawable(context, FontAwesomeIcons.fa_heart))
                else
                    ui.buttonSave.setImageDrawable(IconDrawable(context, FontAwesomeIcons.fa_heart_o))
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

    lateinit var buttonSave: FloatingActionButton

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

            buttonSave = floatingActionButton {
                imageResource = R.drawable.icon_menu
            }.lparams {
                anchorGravity = Gravity.BOTTOM or Gravity.END
                anchorId = R.id.event_splitter
                margin = dip(16)
            }
        }
    }

}