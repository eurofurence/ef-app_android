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
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.fragments.FragmentMap
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.letRoot

/**
 * Created by david on 8/3/16.
 */
class FragmentViewMaps : Fragment(), ContentAPI {
    inner class MapFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int) =
                maps[position].description

        override fun getItem(position: Int) =
                FragmentMap(maps[position])


        override fun getCount() =
                maps.size
    }

    val database: Database get() = letRoot { it.database }!!

    val maps by lazy { database.mapEntityDb.items.filter { it.isBrowseable.toInt() != 0 } }

    val mapViewPager by view(ViewPager::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_maps, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapViewPager.adapter = MapFragmentPagerAdapter(childFragmentManager)

        applyOnRoot { tabs.setupWithViewPager(mapViewPager) }
    }
}