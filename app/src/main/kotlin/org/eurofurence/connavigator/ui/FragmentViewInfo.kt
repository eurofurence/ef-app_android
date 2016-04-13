package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.Info
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.letRoot
import us.feras.mdv.MarkdownView

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

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_view_info, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get info if it exists
        if ("info" in arguments) {
            val info = arguments.jsonObjects["info", Info::class.java]

            // Set the properties of the view
            title.text = info.title
            text.loadMarkdown(info.text)
            imageService.load(database.imageDb[info.imageId], image)
        }
    }
}