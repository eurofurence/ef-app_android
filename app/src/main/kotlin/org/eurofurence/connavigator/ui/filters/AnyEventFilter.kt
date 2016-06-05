package org.eurofurence.connavigator.ui.filters

import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database

/**
 * Created by David on 6/4/2016.
 */
class AnyEventFilter : IEventFilter {
    override fun filter(database: Database, filterVal: Any): Iterable<EventEntry> =
            database.eventEntryDb.items

    override fun getTitle(): String = "All Events"
}