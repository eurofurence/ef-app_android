package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.FavoriteFragmentStateAdapter
import org.eurofurence.connavigator.util.delegators.view

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment() {
    val favoritedViewPager by view(ViewPager::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_home, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Home")

        favoritedViewPager.adapter = FavoriteFragmentStateAdapter(childFragmentManager, Database(activity))
    }
}