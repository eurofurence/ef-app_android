package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.swagger.client.model.MapEntity
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.contains
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.jsonObjects
import org.eurofurence.connavigator.util.extensions.letRoot
import uk.co.senab.photoview.PhotoView

/**
 * Created by david on 8/3/16.
 */
class FragmentMap() : Fragment(), ContentAPI {
    constructor(mapEntity: MapEntity) : this() {
        arguments = Bundle()

        arguments.jsonObjects["mapEntity"] = mapEntity
    }

    val mapTitle by view(TextView::class.java)
    val mapImage by view(PhotoView::class.java)

    val database: Database get() = letRoot { it.database } ?: Database(activity)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_map, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ("mapEntity" in arguments) {
            val mapEntity = arguments.jsonObjects["mapEntity", MapEntity::class.java]

            mapTitle.text = mapEntity.description

            mapTitle.visibility = View.GONE
            imageService.load(database.imageDb[mapEntity.imageId]!!, mapImage, false)
        }
    }
}