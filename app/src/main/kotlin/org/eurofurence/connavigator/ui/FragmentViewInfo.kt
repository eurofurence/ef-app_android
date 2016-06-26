package org.eurofurence.connavigator.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.model.Info
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import us.feras.mdv.MarkdownView
import java.util.*

/**
 * Views an info based on an ID passed to the intent
 */
class FragmentViewInfo() : Fragment() {
    /**
     * Constructs the info view with an assigned bundle
     */
    constructor(info: Info) : this() {
        arguments = Bundle()
        arguments.jsonObjects["info"] = info
    }

    // Views
    val image by view(ImageView::class.java)
    val title by view(TextView::class.java)
    val text by view(MarkdownView::class.java)
    val layout by view(LinearLayout::class.java)

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_info, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Analytics.changeScreenName("Info Specific")


        applyOnRoot { changeTitle("Information") }
        // Get info if it exists
        if ("info" in arguments) {
            val info = arguments.jsonObjects["info", Info::class.java]

            Analytics.trackEvent(Analytics.Category.INFO, Analytics.Action.OPENED, info.title)

            // Set the properties of the view
            title.text = info.title
            text.loadMarkdown(info.text)
            image.scaleType = ImageView.ScaleType.CENTER_CROP

            if (info.imageIds.isNotEmpty()) {
                imageService.load(database.imageDb[UUID.fromString(info.imageIds.first())], image, showHide = false)
            } else {
                image.visibility = View.GONE
            }

            for (url in info.urls) {
                val button = Button(context)
                button.text = url.text
                button.setOnClickListener {
                    Analytics.trackEvent(Analytics.Category.INFO, Analytics.Action.LINK_CLICKED, url.target)
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url.target)))
                }

                layout.addView(button)
            }
        }
    }
}