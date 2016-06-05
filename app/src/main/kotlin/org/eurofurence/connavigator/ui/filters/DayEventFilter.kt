package org.eurofurence.connavigator.ui.filters

import android.content.Context
import com.google.common.base.Preconditions
import io.swagger.client.model.EventConferenceDay
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.ui.filters.intf.IEventFilter

/**
 * Created by David on 6/4/2016.
 */
class DayEventFilter : IEventFilter {
    override fun getTitle(): String = ""

    override fun filter(context: Context, filterVal: Any): Iterable<EventEntry> {
        Preconditions.checkArgument(EventConferenceDay::class.java.isAssignableFrom(filterVal.javaClass), "Filter value is not an eventConference day!")

        val day = filterVal as EventConferenceDay
        return Database(context).eventEntryDb.items.filter { it.conferenceDayId == day.id }
    }
}