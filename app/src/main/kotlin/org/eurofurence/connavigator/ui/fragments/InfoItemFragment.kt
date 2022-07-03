package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import io.noties.markwon.Markwon
import io.swagger.client.model.KnowledgeEntryRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.dropins.fa.FaView
import org.eurofurence.connavigator.util.extensions.*

import java.util.*


/**
 * Views an info based on an ID passed to the intent
 */
class InfoItemFragment : DisposingFragment(), HasDb {
    private val args: InfoItemFragmentArgs by navArgs()
    private val infoId: UUID? by lazy { UUID.fromString(args.itemId) }
    override val db by lazyLocateDb()
    lateinit var uiLayout: LinearLayout
    lateinit var uiImage: PhotoView
    lateinit var uiTitle: TextView
    lateinit var uiText: TextView
    lateinit var uiIcon: FaView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        scrollView {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            verticalLayout {

                linearLayout {
                    weightSum = 10F

                    uiIcon = fontAwesomeView {
                        layoutParams = linearLayoutParams(dip(0), matchParent, 2f)
                        textColorResource = R.color.primary
                        textSize = 24f
                        gravity = Gravity.CENTER
                    }

                    uiTitle = textView {
                        layoutParams = linearLayoutParams(dip(0), wrapContent, 8f)
                        textResource = R.string.misc_title
                        padding = dip(15)
                        compatAppearance = android.R.style.TextAppearance_Large
                    }
                }

                uiImage = photoView {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    adjustViewBounds = true
                }

                linearLayout {
                    padding = dip(15)
                    backgroundResource = R.color.lightBackground

                    uiText = markdownView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                    }
                }

                uiLayout = verticalLayout {
                    padding = dip(15)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillUi()
        db.subscribe { fillUi() }.collectOnDestroyView()
    }

    private fun fillUi() {
        if (infoId != null) {
            val knowledgeEntry: KnowledgeEntryRecord = db.knowledgeEntries[infoId] ?: return
            val knowledgeGroup = db.knowledgeGroups[knowledgeEntry.knowledgeGroupId] ?: return

            AnalyticsService.event(
                AnalyticsService.Category.INFO,
                AnalyticsService.Action.OPENED,
                knowledgeEntry.title
            )

            if (knowledgeEntry.imageIds != null && knowledgeEntry.imageIds?.isNotEmpty() == true) {
                ImageService.load(
                    db.images[knowledgeEntry.imageIds?.first()],
                    uiImage,
                    hideIfNull = false
                )
            } else {
                uiImage.visibility = View.GONE
            }

            // Set the properties of the view
            uiTitle.text = knowledgeEntry.title
            uiText.text = Markwon
                .create(requireContext())
                .toMarkdown(knowledgeEntry.text)
            uiIcon.text = knowledgeGroup.fontAwesomeIconCharacterUnicodeAddress?.toUnicode() ?: ""

            uiLayout.removeAllViewsInLayout()
            for (url in knowledgeEntry.links.orEmpty()) {
                val button = Button(context)
                button.text = url.name
                button.setOnClickListener {
                    AnalyticsService.event(
                        AnalyticsService.Category.INFO,
                        AnalyticsService.Action.LINK_CLICKED,
                        url.target
                    )
                    requireContext().browse(url.target)
                }

                uiLayout.addView(button)
            }
        }
    }
}
