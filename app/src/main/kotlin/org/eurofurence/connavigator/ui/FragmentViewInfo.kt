package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import io.reactivex.disposables.Disposables
import io.swagger.client.model.KnowledgeEntryRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.views.FontAwesomeTextView
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.browse
import us.feras.mdv.MarkdownView


/**
 * Views an info based on an ID passed to the intent
 */
class FragmentViewInfo() : Fragment(), HasDb {
    override val db by lazyLocateDb()

    val ui by lazy { InfoUi() }

    var subscriptions = Disposables.empty()

    /**
     * Constructs the info view with an assigned bundle
     */
    fun withArguments(knowledgeEntry: KnowledgeEntryRecord) = apply {
        arguments = Bundle().apply {
            jsonObjects["knowledgeEntry"] = knowledgeEntry
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscriptions += db.subscribe { fillUi() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    private fun fillUi() {
        if ("knowledgeEntry" in arguments) {
            val knowledgeEntry: KnowledgeEntryRecord = arguments.jsonObjects["knowledgeEntry"]
            val knowledgeGroup = db.knowledgeGroups[knowledgeEntry.knowledgeGroupId!!]!!

            Analytics.event(Analytics.Category.INFO, Analytics.Action.OPENED, knowledgeEntry.title)

            if (knowledgeEntry.imageIds != null && knowledgeEntry.imageIds.isNotEmpty()) {
                imageService.load(db.images[knowledgeEntry.imageIds.first()], ui.image, showHide = false)
            } else {
                ui.image.visibility = View.GONE
            }

            // Set the properties of the view
            ui.title.text = knowledgeEntry.title
            ui.text.loadMarkdown(knowledgeEntry.text)
            ui.icon.text = knowledgeGroup.fontAwesomeIconCharacterUnicodeAddress.toUnicode()

            for (url in knowledgeEntry.links) {
                val button = Button(context)
                button.text = url.name
                button.setOnClickListener {
                    Analytics.event(Analytics.Category.INFO, Analytics.Action.LINK_CLICKED, url.target)
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

                    title = textView("Title") {
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
                    backgroundResource = R.color.cardview_light_background

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