package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.FragmentMap
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot

/**
 * Created by david on 8/3/16.
 */
class FragmentViewMaps : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    inner class MapFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int) =
                browseableMaps[position].description

        override fun getItem(position: Int) =
                FragmentMap(browseableMaps[position])


        override fun getCount() =
                browseableMaps.size
    }

    val browseableMaps by lazy { maps.items.filter { it.isBrowseable } }

    val mapViewPager: ViewPager by view()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_maps, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapViewPager.adapter = MapFragmentPagerAdapter(childFragmentManager)

        applyOnRoot {
            tabs.setupWithViewPager(mapViewPager)
            changeTitle("Maps")
        }
    }
}