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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import io.reactivex.disposables.Disposables
import io.swagger.client.model.KnowledgeEntryRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.ui.views.FontAwesomeTextView
import org.eurofurence.connavigator.util.extensions.fontAwesomeTextView
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.photoView
import org.eurofurence.connavigator.util.extensions.toUnicode
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.browse
import us.feras.mdv.MarkdownView
import java.util.*


/**
 * Views an info based on an ID passed to the intent
 */
class InfoItemFragment : DisposingFragment(), HasDb {
    val ui by lazy { InfoUi() }
    private val args: InfoItemFragmentArgs by navArgs()
    private val infoId: UUID? by lazy { UUID.fromString(args.itemId) }
    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillUi()
        db.subscribe { fillUi() }.collectOnDestroyView()
    }

    private fun fillUi() {
        if (infoId != null) {
            val knowledgeEntry: KnowledgeEntryRecord = db.knowledgeEntries[infoId] ?: return
            val knowledgeGroup = db.knowledgeGroups[knowledgeEntry.knowledgeGroupId] ?: return

            AnalyticsService.event(AnalyticsService.Category.INFO, AnalyticsService.Action.OPENED, knowledgeEntry.title)

            if (knowledgeEntry.imageIds != null && knowledgeEntry.imageIds?.isNotEmpty() == true) {
                ImageService.load(db.images[knowledgeEntry.imageIds?.first()], ui.image, hideIfNull = false)
            } else {
                ui.image.visibility = View.GONE
            }

            // Set the properties of the view
            ui.title.text = knowledgeEntry.title
            ui.text.loadMarkdown(knowledgeEntry.text)
            ui.icon.text = knowledgeGroup.fontAwesomeIconCharacterUnicodeAddress?.toUnicode() ?: ""

            ui.layout.removeAllViewsInLayout()
            for (url in knowledgeEntry.links.orEmpty()) {
                val button = Button(context)
                button.text = url.name
                button.setOnClickListener {
                    AnalyticsService.event(AnalyticsService.Category.INFO, AnalyticsService.Action.LINK_CLICKED, url.target)
                    browse(url.target)
                }

                ui.layout.addView(button)
            }
        }
    }
}

class InfoUi : AnkoComponent<Fragment> {
    lateinit var layout: LinearLayout
    lateinit var image: PhotoView
    lateinit var title: TextView
    lateinit var text: MarkdownView
    lateinit var icon: FontAwesomeTextView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            verticalLayout {

                linearLayout {
                    weightSum = 10F

                    icon = fontAwesomeTextView {
                        textColor = ContextCompat.getColor(context, R.color.primary)
                        textSize = 24f
                        gravity = Gravity.CENTER
                    }.lparams(dip(0), matchParent) {
                        weight = 2F
                    }

                    title = textView {
                        textResource = R.string.misc_title
                        padding = dip(15)
                        compatAppearance = android.R.style.TextAppearance_Large
                    }.lparams(dip(0), wrapContent) {
                        weight = 8F
                    }
                }

                image = photoView {
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    adjustViewBounds = true
                }.lparams(matchParent, wrapContent)

                linearLayout {
                    padding = dip(15)
                    backgroundResource = R.color.lightBackground

                    text = markdownView {
                        lparams(matchParent, wrapContent)
                    }
                }

                layout = verticalLayout {
                    padding = dip(15)
                }
            }
        }
    }

}