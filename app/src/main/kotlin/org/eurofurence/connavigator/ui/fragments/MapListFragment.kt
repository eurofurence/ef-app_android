package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.util.extensions.multitouchViewPager
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.UI

/**
 * Created by david on 8/3/16.
 */
class MapListFragment : Fragment(), HasDb {

    val ui by lazy { MapsUi() }
    override val db by lazyLocateDb()

    inner class MapFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int): String =
                browseableMaps[position].description

        override fun getItem(position: Int) =
                FragmentMap().withArguments(browseableMaps[position])


        override fun getCount() =
                browseableMaps.size
    }

    val browseableMaps by lazy { maps.items.filter { it.isBrowseable == true }.sortedBy { it.description } }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.mapViewPager.adapter = MapFragmentPagerAdapter(childFragmentManager)

        ui.tabs.setupWithViewPager(ui.mapViewPager)
    }
}


class MapsUi : AnkoComponent<Fragment> {
    lateinit var mapViewPager: ViewPager
    lateinit var tabs: TabLayout
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        frameLayout {
            tabs = tabLayout{
                backgroundResource = R.color.primaryDark
                setTabTextColors(
                        ContextCompat.getColor(context, R.color.textWhite),
                        ContextCompat.getColor(context, R.color.textWhite)
                )
                tabMode = TabLayout.MODE_SCROLLABLE
            }.lparams(matchParent, wrapContent)
            mapViewPager = multitouchViewPager {
                id = R.id.map_view_pager
            }.lparams(matchParent, matchParent)
        }
    }
}
