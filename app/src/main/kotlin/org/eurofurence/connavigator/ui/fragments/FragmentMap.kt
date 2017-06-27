package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.swagger.client.model.MapRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.jsonObjects
import uk.co.senab.photoview.PhotoView
import kotlin.properties.Delegates.notNull

/**
 * Created by david on 8/3/16.
 */
class FragmentMap() : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    constructor(mapRecord: MapRecord) : this() {
        arguments = Bundle()

        arguments.jsonObjects["mapRecord"] = mapRecord
    }

    val mapTitle: TextView by view()
    val mapImage: PhotoView by view()

    var mapRecord by notNull<MapRecord>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ("mapRecord" in arguments) {
            mapRecord = arguments.jsonObjects["mapRecord"]

            mapTitle.text = mapRecord.description

            mapTitle.visibility = View.GONE
            /* TODO
            imageService.load(database.imageDb[mapRecord.imageId]!!, mapImage, false)
            */
        } else {
            mapImage.setImageResource(R.drawable.placeholder_event)
        }
    }
}