package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import us.feras.mdv.MarkdownView

/**
 * Created by David on 28-4-2016.
 */
class FragmentAbout : Fragment() {
    private val attributations = "Google Maps" +
            "" +
            " Icons8"

    val textVersion by view(TextView::class.java)
    val markdownAttributation by view(MarkdownView::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_about, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View About")

        applyOnRoot { changeTitle("About") }
        textVersion.text = "Version: %s - Build: %s".format(BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE.toString())
        markdownAttributation.loadMarkdown(attributations)
    }
}