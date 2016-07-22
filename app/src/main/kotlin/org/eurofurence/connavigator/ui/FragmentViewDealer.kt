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
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import us.feras.mdv.MarkdownView

/**
 * Created by David on 16-5-2016.
 */
class FragmentViewDealer() : Fragment(), ContentAPI {
    constructor(dealer: Dealer) : this() {
        arguments = Bundle()

        arguments.jsonObjects["dealer"] = dealer
    }

    val dealerName by view(TextView::class.java)
    val dealerShortDescription by view(TextView::class.java)
    val dealerArtistDescription by view(MarkdownView::class.java)
    val dealerArtDescription by view(MarkdownView::class.java)
    val dealerImage by view(ImageView::class.java)
    val dealerButtonMore by view(FloatingActionButton::class.java)
    val dealerPreviewArtImage by view(ImageView::class.java)
    val dealerPreviewCaption by view(TextView::class.java)
    val dealerMap by view(ImageView::class.java)

    var isFullscreen = false

    val database: Database get() = letRoot { it.database }!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_dealer, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Send analytics pings
        Analytics.screen("View Dealer Details")

        if ("dealer" in arguments) {
            val dealer = arguments.jsonObjects["dealer", Dealer::class.java]

            Analytics.event(Analytics.Category.DEALER, Analytics.Action.OPENED, dealer.displayName ?: dealer.attendeeNickname)

            // Retrieve top image
            val image = database.imageDb[dealer.artistImageId]

            // Load the map
            val mapEntry = database.mapEntryDb.items.find { it.targetId == dealer.id }

            val mapImage = database.imageDb[database.mapEntityDb[mapEntry?.mapId]?.imageId]

            imageService.load(mapImage, dealerMap)

            // Set image on top
            if (image != null) {
                imageService.load(image, dealerImage, false)
                imageService.resizeFor(image, dealerImage)
            } else {
                dealerImage.setImageDrawable(ContextCompat.getDrawable(database.context, R.drawable.dealer_black))
            }

            // Load art preview image
            imageService.load(database.imageDb[dealer.artPreviewImageId], dealerPreviewArtImage)

            dealerPreviewCaption.text = dealer.artPreviewCaption

            dealerName.text = Formatter.dealerName(dealer)
            dealerShortDescription.text = dealer.shortDescription

            dealerArtistDescription.loadMarkdown(dealer.aboutTheArtistText)
            dealerArtDescription.loadMarkdown(dealer.aboutTheArtText)

            // Handle the FAB that links out
            dealerButtonMore.setOnClickListener {
                try {
                    if (dealer.websiteUri.startsWith("http")) {
                        Analytics.event(Analytics.Category.DEALER, Analytics.Action.LINK_CLICKED, dealer.displayName ?: dealer.attendeeNickname)

                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(dealer.websiteUri)))
                    } else {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://" + dealer.websiteUri)))
                    }
                } catch(e: Exception) {
                    Analytics.exception(e)
                    logv { "User tried clicking on a dealer with no url" }
                }
            }

            // Load empty texts
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

}