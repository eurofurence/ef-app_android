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
import io.reactivex.disposables.Disposables
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.EventFavoriteBroadcast
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.eventStart
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.tracking.Analytics.Action
import org.eurofurence.connavigator.tracking.Analytics.Category
import org.eurofurence.connavigator.ui.dialogs.eventDialog
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.get
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Created by David on 4/9/2016.
 */
class FragmentViewEvent : Fragment(), HasDb {
    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()

    val eventId: UUID? get() = UUID.fromString(arguments.getString("id"))

    val title: TextView by view()
    val description: MarkdownView by view()
    val image: ImageView by view()
    val organizers: TextView by view()
    val room: TextView by view()
    val time: TextView by view()
    val buttonSave: FloatingActionButton by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_event, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.screen(activity, "Event Specific")
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
        if ("id" in arguments) {
            val event: EventRecord = db.events[eventId] ?: return

            Analytics.event(Category.EVENT, Action.OPENED, event.title)

            val conferenceRoom = event[toRoom]
            val conferenceDay = event[toDay]

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

    private fun showDialog(event: EventRecord) {
        eventDialog(context, event, db)
    }

    private fun favoriteEvent(event: EventRecord) {
        context.sendBroadcast(IntentFor<EventFavoriteBroadcast>(context).apply { jsonObjects["event"] = event })
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