package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.util.extensions.fontAwesomeTextView
import org.eurofurence.connavigator.util.extensions.jodatime
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.photoView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.toast
import org.joda.time.DateTime
import us.feras.mdv.MarkdownView
import java.util.*

class AnnouncementItemFragment : DisposingFragment(), HasDb, AnkoLogger {
    private val args: AnnouncementItemFragmentArgs by navArgs()
    private val announcementId by lazy { UUID.fromString(args.announcementId) }

    override val db by lazyLocateDb()

    val ui = AnnouncementItemUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info { "Created announcement view for $announcementId" }

        if (announcementId == null) {
            toast("No announcement ID was sent!")
            findNavController().popBackStack()
        }

        db.subscribe {
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
                    ImageService.load(image, ui.image, true)
                } else {
                    ui.image.visibility = View.GONE
                }

                ui.warningLayout.visibility = if (DateTime.now().isAfter(announcement.validUntilDateTimeUtc.jodatime()))
                    View.VISIBLE
                else
                    View.GONE
            }
        }
        .collectOnDestroyView()
    }
}

class AnnouncementItemUi : AnkoComponent<Fragment> {
    lateinit var title: TextView
    lateinit var text: MarkdownView
    lateinit var image: PhotoView
    lateinit var loadingIndicator: ProgressBar
    lateinit var warningLayout: LinearLayout
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        relativeLayout {
            backgroundResource = R.color.backgroundGrey
            lparams(matchParent, matchParent)
            scrollView {
                lparams(matchParent, matchParent)

                verticalLayout {
                    backgroundResource = R.color.lightBackground
                    isClickable = true

                    loadingIndicator = progressBar()

                    linearLayout {
                        padding = dip(50)
                        backgroundResource = R.drawable.image_fade
                        title = textView {
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large_Inverse
                        }
                    }

                    warningLayout = linearLayout {
                        padding = dip(20 - 8)

                        fontAwesomeTextView {
                            text = "This announcement has expired!"
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            textAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        }.lparams(matchParent, wrapContent)
                    }

                    text = markdownView {

                    }.lparams(matchParent, wrapContent) {
                        margin = dip(20 - 8)
                    }

                    image = photoView {
                        backgroundResource = R.color.darkBackground
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