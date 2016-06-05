package org.eurofurence.connavigator.ui.filters

import com.google.common.base.Preconditions
import io.swagger.client.model.EventConferenceDay
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database

/**
 * Created by David on 6/4/2016.
 */
class DayEventFilter : IEventFilter {
    override fun getTitle(): String = ""
    override val scrolling: Boolean
        get() = true

    override fun filter(database: Database, filterVal: Any): Iterable<EventEntry> {
        Preconditions.checkArgument(EventConferenceDay::class.java.isAssignableFrom(filterVal.javaClass), "Filter value is not an eventConference day!")

        val day = filterVal as EventConferenceDay
        return database.eventEntryDb.items.filter { it.conferenceDayId == day.id }
    }
}