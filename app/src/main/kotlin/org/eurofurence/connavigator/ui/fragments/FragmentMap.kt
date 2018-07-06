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
import org.eurofurence.connavigator.util.extensions.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.browse
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.selector
import java.util.*
import kotlin.properties.Delegates.notNull

/**
 * Created by david on 8/3/16.
 */
class FragmentMap() : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()


    companion object {
        fun onMap(mapRecord: MapRecord) = FragmentMap().apply {
            arguments = Bundle().apply {
                jsonObjects["mapRecord"] = mapRecord
            }
        }
    }

    val ui = MapUi()
    var mapRecord by notNull<MapRecord>()
    val image by lazy { db.images[mapRecord.imageId]!! }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.create(requireContext().applicationContext, container!!))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { arguments ->
            if ("mapRecord" in arguments) {
                mapRecord = arguments.jsonObjects["mapRecord"]

                info { "Browsing to map ${mapRecord.description}" }

                ui.title.text = mapRecord.description
                ui.title.visibility = View.GONE

                imageService.load(db.images[mapRecord.imageId]!!, ui.map, false)

                ui.map.attacher.minimumScale = 1F
                ui.map.attacher.mediumScale = 2.5F
                ui.map.attacher.maximumScale = 5F

                ui.map.attacher.setOnPhotoTapListener { _, percX, percY ->
                    val x = image.width * percX
                    val y = image.height * percY
                    info { "Tap registered at x: $x, y: $y" }

                    val entries = mapRecord.findMatchingEntries(x, y)

                    info { "Found ${entries.size} entries" }

                    if (entries.isNotEmpty()) {
                        val links = entries.first()
                                .links
                                .filter { it.name !== null }
                                .filter { it.fragmentType !== LinkFragment.FragmentTypeEnum.MapEntry }

                        if (!links.isEmpty()) {
                            info { "Showing location selector" }
                            selector("Find out more", links.map { it.name }, { _, position ->
                                val link = links[position]

                                when (link.fragmentType) {
                                    LinkFragment.FragmentTypeEnum.DealerDetail -> launchDealer(link)
                                    LinkFragment.FragmentTypeEnum.MapExternal -> launchMap(link)
                                    LinkFragment.FragmentTypeEnum.WebExternal -> browse(link.target)
                                    else -> warn { "No items selected" }
                                }
                            })
                        }
                    }
                }
            } else {
                ui.map.setImageResource(R.drawable.placeholder_event)
            }
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

    class MapUi : AnkoComponent<ViewGroup> {
        lateinit var map: PhotoView
        lateinit var title: TextView

        override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
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