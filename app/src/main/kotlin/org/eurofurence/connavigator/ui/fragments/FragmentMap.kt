package org.eurofurence.connavigator.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import com.google.gson.Gson
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.MapRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.*
import org.jetbrains.anko.*
import java.util.*
import kotlin.properties.Delegates.notNull

/**
 * Created by david on 8/3/16.
 */
class FragmentMap() : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    constructor(mapRecord: MapRecord) : this() {
        arguments = Bundle()

        arguments.jsonObjects["mapRecord"] = mapRecord
    }

    val ui = MapUi()
    var mapRecord by notNull<MapRecord>()
    val image by lazy { db.images[mapRecord.imageId]!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.create(context.applicationContext, container!!))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ("mapRecord" in arguments) {
            mapRecord = arguments.jsonObjects["mapRecord"]

            info { "Browsing to map ${mapRecord.description}" }

            ui.title.text = mapRecord.description
            ui.title.visibility = View.GONE

            imageService.load(db.images[mapRecord.imageId]!!, ui.map, false)

            ui.map.attacher.minimumScale = 1F
            ui.map.attacher.mediumScale = 2.5F
            ui.map.attacher.maximumScale = 5F

            ui.map.attacher.setOnPhotoTapListener { view, percX, percY ->
                val x = image.width * percX
                val y = image.height * percY
                info { "Tap registered at x: $x, y: $y" }

                val entries = mapRecord.findMatchingEntries(x, y)

                info { "Found ${entries.size} entries" }

                if (entries.isNotEmpty()) {
                    info { "Dealer ID:  ${entries.first()}" }
                    fillLinkLayout(entries.first())
                    ui.linkLayout.visibility = View.VISIBLE
                } else {
                    ui.linkLayout.visibility = View.GONE
                }
            }
        } else {
            ui.map.setImageResource(R.drawable.placeholder_event)
        }
    }

    fun fillLinkLayout(entry: MapEntryRecord) = when (entry.links.first().fragmentType.name) {
        "DealerDetail" -> fillLinkAsDealer(entry)
        "MapExternal" -> fillLinkAsExternalMap(entry)
        else -> Unit
    }

    private fun fillLinkAsExternalMap(entry: MapEntryRecord) {
        val mapData = Gson().fromJson(entry.links.first().target, MapExternal::class.java)

        ui.linkTitle.text = "Navigate to ${mapData.name}"
        ui.linkLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:${mapData.lat},${mapData.lon}")
            startActivity(intent)
        }
    }

    private fun fillLinkAsDealer(entry: MapEntryRecord) {
        val dealer = db.dealers[UUID.fromString(entry.links.first().target)]

        ui.linkTitle.text = "Read more about ${dealer!!.getName()}"
        ui.linkLayout.setOnClickListener {
            applyOnRoot { navigateToDealer(dealer) }
        }
    }

    class MapUi : AnkoComponent<ViewGroup> {
        lateinit var map: PhotoView
        lateinit var title: TextView
        lateinit var linkLayout: LinearLayout

        lateinit var linkTitle: TextView

        override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
            relativeLayout {
                backgroundResource = R.color.cardview_dark_background
                map = photoView {
                    lparams(matchParent, matchParent)
                }

                title = textView()

                linkLayout = verticalLayout {
                    visibility = View.GONE
                    padding = dip(15)
                    backgroundResource = R.color.accent
                    linkTitle = textView {
                        setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Medium)
                    }
                }.lparams(matchParent, wrapContent) { alignParentBottom() }
            }
        }
    }
}

data class MapExternal(
        val lat: String,
        val lon: String,
        val name: String
)