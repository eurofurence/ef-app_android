package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.letRoot
import uk.co.senab.photoview.PhotoView

/**
 * Created by david on 8/3/16.
 */
class FragmentViewMaps : Fragment(), ContentAPI {
    // Event view holder finds and memorizes the views in an event card
    inner class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mapTitle by view(TextView::class.java)
        val mapImage by view(PhotoView::class.java)
    }

    inner class DataAdapter : RecyclerView.Adapter<MapViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, pos: Int) =
                MapViewHolder(LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.fragment_map, parent, false))

        override fun getItemCount() =
                maps.size

        override fun onBindViewHolder(holder: MapViewHolder, pos: Int) {
            val mapEntity = maps[pos]

            holder.mapTitle.text = mapEntity.description
            imageService.load(database.imageDb[mapEntity.imageId]!!, holder.mapImage)
        }
    }

    val database: Database get() = letRoot { it.database }!!

    val maps by lazy { database.mapEntityDb.items.filter { it.isBrowseable.toInt() != 0 } }

    val mapsRecycler by view(RecyclerView::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_maps, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapsRecycler.adapter = DataAdapter()
        mapsRecycler.layoutManager = LinearLayoutManager(activity)
        mapsRecycler.itemAnimator = DefaultItemAnimator()
    }
}