package org.eurofurence.connavigator.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.Dealer
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.letRoot
import org.eurofurence.connavigator.util.extensions.logv
import us.feras.mdv.MarkdownView

/**
 * Created by David on 16-5-2016.
 */
class FragmentViewDealer(val dealer: Dealer) : Fragment() {
    val dealerName by view(TextView::class.java)
    val dealerShortDescription by view(TextView::class.java)
    val dealerFullDescription by view(MarkdownView::class.java)
    val dealerImage by view(ImageView::class.java)
    val dealerButtonMore by view(FloatingActionButton::class.java)

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_dealer, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Dealer Details")

        dealerName.text = Formatter.dealerName(dealer)
        dealerShortDescription.text = dealer.shortDescription
        dealerFullDescription.loadMarkdown(dealer.aboutTheArtistText + " \r" + dealer.aboutTheArtText)
        imageService.load(database.imageDb[dealer.artPreviewImageId], dealerImage, false)

        dealerButtonMore.setOnClickListener {
            try {
                if (dealer.websiteUri.startsWith("http"))
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(dealer.websiteUri)))
                else
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://" + dealer.websiteUri)))
            } catch(e: Exception) {
                logv { "User tried clicking on a dealer with no url" }
            }
        }

        if (dealer.websiteUri.isEmpty())
            dealerButtonMore.visibility = View.GONE

        if (dealer.aboutTheArtText.isEmpty() && dealer.aboutTheArtistText.isEmpty())
            dealerFullDescription.loadMarkdown("This artist did not supply any long descriptions")

        if (dealer.shortDescription.isEmpty())
            dealerShortDescription.visibility = View.GONE
    }
}