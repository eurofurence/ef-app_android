package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.pawegio.kandroid.IntentFor
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.DataChanged
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Created by David on 4/9/2016.
 */
class FragmentViewEvent() : Fragment(), HasDb {
    companion object {
        val EVENT_STATUS_CHANGED = "org.eurofurence.connavigator.ui.EVENT_STATUS_CHANGED"

        /**
         * Constructs the info view with an assigned bundle
         */
        fun onEvent(event: EventRecord) = FragmentViewEvent().apply {
            arguments = Bundle().apply {
                jsonObjects["event"] = event
            }
        }
    }

    override val db by lazyLocateDb()
    val dataChanged by lazy {
        requireContext().localReceiver(DataChanged.DATACHANGED) {
            changeFabIcon()
        }
    }

    lateinit var eventId: UUID


    val title: TextView by view()
    val description: MarkdownView by view()
    val image: ImageView by view()
    val organizers: TextView by view()
    val room: TextView by view()
    val time: TextView by view()
    val buttonSave: FloatingActionButton by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_event, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.screen(requireActivity(), "Event Specific")

        arguments?.let {
            if ("event" in it) {
                val event: EventRecord = it.jsonObjects["event"]

                eventId = event.id

                dataChanged.register()

                Analytics.event(Analytics.Category.EVENT, Analytics.Action.OPENED, event.title)

                val conferenceRoom = event[toRoom]

                title.text = event.fullTitle()

                description.loadMarkdown(event.description.markdownLinks())

                time.text = "${db.eventStart(event).dayOfWeek().asText} from ${event.startTimeString()} to ${event.endTimeString()}"
                organizers.text = event.ownerString()
                room.text = conferenceRoom!!.name

                if (event.posterImageId !== null) {
                    imageService.load(db.images[event.posterImageId], image)
                } else if (event.bannerImageId !== null) {
                    imageService.load(db.images[event.bannerImageId], image)
                } else {
                    image.visibility = View.GONE
                }

                changeFabIcon()

                buttonSave.setOnClickListener {
                    if (AppPreferences.dialogOnEventPress) {
                        showDialog(event)
                    } else {
                        favoriteEvent(event)
                    }
                }

                buttonSave.setOnLongClickListener {
                    if (AppPreferences.dialogOnEventPress) {
                        favoriteEvent(event)
                    } else {
                        showDialog(event)
                    }
                    true
                }
            }
        }
    }

    private fun showDialog(event: EventRecord) {
        context?.let { context ->
            eventDialog(context, event, db)
        } ?: error("Fragment not initialized")
    }

    private fun favoriteEvent(event: EventRecord) {
        context?.let { context ->
            context.sendBroadcast(IntentFor<EventFavoriteBroadcast>(context).apply { jsonObjects["event"] = event })
        } ?: error("Fragment not initialized")
    }

    /**
     * Changes the FAB based on if the current event is liked or not
     */
    private fun changeFabIcon() {
        if (eventId in db.faves)
            buttonSave.setImageDrawable(IconDrawable(context, FontAwesomeIcons.fa_heart))
        else
            buttonSave.setImageDrawable(IconDrawable(context, FontAwesomeIcons.fa_heart_o))
    }
}

class EventUi : AnkoComponent<ViewGroup> {
    lateinit var poster: PhotoView
    lateinit var title: TextView
    lateinit var room: TextView
    lateinit var host: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.cardview_light_background

            scrollView {
                verticalLayout {
                    poster = photoView {
                        backgroundResource = R.drawable.image_fade
                        imageResource = R.drawable.placeholder_event
                    }.lparams(matchParent, wrapContent)

                    verticalLayout {
                        padding = dip(15)
                        id = R.id.splitter
                    }
                }.lparams(matchParent, wrapContent)
            }

            floatingActionButton {

            }.lparams(wrapContent, wrapContent) {
                margin = dip(16)
                alignEnd(R.id.splitter)
            }
        }
    }

}