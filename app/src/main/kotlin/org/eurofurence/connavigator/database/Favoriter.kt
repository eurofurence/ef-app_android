package org.eurofurence.connavigator.database

import com.google.android.gms.analytics.HitBuilders
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.logv
import java.util.*

/**
 * Created by David on 5/14/2016.
 */
object Favoriter {
    /**
     * Handles logic for favoriting events
     * Return: True if element was inserted, false is element was removed
     */
    fun event(database: Database, eventEntry: EventEntry): Boolean {
        logv { "Favoriting event" }

        Analytics.trackEvent(
                HitBuilders.EventBuilder()
                        .setAction("Favorited event %s; uid %s".format(eventEntry.title, eventEntry.id))
                        .setLabel("Event")
                        .setCategory("Favorited")
        )

        if (database.favoritedDb.items.contains(eventEntry)) {
            logv { "Removing event %s".format(eventEntry.title) }
            database.favoritedDb.items = database.favoritedDb.items.filter { it.id != eventEntry.id }
            return false
        } else {
            logv { "Entering event %s".format(eventEntry.title) }
            val newFavourited = LinkedList<EventEntry>()
            newFavourited.addAll(database.favoritedDb.items)
            newFavourited.add(eventEntry)

            database.favoritedDb.items = newFavourited
            return true
        }
    }
}