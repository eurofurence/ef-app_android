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
import com.pawegio.kandroid.longToast
import com.pawegio.kandroid.selector
import io.swagger.client.model.LinkFragment
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.MapRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.util.extensions.*

import java.util.*
import kotlin.properties.Delegates.notNull

/**
 * Created by david on 8/3/16.
 */
class MapFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    lateinit var map: PhotoView
    lateinit var title: TextView

    fun withArguments(mapRecord: MapRecord) = apply {
        arguments = Bundle().apply {
            jsonObjects["mapRecord"] = mapRecord
        }
    }

    private var mapRecord by notNull<MapRecord>()
    val image by lazy { db.images[mapRecord.imageId]!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        relativeLayout {
            backgroundResource = R.color.darkBackground
            map = photoView {
                layoutParams = relativeLayoutParams(matchParent, matchParent)
            }

            title = textView {

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ("mapRecord" in arguments!!) {
            mapRecord = arguments!!.jsonObjects["mapRecord"]

            info { "Browsing to map ${mapRecord.description}" }

            title.text = mapRecord.description
            title.visibility = View.GONE

            (db.images[mapRecord.imageId])?.let {
                ImageService.load(it, map, false)
            } ?: return

            map.attacher.minimumScale = 1F
            map.attacher.mediumScale = 2.5F
            map.attacher.maximumScale = 5F

            map.attacher.setOnPhotoTapListener { _, percentX, percentY ->
                val x = (image.width ?: 0) * percentX
                val y = (image.height ?: 0) * percentY
                info { "Tap registered at x: $x, y: $y" }

                val entry = mapRecord.findMatchingEntry(x, y)

                if (entry !== null) {
                    info { "Found matching entry" }
                    when (entry.links.size) {
                        0 -> Unit
                        1 -> linkAction(entry, entry.links[0])
                        else -> requireContext().selector(
                            getString(R.string.misc_find_out_more),
                            entry.links.map {
                                it.name ?: getString(R.string.misc_link_no_name_provided)
                            }) { position ->
                            linkAction(entry, entry.links[position])
                        }
                    }
                }
            }
        } else {
            map.imageResource = R.drawable.placeholder_event
        }
    }

    private fun linkAction(entry: MapEntryRecord, link: LinkFragment) {
        when (link.fragmentType) {
            LinkFragment.FragmentTypeEnum.DealerDetail -> launchDealer(link)
            LinkFragment.FragmentTypeEnum.MapExternal -> launchMap(link)
            LinkFragment.FragmentTypeEnum.WebExternal -> requireContext().browse(link.target)
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
            val action = MapListFragmentDirections.actionMapListFragmentToDealerItemFragment(
                dealer.id.toString(),
                null
            )
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
            // TODO: Cancellation removed
            longToast(label)
        } else {
            info { "Failed to create label; skipping tooltip" }
        }
    }
}

data class MapExternal(
    val lat: String,
    val lon: String,
    val name: String
)