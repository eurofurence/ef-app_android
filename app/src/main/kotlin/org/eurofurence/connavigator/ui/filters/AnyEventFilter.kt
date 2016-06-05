package org.eurofurence.connavigator.ui.filters

import android.content.Context
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.ui.filters.intf.IEventFilter

/**
 * Created by David on 6/4/2016.
 */
class AnyEventFilter : IEventFilter {
    override fun getTitle(): String = "All Events"

    override fun filter(context: Context, filterVal: Any): Iterable<EventEntry> {
        return Database(context).eventEntryDb.items
    }
}