package org.eurofurence.connavigator.ui.filters

import android.content.Context
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database

/**
 * Created by David on 6/4/2016.
 */
class AnyFavoritedEventFilter : IEventFilter {
    override fun getTitle(): String = "Favourited Events"

    override fun filter(database: Database, filterVal: Any): Iterable<EventEntry> {
        return database.favoritedDb.items
    }
}