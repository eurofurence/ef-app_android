package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.github.chrisbanes.photoview.PhotoView
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.photoView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import us.feras.mdv.MarkdownView
import java.util.*

class AnnouncementFragment : Fragment(), HasDb, AnkoLogger {
    val ui = AnnouncementItemUi()
    private val announcementId
        get () = try {
            UUID.fromString(AnnouncementFragmentArgs.fromBundle(arguments).announcementId)
        } catch (_: Exception) {
            null
        }

    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info { "Created announcement view for $announcementId" }

        if(announcementId == null){
            toast("No announcement ID was sent!")
            findNavController().popBackStack()
        }

        subscriptions += db.subscribe {
            val announcement = it.announcements[announcementId]
            if (announcement != null) {
                info { "Updating announcement item" }

                ui.loadingIndicator.visibility = View.GONE

                ui.title.text = announcement.title
                ui.text.loadMarkdown(announcement.content)

                // Retrieve top image
                val image = announcement.let { it[toAnnouncementImage] }

                // Set image on top
                if (image != null) {
                    imageService.load(image, ui.image, true)
                } else {
                    ui.image.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }
}

class AnnouncementItemUi : AnkoComponent<Fragment> {
    lateinit var title: TextView
    lateinit var text: MarkdownView
    lateinit var image: PhotoView
    lateinit var loadingIndicator: ProgressBar
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        relativeLayout {
            backgroundResource = R.color.backgroundGrey
            lparams(matchParent, matchParent)
            scrollView {
                lparams(matchParent, matchParent)

                verticalLayout {
                    backgroundResource = R.color.cardview_light_background
                    isClickable = true

                    loadingIndicator = progressBar()

                    linearLayout {
                        padding = dip(50)
                        backgroundResource = R.drawable.image_fade
                        title = textView {
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large_Inverse
                        }
                    }

                    text = markdownView {

                    }.lparams(matchParent, wrapContent) {
                        margin = dip(20 - 8)
                    }

                    image = photoView {
                        backgroundResource = R.color.cardview_dark_background
                        imageResource = R.drawable.placeholder_event
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        adjustViewBounds = true
                        visibility = View.GONE
                    }.lparams(matchParent, wrapContent) {
                        margin = dip(20)
                    }
                }.lparams(matchParent, wrapContent)
            }
        }
    }

}