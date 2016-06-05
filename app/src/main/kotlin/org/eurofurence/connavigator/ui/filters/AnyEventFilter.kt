package org.eurofurence.connavigator.ui.filters

import android.content.Context
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.ui.filters.intf.IEventFilter

/**
 * Created by David on 6/4/2016.
 */
class AnyEventFilter : IEventFilter {
    override fun filter(database: Database, filterVal: Any): Iterable<EventEntry> {
        return database.eventEntryDb.items
    override fun getTitle(): String = "All Events"
}