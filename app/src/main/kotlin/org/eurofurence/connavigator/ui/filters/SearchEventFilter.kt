package org.eurofurence.connavigator.ui.filters

import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.util.extensions.filterIf
import org.eurofurence.connavigator.util.extensions.limit

/**
 * Created by david on 7/17/16.
 */
class SearchEventFilter : IEventFilter {
    override fun filter(database: Database, filterVal: Any): Iterable<EventEntry> =
            database.eventEntryDb.items
                    .filterIf(
                            filterVal != Unit && filterVal != "",
                            { it.title.contains(filterVal.toString(), true) }
                    )
                    .distinctBy { it.title }
                    .sortedBy { it.title }
                    .limit(25)

    override fun getTitle(): String = "Search events"

}