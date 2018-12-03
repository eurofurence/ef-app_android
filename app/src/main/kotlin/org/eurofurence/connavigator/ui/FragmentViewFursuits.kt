package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.viewPager

/**
 * Created by requinard on 7/24/17.
 */
class FragmentViewFursuits : Fragment(), MainScreen {
    override val drawerItemId: Int
        get() = R.id.navFursuitGames

    val ui = FursuitsUi()

    inner class FursuitPagerAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getPageTitle(position: Int) = when(position){
            0 -> "Collect"
            1 -> "My collection"
            else -> "Collect"
        }

        override fun getItem(position: Int): Fragment = when (position) {
            0 -> FragmentViewFursuitGame()
            1 -> FragmentViewFursuitCollected()
            else -> FragmentViewFursuitGame()
        }

        override fun getCount() = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.pager.adapter = FursuitPagerAdapter()

        applyOnRoot { tabs.setupWithViewPager(ui.pager) }
    }
}

class FursuitsUi : AnkoComponent<Fragment> {
    lateinit var pager: ViewPager
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        relativeLayout {
            pager = viewPager {
                id = R.id.eventPager
            }
        }
    }

}