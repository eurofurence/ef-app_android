package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.MapRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.findLinkFragment
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.browse
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
            ui.createView(AnkoContext.create(container!!.context.applicationContext, container))

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

            ui.name.text = dealer.getName()
            ui.shortDescription.text = dealer.shortDescription

            ui.aboutArtist.loadMarkdown(
                    if (dealer.aboutTheArtistText.isNotEmpty())
                        dealer.aboutTheArtistText
                    else
                        "This artist did not supply any artist description to show to you :("
            )

            if (dealer.artPreviewImageId == null) {
                ui.artPreview.visibility = View.GONE

                if (dealer.artPreviewCaption.isEmpty()) {
                    ui.artPreviewContainer.visibility = View.GONE
                }
            }

            if (dealer.aboutTheArtText.isNotEmpty()) {
                ui.aboutArt.loadMarkdown(dealer.aboutTheArtText)
            } else {
                ui.aboutArtContainer.visibility = View.GONE
            }

            configureLinks(dealer)
            configureMap(dealer)
        }
    }

    private fun configureMap(dealer: DealerRecord) {
        info { "Finding dealer in mapEntries" }
        val entryMap = db.findLinkFragment(dealer.id.toString())

        val map = entryMap["map"] as MapRecord?
        val entry = entryMap["entry"] as MapEntryRecord?

        if (map != null && entry != null) {
            info { "Found maps and entries!" }
            info { "Map name is ${map.description}" }
            info { "Entry is at (${entry.x}, ${entry.y})" }
            imageService.load(db.toImage(map), ui.map)

            ui.map.attacher.setScale(4F, entry.x.toFloat(), entry.y.toFloat(), true)
            ui.map.attacher.update()
        } else {
            warn { "No map or entry found!" }
            ui.map.visibility = View.GONE
        }

        ui.map.visibility = View.GONE
    }

    private fun configureLinks(dealer: DealerRecord) {
        info { "Setting up external links" }

        if (dealer.links != null || !dealer.telegramHandle.isNullOrEmpty() || !dealer.twitterHandle.isNullOrEmpty()) {
            ui.linkLayout.visibility = View.VISIBLE
        }

        if (dealer.links != null && !dealer.links.isEmpty()) {
            ui.websites.visibility = View.VISIBLE
            dealer.links.forEach {
                val button = Button(context).apply {
                    info { "Adding button for $it" }
                    text = it.target
                    visibility = View.VISIBLE
                    setOnTouchListener { _, _ -> browse(it.target) }
                }

                ui.websites.addView(button)
            }
        }

        if (!dealer.telegramHandle.isNullOrEmpty()) {
            info { "Setting up telegram button for ${dealer.telegramHandle}" }
            ui.telegram.apply {
                text = "${dealer.telegramHandle} on Telegram"
                visibility = View.VISIBLE
                setOnClickListener { browse("https://telegram.me/${dealer.telegramHandle}") }
            }
        }

        if (!dealer.twitterHandle.isNullOrEmpty()) {
            info { "Setting up twitter handle" }
            ui.twitter.apply {
                text = "${dealer.twitterHandle} on {fa-twitter}"
                visibility = View.VISIBLE
                setOnClickListener { browse("https://twitter.com/${dealer.twitterHandle}") }
            }
        }
    }

    class DealerUi : AnkoComponent<ViewGroup> {
        lateinit var primaryImage: PhotoView
        lateinit var name: TextView
        lateinit var shortDescription: TextView
        lateinit var aboutArtist: MarkdownView
        lateinit var aboutArt: MarkdownView
        lateinit var aboutArtContainer: LinearLayout
        lateinit var artPreview: PhotoView
        lateinit var artPreviewCaption: TextView
        lateinit var artPreviewContainer: LinearLayout
        lateinit var linkLayout: LinearLayout
        lateinit var websites: LinearLayout
        lateinit var twitter: Button
        lateinit var telegram: Button
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
                            setPadding(dip(25), dip(20), dip(25), dip(0))
                            setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)
                        }

                        aboutArtist = markdownView {
                            lparams {
                                margin = dip(15)
                            }
                        }

                        artPreviewContainer = verticalLayout {
                            padding = dip(15)
                            backgroundResource = R.color.primary

                            artPreview = photoView {
                                lparams(matchParent, dip(400))
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            }

                            artPreviewCaption = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                padding = dip(15)
                                setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Medium_Inverse)
                            }
                        }

                        aboutArtContainer = verticalLayout {
                            lparams(matchParent, wrapContent)

                            textView("About the Art") {
                                setPadding(dip(25), dip(20), dip(25), dip(0))
                                setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)
                            }
                            aboutArt = markdownView {
                                lparams {
                                    margin = dip(15)
                                }
                            }
                        }

                        linkLayout = verticalLayout {
                            visibility = View.GONE
                            textView("Find out more") {
                                setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Medium)
                            }

                            websites = verticalLayout {
                                visibility = View.GONE
                            }.lparams(matchParent, wrapContent)

                            twitter = fontAwesomeButton {
                                lparams(matchParent, wrapContent)
                                visibility = View.GONE
                            }.applyRecursively { R.style.Widget_AppCompat_Button_Colored }

                            telegram = fontAwesomeButton {
                                lparams(matchParent, wrapContent)
                                visibility = View.GONE
                            }.applyRecursively { R.style.Widget_AppCompat_Button_Colored }
                        }.lparams(matchParent, wrapContent) { margin = dip(15) }

                        map = photoView {
                            backgroundResource = R.color.cardview_dark_background
                            minimumScale = 1F
                            mediumScale = 2.5F
                            maximumScale = 5F
                            lparams(matchParent, dip(400))
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            imageResource = R.drawable.placeholder_event
                        }
                    }.lparams(matchParent, wrapContent)
                }
            }
        }
    }
}