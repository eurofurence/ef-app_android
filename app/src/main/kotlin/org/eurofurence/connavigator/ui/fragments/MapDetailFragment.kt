package org.eurofurence.connavigator.ui.fragments

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import io.reactivex.disposables.Disposables
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.MapRecord
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.findLinkFragment
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.util.extensions.photoView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.px2dip
import java.util.*

class MapDetailFragment : DisposingFragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    val ui by lazy { MapDetailUi() }
    val id get() = arguments?.getString("id") ?: ""
    val showTitle get() = arguments!!.getBoolean("showTitle")
    private var linkFound = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.subscribe {
            ui.title.visibility = if (showTitle) View.VISIBLE else View.GONE
            findLinkFragment()
        }
        .collectOnDestroyView()
    }

    fun withArguments(id: UUID?, showTitle: Boolean = false) = apply {
        arguments = Bundle().apply {
            id?.let { putString("id", it.toString()) }
            putBoolean("showTitle", showTitle)
        }
    }

    private fun findLinkFragment() {
        info { "Finding map link in mapEntries. ID: $id" }
        val entryMap = db.findLinkFragment(id)

        val map = entryMap["map"] as MapRecord?
        val entry = entryMap["entry"] as MapEntryRecord?

        if (map == null) {
            info { "No maps or deviations. Hiding location and availability" }
            ui.layout.visibility = View.GONE
            return
        }

        // Setup map
        if (map != null && entry != null) {
            info { "Found maps and entries, ${map.description} at (${entry.x}, ${entry.y})" }

            linkFound = true

            val mapImage = db.toImage(map)!!
            val radius = 300
            val circle = entry.tapRadius
            val x = maxOf(0, (entry.x ?: 0) - radius)
            val y = maxOf(0, (entry.y ?: 0) - radius)
            val w = minOf((mapImage.width ?: 0) - x - 1, radius + radius)
            val h = minOf((mapImage.height ?: 0) - y - 1, radius + radius)
            val ox = (entry.x ?: 0) - x
            val oy = (entry.y ?: 0) - y

            ImageService.preload(mapImage) successUi {
                if (it == null || activity == null)
                    ui.layout.visibility = View.GONE
                else {
                    try {
                        val bitmap = Bitmap.createBitmap(it, x, y, w, h)

                        Canvas(bitmap).apply {
                            drawCircle(ox.toFloat(), oy.toFloat(), (circle
                                    ?: 0).toFloat(), Paint(Paint.ANTI_ALIAS_FLAG).apply {
                                color = Color.RED
                                style = Paint.Style.STROKE
                                strokeWidth = px2dip(5)
                            })
                        }

                        ui.map.image = BitmapDrawable(resources, bitmap)
                        ui.layout.visibility = View.VISIBLE
                    } catch (ex: IllegalArgumentException) {
                        ui.layout.visibility = View.GONE
                    }
                }
            } failUi {
                ui.layout.visibility = View.GONE
            }
        } else {
            warn { "No map or entry found!" }
            ui.layout.visibility = View.GONE
        }

    }

}

class MapDetailUi : AnkoComponent<Fragment> {
    lateinit var map: PhotoView
    lateinit var layout: LinearLayout
    lateinit var title: TextView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        relativeLayout {
            lparams(matchParent, wrapContent)
            layout = verticalLayout {
                backgroundResource = R.color.lightBackground
                title = textView {
                    textResource = R.string.misc_location
                    compatAppearance = android.R.style.TextAppearance_Small
                    padding = dip(20)
                }
                map = photoView {
                    backgroundResource = R.color.darkBackground
                    minimumScale = 1F
                    mediumScale = 2.5F
                    maximumScale = 5F
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    imageResource = R.drawable.placeholder_event
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent) {
                topMargin = dip(10)
            }
        }
    }
}