package org.eurofurence.connavigator.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.chrisbanes.photoview.PhotoView
import com.google.gson.Gson
import io.swagger.client.model.LinkFragment
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.MapRecord
import kotlinx.serialization.internal.MapEntry
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.util.extensions.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.*
import java.util.*
import kotlin.properties.Delegates.notNull

/**
 * Created by david on 8/3/16.
 */
class MapFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    fun withArguments(mapRecord: MapRecord) = apply {
        arguments = Bundle().apply {
            jsonObjects["mapRecord"] = mapRecord
        }
    }

    val ui = MapUi()
    private var mapRecord by notNull<MapRecord>()
    val image by lazy { db.images[mapRecord.imageId]!! }
    private var lastTooltipToast: Toast? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ("mapRecord" in arguments!!) {
            mapRecord = arguments!!.jsonObjects["mapRecord"]

            info { "Browsing to map ${mapRecord.description}" }

            ui.title.text = mapRecord.description
            ui.title.visibility = View.GONE

            (db.images[mapRecord.imageId])?.let {
                ImageService.load(it, ui.map, false)
            } ?: return

            ui.map.attacher.minimumScale = 1F
            ui.map.attacher.mediumScale = 2.5F
            ui.map.attacher.maximumScale = 5F

            ui.map.attacher.setOnPhotoTapListener { _, percentX, percentY ->
                val x = (image.width ?: 0) * percentX
                val y = (image.height ?: 0) * percentY
                info { "Tap registered at x: $x, y: $y" }

                val entry = mapRecord.findMatchingEntry(x, y)

                if (entry !== null) {
                    info { "Found matching entry" }
                    when (entry.links.size) {
                        0 -> Unit
                        1 -> linkAction(entry, entry.links[0])
                        else -> selector(getString(R.string.misc_find_out_more), entry.links.map {
                            it.name ?: getString(R.string.misc_link_no_name_provided)
                        }) { _, position ->
                            linkAction(entry, entry.links[position])
                        }
                    }
                }
            }
        } else {
            ui.map.setImageResource(R.drawable.placeholder_event)
        }
    }

    private fun linkAction(entry: MapEntryRecord, link: LinkFragment) {
        when (link.fragmentType) {
            LinkFragment.FragmentTypeEnum.DealerDetail -> launchDealer(link)
            LinkFragment.FragmentTypeEnum.MapExternal -> launchMap(link)
            LinkFragment.FragmentTypeEnum.WebExternal -> browse(link.target)
            LinkFragment.FragmentTypeEnum.MapEntry -> showTooltip(entry, link)
            LinkFragment.FragmentTypeEnum.EventConferenceRoom -> showTooltip(entry, link)
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
            val action = MapListFragmentDirections.actionMapListFragmentToDealerItemFragment(dealer.id.toString(), null)
            findNavController().navigate(action)
        } else {
            longToast(getString(R.string.dealer_could_not_navigate_to))
        }
    }

    private fun showTooltip(entry: MapEntryRecord, link: LinkFragment) {
        info { "Request to show tooltip" }
        var label: String = link.name
        var tooltipX: Int = entry.x
        var tooltipY: Int = entry.y
        when (link.fragmentType) {
            LinkFragment.FragmentTypeEnum.MapEntry -> {
                val targetEntry = mapRecord.entries.find { it.id.equals(link.target) }
                if (targetEntry !== null) {
                    tooltipX = targetEntry.x
                    tooltipY = targetEntry.y
                }
            }
            LinkFragment.FragmentTypeEnum.EventConferenceRoom -> {
                if (label.isNotBlank()) {
                    val eventConferenceRoom = db.rooms[UUID.fromString(link.target)]
                    if (eventConferenceRoom !== null) {
                        label = eventConferenceRoom.name
                    }
                }
            }
            else -> {}
        }

        if (label.isNotBlank()) {
            info { "Displaying tooltip for ${label} at (${tooltipX}, ${tooltipY})" }
            // TODO: Display actual tooltip at (tooltipX, tooltipY) instead of using a Toast
            lastTooltipToast?.cancel()
            lastTooltipToast = longToast(label)
        } else {
            info { "Failed to create label; skipping tooltip" }
        }
    }

    class MapUi : AnkoComponent<Fragment> {
        lateinit var map: PhotoView
        lateinit var title: TextView

        override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
            relativeLayout {
                backgroundResource = R.color.darkBackground
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