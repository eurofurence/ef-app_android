package org.eurofurence.connavigator.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.ui.filters.EnumEventRecyclerViewmode
import org.eurofurence.connavigator.ui.filters.EventFilterFactory
import org.eurofurence.connavigator.ui.fragments.EmptyCard
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment

/**
 * Created by David on 5/14/2016.
 */
class FavoriteFragmentStateAdapter(val fragmentManager: FragmentManager, val database: Database) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment? {
        return EventRecyclerFragment(EventFilterFactory.create(EnumEventRecyclerViewmode.FAVORITED))
    }

    override fun getCount(): Int {
        return 1
    }
}
