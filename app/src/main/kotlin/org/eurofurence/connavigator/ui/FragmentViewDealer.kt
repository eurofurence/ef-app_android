package org.eurofurence.connavigator.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
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
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.browse
import java.util.*

class FragmentViewDealer : Fragment(), ContentAPI, HasDb, AnkoLogger {
    val dealerId by lazy {
        arguments?.let { arguments ->
            UUID.fromString(arguments.getString("id"))
        } ?: throw IllegalStateException("Arguments are not initialized.")
    }

    val ui by lazy { DealerUi() }

    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.create(container!!.context.applicationContext, container))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Send analytics pings
        Analytics.screen(requireActivity(), "View Dealer Details")

        arguments?.let { arguments ->
            if ("id" in arguments) {
                val dealer: DealerRecord = db.dealers[dealerId] ?: return

                Analytics.event(Analytics.Category.DEALER, Analytics.Action.OPENED, dealer.displayName
                        ?: dealer.attendeeNickname)

                // Retrieve top image
                val image = dealer[toArtistImage]

                // Set image on top
                if (image != null) {
                    imageService.load(image, ui.primaryImage, false)
                } else {
                    ui.primaryImage.visibility = View.GONE
                }

                // Load art preview image
                imageService.load(dealer[toPreview], ui.artPreview)

                ui.artPreviewCaption.text = dealer.artPreviewCaption

                ui.name.text = dealer.getName()
                ui.nameSecond.apply {
                    text = dealer.attendeeNickname
                    visibility = if (dealer.hasUniqueDisplayName()) View.VISIBLE else View.GONE
                }

                ui.categories.text = dealer.categories.joinToString(", ")
                ui.shortDescription.apply {
                    text = dealer.shortDescription
                    visibility = if (dealer.shortDescription.isNullOrEmpty()) View.GONE else View.VISIBLE
                }

                ui.categories.text = dealer.categories.joinToString(", ")

                ui.aboutArtist.text =
                        if (dealer.aboutTheArtistText.isNotEmpty())
                            dealer.aboutTheArtistText
                        else
                            "This artist did not supply any artist description to show to you :("

                if (dealer.artPreviewImageId == null) {
                    ui.artPreview.visibility = View.GONE

                    if (dealer.artPreviewCaption.isEmpty()) {
                        ui.artPreviewContainer.visibility = View.GONE
                    }
                }

                if (dealer.aboutTheArtText.isNotEmpty()) {
                    ui.aboutArt.text = dealer.aboutTheArtText
                } else {
                    ui.aboutArtContainer.visibility = View.GONE
                }

                configureLinks(dealer)
                configureMap(dealer)
            }
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

        if (dealer.links != null && !dealer.links.isEmpty()) {
            ui.websitesContainer.visibility = View.VISIBLE
            dealer.links.forEach {
                val button = Button(context).apply {
                    info { "Adding button for $it" }
                    text = it.target
                    visibility = View.VISIBLE
                    //layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                    setOnTouchListener { _, _ -> browse(it.target) }
                }

                ui.websites.addView(button)
            }
        }

        if (!dealer.telegramHandle.isNullOrEmpty()) {
            info { "Setting up telegram button for ${dealer.telegramHandle}" }
            ui.telegramButton.apply {
                text = "${dealer.telegramHandle} @ Telegram"
                setOnClickListener { browse("https://telegram.me/${dealer.telegramHandle}") }
            }
            ui.telegramContainer.apply {
                visibility = View.VISIBLE
            }
        }

        if (!dealer.twitterHandle.isNullOrEmpty()) {
            info { "Setting up twitter handle" }
            ui.twitterButton.apply {
                text = "${dealer.twitterHandle} @ Twitter"
                setOnClickListener { browse("https://twitter.com/${dealer.twitterHandle}") }
            }
            ui.twitterContainer.apply {
                visibility = View.VISIBLE
            }
        }
    }
}

class DealerUi : AnkoComponent<ViewGroup> {
    lateinit var primaryImage: PhotoView
    lateinit var name: TextView
    lateinit var nameSecond: TextView
    lateinit var shortDescription: TextView
    lateinit var categories: TextView
    lateinit var aboutArtist: TextView
    lateinit var aboutArt: TextView
    lateinit var aboutArtContainer: LinearLayout
    lateinit var artPreview: PhotoView
    lateinit var artPreviewCaption: TextView
    lateinit var artPreviewContainer: LinearLayout
    lateinit var websites: LinearLayout
    lateinit var websitesContainer: LinearLayout
    lateinit var twitterButton: Button
    lateinit var twitterContainer: LinearLayout
    lateinit var telegramButton: Button
    lateinit var telegramContainer: LinearLayout
    lateinit var map: PhotoView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            backgroundResource = R.color.backgroundGrey
            scrollView {
                verticalLayout {
                    lparams(matchParent, matchParent)

                    verticalLayout {
                        lparams(matchParent, wrapContent)
                        backgroundResource = R.drawable.image_fade

                        padding = dip(20)
                        primaryImage = photoView {
                            lparams(matchParent, dip(300))

                        }
                        name = textView {
                            text = "Dealer Name"
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            compatAppearance = android.R.style.TextAppearance_Large_Inverse

                            topPadding = dip(10)
                        }

                        nameSecond = textView {
                            text = "Subname"
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            textAlignment = View.TEXT_ALIGNMENT_CENTER

                        }

                        categories = textView {
                            text = "Categories"
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            setTypeface(null, Typeface.BOLD)
                            setPadding(dip(10), dip(15), dip(10), dip(0))
                            topPadding = dip(15)
                        }

                        shortDescription = textView {
                            text = "Short description"
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            setPadding(dip(10), dip(15), dip(10), dip(15))
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                        }


                        websitesContainer = linearLayout {
                            weightSum = 100F
                            visibility = View.GONE

                            fontAwesomeView {
                                compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                                text = "{fa-globe 24sp}"
                            }.lparams(dip(0), wrapContent, 10F) { gravity = Gravity.CENTER_VERTICAL }

                            websites = verticalLayout {}.lparams(dip(0), wrapContent, 90F)
                        }

                        twitterContainer = linearLayout {
                            weightSum = 100F
                            visibility = View.GONE

                            fontAwesomeView {
                                compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                                text = "{fa-twitter 24sp}"
                            }.lparams(dip(0), wrapContent, 10F) { gravity = Gravity.CENTER_VERTICAL }

                            twitterButton = button {}.lparams(dip(0), wrapContent, 90F)
                        }

                        telegramContainer = linearLayout {
                            weightSum = 100F
                            visibility = View.GONE

                            fontAwesomeView {
                                compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                                text = "{fa-comments 24sp}"
                            }.lparams(dip(0), wrapContent, 10F) { gravity = Gravity.CENTER_VERTICAL }

                            telegramButton = button {}.lparams(dip(0), wrapContent, 90F)
                        }
                    }

                    verticalLayout {
                        backgroundResource = R.color.cardview_light_background
                        textView {
                            text = "Location & Availability"
                            compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            bottomPadding = dip(5)
                        }
                        padding = dip(20)

                        map = photoView {
                            backgroundResource = R.color.cardview_dark_background
                            minimumScale = 1F
                            mediumScale = 2.5F
                            maximumScale = 5F
                            lparams(matchParent, dip(400))
                            scaleType = ImageView.ScaleType.FIT_CENTER
                            imageResource = R.drawable.placeholder_event
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(10)
                    }


                    verticalLayout {
                        // artist
                        padding = dip(20)
                        backgroundResource = R.color.cardview_light_background
                        textView {
                            text = "About the Artist"
                            compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            bottomPadding = dip(5)
                        }

                        aboutArtist = textView {
                            compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            textColor = Color.BLACK
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(10)
                    }

                    aboutArtContainer = verticalLayout {
                        padding = dip(20)
                        backgroundResource = R.color.cardview_light_background

                        textView {
                            text = "About the Art"
                            compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            bottomPadding = dip(5)
                        }

                        aboutArt = textView {
                            compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            textColor = Color.BLACK
                        }

                        artPreviewContainer = verticalLayout {
                            topPadding = dip(10)

                            artPreview = photoView {
                                lparams(matchParent, dip(400))
                                scaleType = ImageView.ScaleType.FIT_CENTER
                            }

                            artPreviewCaption = textView {
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                padding = dip(15)
                                topPadding = dip(5)
                                compatAppearance = R.style.TextAppearance_AppCompat_Small
                            }
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(10)
                    }

                }.lparams(matchParent, wrapContent)
            }
        }
    }
}
