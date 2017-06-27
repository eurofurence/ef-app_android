package org.eurofurence.connavigator.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.filterEvents
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment

/**
 * Created by David on 5/14/2016.
 */
class FavoriteFragmentStateAdapter(val fragmentManager: FragmentManager, override val db: Db)
    : FragmentStatePagerAdapter(fragmentManager), HasDb {
    override fun getItem(position: Int): Fragment? {
        return EventRecyclerFragment(filterEvents().isFavorited())
    }

    override fun getCount(): Int {
        return 1
    }
}
