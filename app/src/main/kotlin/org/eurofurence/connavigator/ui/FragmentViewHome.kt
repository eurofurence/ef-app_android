package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.adapter.FavoriteFragmentStateAdapter
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment() {
    val favoritedViewPager by view(ViewPager::class.java)
    val announcementsRecycler by view(RecyclerView::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_home, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.changeScreenName("View Home")

        val database = Database(activity)
        applyOnRoot { changeTitle("Home") }
        favoritedViewPager.adapter = FavoriteFragmentStateAdapter(childFragmentManager, database)

        announcementsRecycler.adapter = AnnoucementRecyclerDataAdapter(database.announcementDb.items.toList())
        announcementsRecycler.layoutManager = LinearLayoutManager(activity)
        announcementsRecycler.itemAnimator = DefaultItemAnimator()
    }
}