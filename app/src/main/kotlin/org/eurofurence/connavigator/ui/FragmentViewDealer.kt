package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.Formatter
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.photoView
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*
import us.feras.mdv.MarkdownView

/**
 * Created by David on 16-5-2016.
 */
class FragmentViewDealer() : Fragment(), ContentAPI, HasDb, AnkoLogger {
    constructor(dealer: DealerRecord) : this() {
        arguments = Bundle()

        arguments.jsonObjects["dealer"] = dealer
    }

    val ui by lazy { DealerUi() }

    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.Companion.create(container!!.context, container))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Send analytics pings
        Analytics.screen(activity, "View Dealer Details")

        if ("dealer" in arguments) {
            val dealer: DealerRecord = arguments.jsonObjects["dealer"]

            Analytics.event(Analytics.Category.DEALER, Analytics.Action.OPENED, dealer.displayName ?: dealer.attendeeNickname)

            // Retrieve top image
            val image = dealer[toArtistImage]

            // Set image on top
            if (image != null) {
                imageService.load(image, ui.primaryImage, false)
            } else {
                ui.primaryImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.dealer_white_full))
            }

            // Load art preview image
            imageService.load(dealer[toPreview], ui.artPreview)

            ui.artPreviewCaption.text = dealer.artPreviewCaption

            ui.name.text = Formatter.dealerName(dealer)
            ui.shortDescription.text = dealer.shortDescription

            ui.aboutArtist.loadMarkdown(
                    if (dealer.aboutTheArtistText.isNotEmpty())
                        dealer.aboutTheArtistText
                    else
                        "This artist did not supply any artist description to show to you :("
            )

            ui.aboutArt.loadMarkdown(
                    if (dealer.aboutTheArtText.isNotEmpty())
                        dealer.aboutTheArtText
                    else
                        "this artist did not supply any art descriptions to show to you :("
            )

            ui.map.setScale(2.5F, true)
        }
    }
}

class DealerUi : AnkoComponent<ViewGroup> {
    lateinit var primaryImage: PhotoView
    lateinit var name: TextView
    lateinit var shortDescription: TextView
    lateinit var aboutArtist: MarkdownView
    lateinit var aboutArt: MarkdownView
    lateinit var artPreview: PhotoView
    lateinit var artPreviewCaption: TextView
    lateinit var map: PhotoView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            backgroundResource = R.color.cardview_light_background
            scrollView {
                verticalLayout {
                    lparams(matchParent, matchParent)

                    primaryImage = photoView {
                        lparams(matchParent, dip(300))
                        backgroundResource = R.drawable.image_fade
                    }

                    verticalLayout {
                        lparams(matchParent, wrapContent)
                        backgroundResource = R.color.primaryDarker
                        padding = dip(15)

                        name = textView {
                            text = "Dealer Name"
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            setTextAppearance(ctx, android.R.style.TextAppearance_Large_Inverse)

                            padding = dip(10)
                        }

                        shortDescription = textView {
                            text = "Short description"
                            padding = dip(10)

                            setTextAppearance(ctx, android.R.style.TextAppearance_Medium_Inverse)

                        }
                    }

                    textView {
                        text = "About the Artist"
                        padding = dip(15)
                        setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)

                    }

                    aboutArtist = markdownView { }

                    artPreview = photoView {
                        lparams(matchParent, dip(400))
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        backgroundResource = R.color.primary
                    }

                    artPreviewCaption = textView {
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        padding = dip(15)
                        backgroundResource = R.color.primary
                        setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Medium_Inverse)

                    }

                    textView("About the Art") {
                        padding = dip(15)
                        setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)
                    }
                    aboutArt = markdownView { }

                    map = photoView {
                        lparams(matchParent, dip(200))
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        imageResource = R.drawable.placeholder_event
                    }
                }
            }
        }
    }
}