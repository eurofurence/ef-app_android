package org.eurofurence.connavigator.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.ui.fragments.EventCard
import org.eurofurence.connavigator.util.extensions.get

/**
 * Created by David on 5/14/2016.
 */
class FavoriteFragmentStateAdapter(val fragmentManager: FragmentManager, val database: Database) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getItem(position: Int): Fragment? {
        return EventCard(database.favoritedDb.asc { database.eventConferenceDayDb[it.conferenceDayId]?.date + it.startTime }.elementAt(position))
    }

    override fun getCount(): Int {
        return database.favoritedDb.items.count()
    }
}
