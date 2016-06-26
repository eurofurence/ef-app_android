package org.eurofurence.connavigator.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
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
    val dealerArtistDescription by view(MarkdownView::class.java)
    val dealerArtDescription by view(MarkdownView::class.java)
    val dealerImage by view(ImageView::class.java)
    val dealerButtonMore by view(FloatingActionButton::class.java)
    val dealerPreviewArtImage by view(ImageView::class.java)
    val dealerPreviewCaption by view(TextView::class.java)
    var isFullscreen = false

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_dealer, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Dealer Details")

        Analytics.trackEvent(Analytics.Category.DEALER, Analytics.Action.OPENED, dealer.displayName ?: dealer.attendeeNickname)

        val image = database.imageDb[dealer.artistImageId]

        if (image != null) {
            imageService.load(image, dealerImage, false)
            imageService.resizeFor(image, dealerImage)
        } else {
            dealerImage.setImageDrawable(ContextCompat.getDrawable(database.context, R.drawable.dealer_black))
        }

        imageService.load(database.imageDb[dealer.artPreviewImageId], dealerPreviewArtImage)

        dealerPreviewCaption.text = dealer.artPreviewCaption

        dealerName.text = Formatter.dealerName(dealer)
        dealerShortDescription.text = dealer.shortDescription

        dealerArtistDescription.loadMarkdown(dealer.aboutTheArtistText)
        dealerArtDescription.loadMarkdown(dealer.aboutTheArtText)

        dealerButtonMore.setOnClickListener {
            try {
                if (dealer.websiteUri.startsWith("http")) {
                    Analytics.trackEvent(Analytics.Category.DEALER, Analytics.Action.LINK_CLICKED, dealer.displayName ?: dealer.attendeeNickname)

                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(dealer.websiteUri)))
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://" + dealer.websiteUri)))
                }
            } catch(e: Exception) {
                logv { "User tried clicking on a dealer with no url" }
            }
        }

        if (dealer.websiteUri.isEmpty())
            dealerButtonMore.visibility = View.GONE

        if (dealer.aboutTheArtText.isEmpty())
            dealerArtistDescription.loadMarkdown("This artist did not supply any artist description to show to you :(")

        if (dealer.aboutTheArtistText.isEmpty())
            dealerArtDescription.loadMarkdown("this artist did not supply any art descriptions to show to you  :V")

        if (dealer.shortDescription.isEmpty())
            dealerShortDescription.visibility = View.GONE
    }
}