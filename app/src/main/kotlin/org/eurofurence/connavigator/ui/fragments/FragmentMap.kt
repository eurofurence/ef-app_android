package org.eurofurence.connavigator.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import com.google.gson.Gson
import io.swagger.client.model.LinkFragment
import io.swagger.client.model.MapRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.findMatchingEntries
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.photoView
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.selector
import java.util.*
import kotlin.properties.Delegates.notNull

/**
 * Created by david on 8/3/16.
 */
class FragmentMap() : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    fun withArguments(mapRecord: MapRecord) = apply {
        arguments = Bundle().apply {
            jsonObjects["mapRecord"] = mapRecord
        }
    }

    val ui = MapUi()
    var mapRecord by notNull<MapRecord>()
    val image by lazy { db.images[mapRecord.imageId]!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ("mapRecord" in arguments) {
            mapRecord = arguments.jsonObjects["mapRecord"]

            info { "Browsing to map ${mapRecord.description}" }

            ui.title.text = mapRecord.description
            ui.title.visibility = View.GONE

            (db.images[mapRecord.imageId])?.let {
                imageService.load(it, ui.map, false)
            } ?: return

            ui.map.attacher.minimumScale = 1F
            ui.map.attacher.mediumScale = 2.5F
            ui.map.attacher.maximumScale = 5F

            ui.map.attacher.setOnPhotoTapListener { _, percX, percY ->
                val x = (image.width ?: 0) * percX
                val y = (image.height ?: 0) * percY
                info { "Tap registered at x: $x, y: $y" }

                val entries = mapRecord.findMatchingEntries(x, y)

                info { "Found ${entries.size} entries" }

                if (entries.isNotEmpty()) {
                    val links = entries
                            .flatMap { it.links.orEmpty() }
                            .filter { it.fragmentType !== LinkFragment.FragmentTypeEnum.MapEntry }

                    when (links.size) {
                        0 -> Unit
                        1 -> linkAction(links[0])
                        else -> selector("Find out more", links.map {
                            it.name ?: "No name provided for link"
                        }) { _, position ->
                            linkAction(links[position])
                        }
                    }
                }
            }
        }
    } else
    {
        ui.map.setImageResource(R.drawable.placeholder_event)
    }
}

private fun linkAction(link: LinkFragment) {
    when (link.fragmentType) {
        LinkFragment.FragmentTypeEnum.DealerDetail -> launchDealer(link)
        LinkFragment.FragmentTypeEnum.MapExternal -> launchMap(link)
        LinkFragment.FragmentTypeEnum.WebExternal -> browse(link.target)
        else -> warn { "No items selected" }
    }
}

private fun launchMap(link: LinkFragment) {
    info { "Launching map" }
    val mapData = Gson().fromJson(link.target, MapExternal::class.java)
    info { "Launching to ${mapData.name}" }

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("geo:${mapData.lat},${mapData.lon}")
    startActivity(intent)
}

private fun launchDealer(link: LinkFragment) {
    info { "Launching dealer" }
    val dealer = db.dealers[UUID.fromString(link.target)]

    info { "Dealer is ${dealer?.getName()}" }
    if (dealer !== null) {
        applyOnRoot { navigateToDealer(dealer) }
    } else {
        longToast("Could not navigate to dealer")
    }
}

class MapUi : AnkoComponent<Fragment> {
    lateinit var map: PhotoView
    lateinit var title: TextView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        relativeLayout {
            backgroundResource = R.color.cardview_dark_background
            map = photoView {
                lparams(matchParent, matchParent)
            }

            title = textView()
        }
    }
}
}

data class MapExternal(
        val lat: String,
        val lon: String,
        val name: String
)