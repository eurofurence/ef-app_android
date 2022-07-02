package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*

/**
 * Created by david on 8/3/16.
 */
class MapListFragment : Fragment(), HasDb {

    override val db by lazyLocateDb()
    lateinit var mapViewPager: ViewPager
    lateinit var tabs: TabLayout

    inner class MapFragmentPagerAdapter(fragmentManager: FragmentManager) :
        FragmentStatePagerAdapter(fragmentManager) {
        override fun getPageTitle(position: Int): String =
            browseableMaps[position].description

        override fun getItem(position: Int) =
            MapFragment().withArguments(browseableMaps[position])


        override fun getCount() =
            browseableMaps.size
    }

    val browseableMaps by lazy {
        maps.items.filter { it.isBrowseable == true }.sortedBy { it.description }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        verticalLayout {
            tabs = tabLayout {
                layoutParams = linearLayoutParams(matchParent, wrapContent)
                backgroundResource = R.color.primaryDark
                setTabTextColors(
                    ContextCompat.getColor(context, R.color.textWhite),
                    ContextCompat.getColor(context, R.color.textWhite)
                )
                tabMode = TabLayout.MODE_SCROLLABLE
            }
            mapViewPager = multitouchViewPager {
                layoutParams = linearLayoutParams(matchParent, matchParent)
                id = R.id.map_view_pager
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapViewPager.adapter = MapFragmentPagerAdapter(childFragmentManager)

        tabs.setupWithViewPager(mapViewPager)
    }
}

