package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.text.TextUtilsCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import io.swagger.client.model.KnowledgeEntryRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.v2.compatAppearance
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

    /**
     * Constructs the info view with an assigned bundle
     */
    constructor(knowledgeEntry: KnowledgeEntryRecord) : this() {
        arguments = Bundle()
        arguments.jsonObjects["knowledgeEntry"] = knowledgeEntry
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get info if it exists
        if ("knowledgeEntry" in arguments) {
            val knowledgeEntry: KnowledgeEntryRecord = arguments.jsonObjects["knowledgeEntry"]

            Analytics.event(Analytics.Category.INFO, Analytics.Action.OPENED, knowledgeEntry.title)

            // Set the properties of the view
            ui.title.text = knowledgeEntry.title
            ui.text.loadMarkdown(knowledgeEntry.text)

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
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            verticalLayout {

                linearLayout {
                    weightSum = 10F
                    padding = dip(15)

                    view {}.lparams(dip(0), matchParent) {
                        weight = 2F
                    }

                    title = textView("Title") {
                        compatAppearance = android.R.style.TextAppearance_Large
                    }.lparams(dip(0), wrapContent) {
                        weight = 8F
                    }
                }

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