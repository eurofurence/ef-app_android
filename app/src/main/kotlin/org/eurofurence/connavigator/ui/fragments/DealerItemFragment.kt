package org.eurofurence.connavigator.ui.fragments

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import com.joanzapata.iconify.widget.IconTextView
import com.pawegio.kandroid.runDelayed
import io.reactivex.disposables.Disposables
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.MapRecord
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.findLinkFragment
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.services.AnalyticsService.Action
import org.eurofurence.connavigator.services.AnalyticsService.Category
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.get
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.browse
import org.jetbrains.anko.support.v4.px2dip
import java.util.*

class DealerItemFragment : DisposingFragment(), HasDb, AnkoLogger {
    private val args: DealerItemFragmentArgs by navArgs()
    private val dealerId by lazy { UUID.fromString(args.dealerId) }

    val ui by lazy { DealerUi() }

    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.CID != null && !args.CID.equals(BuildConfig.CONVENTION_IDENTIFIER, true)) {
            alert("This item is not for this convention", "Wrong convention!").show()

            findNavController().popBackStack()
        }

        db.subscribe {
            fillUi()
        }
        .collectOnDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        ui.scrollview?.also { sv ->
            outState.putInt("sv_key_x", sv.scrollX)
            outState.putInt("sv_key_y", sv.scrollY)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        runDelayed(200) {
            ui.scrollview?.also { sv ->
                savedInstanceState?.getInt("sv_key_x")?.let(sv::setScrollX)
                savedInstanceState?.getInt("sv_key_y")?.let(sv::setScrollY)
            }
        }
    }

    private fun fillUi() {
        if (dealerId != null) {
            val dealer: DealerRecord = db.dealers[dealerId] ?: return

            AnalyticsService.event(Category.DEALER, Action.OPENED, dealer.displayName
                    ?: dealer.attendeeNickname)

            // Retrieve top image
            val image = dealer[toArtistImage]

            // Set image on top
            if (image != null) {
                ImageService.load(image, ui.primaryImage, false)
            } else {
                ui.primaryImage.visibility = View.GONE
            }

            // Load art preview image
            ImageService.load(dealer[toPreview], ui.artPreview)

            ui.artPreviewCaption.text = dealer.artPreviewCaption

            ui.name.text = dealer.getName()
            ui.nameSecond.apply {
                text = dealer.attendeeNickname
                visibility = if (dealer.hasUniqueDisplayName()) View.VISIBLE else View.GONE
            }

            ui.categories.text = dealer.categories?.joinToString(", ") ?: ""
            ui.shortDescription.apply {
                text = dealer.shortDescription
                visibility = if (dealer.shortDescription.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            ui.categories.text = dealer.categories?.joinToString(", ") ?: ""

            ui.aboutArtist.text =
                    if (dealer.aboutTheArtistText.isNullOrEmpty())
                        getString(R.string.dealer_artist_did_not_supply_a_description)
                    else
                        dealer.aboutTheArtistText


            if (dealer.artPreviewImageId == null) {
                ui.artPreview.visibility = View.GONE

                if (dealer.artPreviewCaption.isNullOrEmpty()) {
                    ui.artPreviewContainer.visibility = View.GONE
                }
            }

            if (dealer.aboutTheArtText.isNullOrEmpty()) {
                ui.aboutArtContainer.visibility = View.GONE
            } else {
                ui.aboutArt.text = dealer.aboutTheArtText
            }

            configureLinks(dealer)
            configureAvailability(dealer)
            configureLocation(dealer)
        }
    }

    private fun configureLocation(dealer: DealerRecord) {
        info { "Finding dealer in mapEntries" }
        val entryMap = db.findLinkFragment(dealer.id.toString())

        val map = entryMap["map"] as MapRecord?
        val entry = entryMap["entry"] as MapEntryRecord?

        if (map == null && dealer.allDaysAvailable() && dealer.isAfterDark != true) {
            info { "No maps or deviations. Hiding location and availability" }
            ui.locationContainer.visibility = View.GONE
            return
        }

        // Setup map
        if (map != null && entry != null) {
            info { "Found maps and entries, ${map.description} at (${entry.x}, ${entry.y})" }

            val mapImage = db.toImage(map) ?: return


            val radius = 300
            val circle = entry.tapRadius
            val x = maxOf(0, (entry.x ?: 0) - radius)
            val y = maxOf(0, (entry.y ?: 0) - radius)
            val w = minOf((mapImage.width ?: 0) - x - 1, radius + radius)
            val h = minOf((mapImage.height ?: 0) - y - 1, radius + radius)
            val ox = (entry.x ?: 0) - x
            val oy = (entry.y ?: 0) - y

            ImageService.preload(mapImage) successUi {
                if (it == null)
                    ui.map.visibility = View.GONE
                else {
                    val bitmap = Bitmap.createBitmap(it, x, y, w, h)

                    Canvas(bitmap).apply {
                        drawCircle(ox.toFloat(), oy.toFloat(), circle?.toFloat()
                                ?: 0f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = Color.RED
                            style = Paint.Style.STROKE
                            strokeWidth = try {
                                px2dip(5)
                            } catch (ex: IllegalArgumentException) {
                                5F
                            }
                        })
                    }

                    ui.map.image = BitmapDrawable(resources, bitmap)
                    ui.map.visibility = View.VISIBLE
                }
            } failUi {
                ui.map.visibility = View.GONE
            }
        }
    }

    private fun configureAvailability(dealer: DealerRecord) {
        // Availability
        ui.availableDaysText.visibility = if (dealer.allDaysAvailable()) View.GONE else View.VISIBLE

        val availableDayList = mutableSetOf<String>()

        if (dealer.attendsOnThursday == true) availableDayList.add("Thu")
        if (dealer.attendsOnFriday == true) availableDayList.add("Fri")
        if (dealer.attendsOnSaturday == true) availableDayList.add("Sat")

        ui.availableDaysText.text = getString(R.string.dealer_only_present_on, availableDayList.joinToString(", "))

        // After dark
        ui.afterDarkText.visibility = if (dealer.isAfterDark == true) View.VISIBLE else View.GONE
    }

    private fun configureLinks(dealer: DealerRecord) {
        info { "Setting up external links" }

        // Reset all views that have been present before
        ui.websites.removeAllViews()

        if (dealer.links != null && dealer.links?.isNotEmpty() != false) {
            ui.websitesContainer.visibility = View.VISIBLE
            dealer.links?.forEach {
                val button = Button(context).apply {
                    info { "Adding button for $it" }
                    text = it.target
                    visibility = View.VISIBLE
                    //layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                    setOnClickListener { _ -> browse(it.target) }
                }

                ui.websites.addView(button)
            }
        }

        if (!dealer.telegramHandle.isNullOrEmpty()) {
            info { "Setting up telegram button for ${dealer.telegramHandle}" }
            ui.telegramButton.apply {
                text = getString(R.string.dealer_telegram_handle, dealer.telegramHandle)
                setOnClickListener { browse("https://telegram.me/${dealer.telegramHandle}") }
            }
            ui.telegramContainer.apply {
                visibility = View.VISIBLE
            }
        }

        if (!dealer.twitterHandle.isNullOrEmpty()) {
            info { "Setting up twitter handle" }
            ui.twitterButton.apply {
                text = getString(R.string.dealer_twitter_handle, dealer.twitterHandle)
                setOnClickListener { browse("https://twitter.com/${dealer.twitterHandle}") }
            }
            ui.twitterContainer.apply {
                visibility = View.VISIBLE
            }
        }
    }
}

class DealerUi : AnkoComponent<Fragment> {
    var scrollview: ScrollView? = null
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
    lateinit var locationContainer: LinearLayout
    lateinit var availableDaysText: IconTextView
    lateinit var afterDarkText: TextView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        relativeLayout {
            backgroundResource = R.color.backgroundGrey
            scrollview = scrollView {
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
                            textResource = R.string.dealer_name
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            compatAppearance = android.R.style.TextAppearance_Large_Inverse

                            topPadding = dip(10)
                        }

                        nameSecond = textView {
                            textResource = R.string.dealer_subname
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            textAlignment = View.TEXT_ALIGNMENT_CENTER

                        }

                        categories = textView {
                            textResource = R.string.misc_categories
                            compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            textAlignment = View.TEXT_ALIGNMENT_CENTER
                            setTypeface(null, Typeface.BOLD)
                            setPadding(dip(10), dip(15), dip(10), dip(0))
                            topPadding = dip(15)
                        }

                        shortDescription = textView {
                            textResource = R.string.misc_short_description
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

                    locationContainer = verticalLayout {
                        backgroundResource = R.color.lightBackground
                        textView {
                            textResource = R.string.dealer_location_and_availability
                            compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            bottomPadding = dip(5)
                        }
                        padding = dip(20)

                        ankoView(::ConstraintLayout, 0) {
                            id = R.id.dealer_container

                            map = photoView {
                                id = R.id.dealer_map

                                backgroundResource = R.color.darkBackground
                                minimumScale = 1F
                                mediumScale = 2.5F
                                maximumScale = 5F
                                scaleType = ImageView.ScaleType.FIT_CENTER
                                imageResource = R.drawable.placeholder_event
                            }

                            androidx.constraintlayout.widget.ConstraintSet().apply {
                                connect(R.id.dealer_map, START, R.id.dealer_container, START)
                                connect(R.id.dealer_map, END, R.id.dealer_container, END)
                                setDimensionRatio(R.id.dealer_map, "1:1")
                            }.applyTo(this)
                        }.lparams(matchParent, wrapContent)

                        verticalLayout {
                            padding = dip(10)
                            availableDaysText = fontAwesomeView {
                                text = "{fa-exclamation-triangle 24sp} ${resources.getString(R.string.dealer_only_present_on)}"
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            }

                            afterDarkText = fontAwesomeView {
                                text = "{fa-moon-o 24sp} ${resources.getString(R.string.dealer_located_in_the_after_dark)}"
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                            }
                        }
                    }.lparams(matchParent, wrapContent) {
                        topMargin = dip(10)
                    }


                    verticalLayout {
                        // artist
                        padding = dip(20)
                        backgroundResource = R.color.lightBackground
                        textView {
                            textResource = R.string.dealer_about_artist
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
                        backgroundResource = R.color.lightBackground

                        textView {
                            textResource = R.string.dealer_about_art
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
                                backgroundResource = R.color.darkBackground
                                lparams(matchParent, wrapContent)
                                scaleType = ImageView.ScaleType.FIT_CENTER
                                adjustViewBounds = true
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
