package org.eurofurence.connavigator.ui.fragments

import android.app.Dialog
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.chrisbanes.photoview.PhotoView
import com.pawegio.kandroid.runDelayed
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
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.services.AnalyticsService.Action
import org.eurofurence.connavigator.services.AnalyticsService.Category
import org.eurofurence.connavigator.ui.views.FontAwesomeType
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.get
import java.util.*

class DealerItemFragment : DisposingFragment(), HasDb, AnkoLogger {
    private val args: DealerItemFragmentArgs by navArgs()
    private val dealerId by lazy { UUID.fromString(args.dealerId) }


    override val db by lazyLocateDb()

    lateinit var scrollview: ScrollView
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
    lateinit var uiMap: PhotoView
    lateinit var locationContainer: LinearLayout
    lateinit var availableDaysText: TextView // TODO
    lateinit var afterDarkText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        createView {
            relativeLayout {
                backgroundResource = R.color.backgroundGrey
                scrollview = scrollView {
                    verticalLayout {
                        layoutParams = viewGroupLayoutParams(matchParent, matchParent)

                        verticalLayout {
                            layoutParams = linearLayoutParams(matchParent, wrapContent)
                            backgroundResource = R.drawable.image_fade

                            setPadding(dip(20), dip(20), dip(20), dip(20))
                            primaryImage = photoView {
                                layoutParams = linearLayoutParams(matchParent, dip(300))

                            }
                            name = textView {
                                setText(R.string.dealer_name)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                compatAppearance = android.R.style.TextAppearance_Large_Inverse

                                setPadding(0, dip(10), 0, 0)
                            }

                            nameSecond = textView {
                                setText(R.string.dealer_subname)
                                compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                                textAlignment = View.TEXT_ALIGNMENT_CENTER

                            }

                            categories = textView {
                                setText(R.string.misc_categories)
                                compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                setTypeface(null, Typeface.BOLD)
                                setPadding(dip(10), dip(15), dip(10), dip(0))
                            }

                            shortDescription = textView {
                                setText(R.string.misc_short_description)
                                textAlignment = View.TEXT_ALIGNMENT_CENTER
                                setPadding(dip(10), dip(15), dip(10), dip(15))
                                compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                            }


                            websitesContainer = linearLayout {
                                weightSum = 100F
                                visibility = View.GONE

                                fontAwesomeView {
                                    type= FontAwesomeType.Solid
                                    layoutParams = linearLayoutParams(dip(0), wrapContent, 10F) {
                                        gravity = Gravity.CENTER_VERTICAL
                                    }
                                    setTextColor(Color.WHITE)
                                    text = getString(R.string.fa_globe_solid)
                                    textSize = 24f
                                }

                                websites = verticalLayout {
                                    layoutParams = linearLayoutParams(dip(0), wrapContent, 90F)
                                }
                            }

                            twitterContainer = linearLayout {
                                weightSum = 100F
                                visibility = View.GONE

                                fontAwesomeView {
                                    type=FontAwesomeType.Brands
                                    layoutParams = linearLayoutParams(dip(0), wrapContent, 10F) {
                                        gravity = Gravity.CENTER_VERTICAL
                                    }
                                    setTextColor(Color.WHITE)
                                    text = getString(R.string.fa_twitter)
                                    textSize = 24f
                                }

                                twitterButton = button {
                                    layoutParams = linearLayoutParams(dip(0), wrapContent, 90F)
                                }
                            }

                            telegramContainer = linearLayout {
                                weightSum = 100F
                                visibility = View.GONE

                                fontAwesomeView {
                                    type=FontAwesomeType.Brands
                                    layoutParams = linearLayoutParams(dip(0), wrapContent, 10F) {
                                        gravity = Gravity.CENTER_VERTICAL
                                    }
                                    setTextColor(Color.WHITE)
                                    text = getString(R.string.fa_telegram)
                                    textSize = 24f
                                }

                                telegramButton = button {
                                    layoutParams = linearLayoutParams(dip(0), wrapContent, 90F)
                                }
                            }
                        }

                        locationContainer = verticalLayout {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                topMargin = dip(10)
                            }

                            backgroundResource = R.color.lightBackground
                            textView {
                                setText(R.string.dealer_location_and_availability)
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                setPadding(0, 0, 0, dip(5))
                            }
                            padding = dip(20)

                            constraintLayout {
                                layoutParams = linearLayoutParams(matchParent, wrapContent)
                                id = R.id.dealer_container

                                uiMap = photoView {
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
                            }

                            verticalLayout {
                                padding = dip(10)
                                availableDaysText = fontAwesomeView {
                                    text = "${getString(R.string.fa_exclamation_triangle_solid)} ${
                                        resources.getString(R.string.dealer_only_present_on)
                                    }"
                                    textSize = 24f
                                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                }

                                afterDarkText = fontAwesomeView {
                                    text =
                                        "${getString(R.string.fa_moon)} ${resources.getString(R.string.dealer_located_in_the_after_dark)}"
                                    textSize = 24f
                                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                }
                            }
                        }


                        verticalLayout {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                topMargin = dip(10)
                            }
                            // artist
                            padding = dip(20)
                            backgroundResource = R.color.lightBackground
                            textView {
                                setText(R.string.dealer_about_artist)
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                setPadding(0, 0, 0, dip(5))
                            }

                            aboutArtist = textView {
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                setTextColor(Color.BLACK)
                            }
                        }

                        aboutArtContainer = verticalLayout {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                topMargin = dip(10)
                            }

                            padding = dip(20)
                            backgroundResource = R.color.lightBackground

                            textView {
                                setText(R.string.dealer_about_art)
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                setPadding(0, 0, 0, dip(5))
                            }

                            aboutArt = textView {
                                compatAppearance = R.style.TextAppearance_AppCompat_Medium
                                setTextColor(Color.BLACK)
                            }

                            artPreviewContainer = verticalLayout {
                                setPadding(0, dip(10), 0, 0)

                                artPreview = photoView {
                                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                                    backgroundResource = R.color.darkBackground
                                    scaleType = ImageView.ScaleType.FIT_CENTER
                                    adjustViewBounds = true
                                }

                                artPreviewCaption = textView {
                                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    setPadding(dip(15), dip(5), dip(15), dip(15))
                                    compatAppearance = R.style.TextAppearance_AppCompat_Small
                                }
                            }
                        }

                    }
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.cid != null && !args.cid.equals(BuildConfig.CONVENTION_IDENTIFIER, true)) {

            requireContext().alert("This item is not for this convention", "Wrong convention!")
                .show()

            findNavController().popBackStack()
        }

        db.subscribe {
            fillUi()
        }
            .collectOnDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("sv_key_x", scrollview.scrollX)
        outState.putInt("sv_key_y", scrollview.scrollY)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        runDelayed(200) {
            savedInstanceState?.getInt("sv_key_x")?.let(scrollview::setScrollX)
            savedInstanceState?.getInt("sv_key_y")?.let(scrollview::setScrollY)
        }
    }

    private fun fillUi() {
        if (dealerId != null) {
            val dealer: DealerRecord = db.dealers[dealerId] ?: return

            AnalyticsService.event(
                Category.DEALER, Action.OPENED, dealer.displayName
                    ?: dealer.attendeeNickname
            )

            // Retrieve top image
            val image = dealer[toArtistImage]

            // Set image on top
            if (image != null) {
                ImageService.load(image, primaryImage, false)
            } else {
                primaryImage.visibility = View.GONE
            }

            // Load art preview image
            ImageService.load(dealer[toPreview], artPreview)

            artPreviewCaption.text = dealer.artPreviewCaption

            name.text = dealer.getName()
            nameSecond.apply {
                text = dealer.attendeeNickname
                visibility = if (dealer.hasUniqueDisplayName()) View.VISIBLE else View.GONE
            }

            categories.text = dealer.categories?.joinToString(", ") ?: ""
            shortDescription.apply {
                text = dealer.shortDescription
                visibility =
                    if (dealer.shortDescription.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            categories.text = dealer.categories?.joinToString(", ") ?: ""

            aboutArtist.text =
                if (dealer.aboutTheArtistText.isNullOrEmpty())
                    getString(R.string.dealer_artist_did_not_supply_a_description)
                else
                    dealer.aboutTheArtistText


            if (dealer.artPreviewImageId == null) {
                artPreview.visibility = View.GONE

                if (dealer.artPreviewCaption.isNullOrEmpty()) {
                    artPreviewContainer.visibility = View.GONE
                }
            }

            if (dealer.aboutTheArtText.isNullOrEmpty()) {
                aboutArtContainer.visibility = View.GONE
            } else {
                aboutArt.text = dealer.aboutTheArtText
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
            locationContainer.visibility = View.GONE
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
                    uiMap.visibility = View.GONE
                else {
                    val bitmap = Bitmap.createBitmap(it, x, y, w, h)

                    Canvas(bitmap).apply {
                        drawCircle(ox.toFloat(), oy.toFloat(), circle?.toFloat()
                            ?: 0f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = Color.RED
                            style = Paint.Style.STROKE
                            strokeWidth = try {
                                uiMap.dip(5f)
                            } catch (ex: IllegalArgumentException) {
                                5F
                            }
                        })
                    }

                    uiMap.setImageDrawable(BitmapDrawable(resources, bitmap))
                    uiMap.visibility = View.VISIBLE
                }
            } failUi {
                uiMap.visibility = View.GONE
            }
        }
    }

    private fun configureAvailability(dealer: DealerRecord) {
        // Availability
        availableDaysText.visibility = if (dealer.allDaysAvailable()) View.GONE else View.VISIBLE

        val availableDayList = mutableSetOf<String>()

        if (dealer.attendsOnThursday == true) availableDayList.add("Thu")
        if (dealer.attendsOnFriday == true) availableDayList.add("Fri")
        if (dealer.attendsOnSaturday == true) availableDayList.add("Sat")

        availableDaysText.text =
            getString(R.string.dealer_only_present_on, availableDayList.joinToString(", "))

        // After dark
        afterDarkText.visibility = if (dealer.isAfterDark == true) View.VISIBLE else View.GONE
    }

    private fun configureLinks(dealer: DealerRecord) {
        info { "Setting up external links" }

        // Reset all views that have been present before
        websites.removeAllViews()

        if (dealer.links != null && dealer.links?.isNotEmpty() != false) {
            websitesContainer.visibility = View.VISIBLE
            dealer.links?.forEach {
                val button = Button(context).apply {
                    info { "Adding button for $it" }
                    text = it.target
                    visibility = View.VISIBLE
                    //layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
                    setOnClickListener { _ -> context.browse(it.target) }
                }

                websites.addView(button)
            }
        }

        if (!dealer.telegramHandle.isNullOrEmpty()) {
            info { "Setting up telegram button for ${dealer.telegramHandle}" }
            telegramButton.apply {
                text = getString(R.string.dealer_telegram_handle, dealer.telegramHandle)
                setOnClickListener { context.browse("https://telegram.me/${dealer.telegramHandle}") }
            }
            telegramContainer.apply {
                visibility = View.VISIBLE
            }
        }

        if (!dealer.twitterHandle.isNullOrEmpty()) {
            info { "Setting up twitter handle" }
            twitterButton.apply {
                text = getString(R.string.dealer_twitter_handle, dealer.twitterHandle)
                setOnClickListener { context.browse("https://twitter.com/${dealer.twitterHandle}") }
            }
            twitterContainer.apply {
                visibility = View.VISIBLE
            }
        }
    }
}