package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.model.KnowledgeEntryRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.jsonObjects


/**
 * Views an info based on an ID passed to the intent
 */
class FragmentViewInfo() : Fragment(), HasDb {
    override val db by lazyLocateDb()

    /**
     * Constructs the info view with an assigned bundle
     */
    constructor(knowledgeEntry: KnowledgeEntryRecord) : this() {
        arguments = Bundle()
        arguments.jsonObjects["knowledgeEntry"] = knowledgeEntry
    }

    // Views
    val image: ImageView by view()
    val title: TextView by view()
    val text: TextView by view()
    val layout: LinearLayout by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_info, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.screen(activity, "Info Specific")


        applyOnRoot { changeTitle("Information") }
        // Get info if it exists
        if ("knowledgeEntry" in arguments) {
            val knowledgeEntry: KnowledgeEntryRecord = arguments.jsonObjects["knowledgeEntry"]

            Analytics.event(Analytics.Category.INFO, Analytics.Action.OPENED, knowledgeEntry.title)

            // Set the properties of the view
            title.text = knowledgeEntry.title
            text.text = Formatter.wikiToMarkdown(knowledgeEntry.text)
            image.scaleType = ImageView.ScaleType.CENTER_CROP

            /* TODO
            if (knowledgeEntry.imageIds.isNotEmpty()) {
                imageService.load(database.imageDb[UUID.fromString(knowledgeEntry.imageIds.first())], image, showHide = false)
            } else {
                image.visibility = View.GONE
            }

            for (url in knowledgeEntry.urls) {
                val button = Button(context)
                button.text = url.text
                button.setOnClickListener {
                    val target = if (url.target.contains("http")) url.target else "http://${url.target}"
                    Analytics.event(Analytics.Category.INFO, Analytics.Action.LINK_CLICKED, target)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
                }

                layout.addView(button)
            }
            */
        }
    }
}