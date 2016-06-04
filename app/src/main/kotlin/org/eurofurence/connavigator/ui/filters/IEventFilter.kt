package org.eurofurence.connavigator.ui.filters

import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database

/**
 * Created by David on 6/4/2016.
 */
interface IEventFilter {
    fun filter(database: Database, filterVal: Any = Unit): Iterable<EventEntry>
}