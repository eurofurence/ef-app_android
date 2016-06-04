package org.eurofurence.connavigator.ui.filters

import android.content.Context
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database

/**
 * Created by David on 6/4/2016.
 */
class AnyEventFilter : IEventFilter {
    override fun filter(context: Context, filterVal: Any): Iterable<EventEntry> {
        return Database(context).eventEntryDb.items
    }
}