package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import com.pawegio.kandroid.toast
import io.noties.markwon.Markwon
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.util.extensions.jodatime
import org.eurofurence.connavigator.util.v2.get

import org.joda.time.DateTime
import java.util.*

class AnnouncementItemFragment : DisposingFragment(), HasDb, AnkoLogger {
    private val args: AnnouncementItemFragmentArgs by navArgs()
    private val announcementId by lazy { UUID.fromString(args.announcementId) }

    override val db by lazyLocateDb()

    lateinit var title: TextView
    lateinit var text: TextView // TODO
    lateinit var photo: PhotoView
    lateinit var loadingIndicator: ProgressBar
    lateinit var warningLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        createView {
            relativeLayout {
                backgroundResource= R.color.backgroundGrey
                layoutParams = viewGroupLayoutParams(matchParent, matchParent)

                scrollView {
                    layoutParams = relativeLayoutParams(matchParent, matchParent)

                    verticalLayout {
                        layoutParams = linearLayoutParams(matchParent, matchParent)
                        backgroundResource= R.color.lightBackground
                        isClickable = true

                        loadingIndicator = progressBar {}

                        linearLayout {
                            padding = dip(50)
                            backgroundResource= R.drawable.image_fade
                            title = textView {
                                compatAppearance =
                                    android.R.style.TextAppearance_DeviceDefault_Large_Inverse
                            }
                        }

                        warningLayout = linearLayout {
                            padding = dip(20 - 8)

                            fontAwesomeView {
                                layoutParams = linearLayoutParams(matchParent, wrapContent)
                                text = "This announcement has expired!"
                                textAlignment = View.TEXT_ALIGNMENT_CENTER

                                compatAppearance =
                                    android.R.style.TextAppearance_DeviceDefault_Medium
                            }
                        }

                        text = markdownView {
                            layoutParams = marginLayoutParams(dip(20 - 8), dip(20 - 8))
                        }

                        photo = photoView {
                            layoutParams = marginLayoutParams(dip(20), dip(20))
                            backgroundResource= R.color.darkBackground
                            imageResource=R.drawable.placeholder_event
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            adjustViewBounds = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }

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

                loadingIndicator.visibility = View.GONE

                title.text = announcement.title
                text.text = Markwon
                    .create(requireContext())
                    .toMarkdown(announcement.content)

                // Retrieve top image
                val image = announcement.let { it[toAnnouncementImage] }

                // Set image on top
                if (image != null) {
                    ImageService.load(image, photo, true)
                } else {
                    photo.visibility = View.GONE
                }

                warningLayout.visibility =
                    if (DateTime.now().isAfter(announcement.validUntilDateTimeUtc.jodatime()))
                        View.VISIBLE
                    else
                        View.GONE
            }
        }
            .collectOnDestroyView()
    }
}
