package org.eurofurence.connavigator.ui.filters

import android.content.Context
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.joda.time.DateTime
import org.joda.time.Interval

/**
 * Created by David on 6/4/2016.
 */
class UpcomingEventFilter : IEventFilter {
    override fun getTitle(): String = "Upcoming Events"

    override fun filter(database: Database, filterVal: Any): Iterable<EventEntry> {
        val now = DateTime.now()
        val nowDate = now.toString("yyyy-MM-dd")

        // Get today, otherwise first day
        val closestEventDay = database.eventConferenceDayDb.items.find { it.date == nowDate } ?: database.eventConferenceDayDb.asc { it.date }.first()

        return database.eventEntryDb.items.filter { it.conferenceDayId == closestEventDay.id && database.eventIsUpcoming(it, now, 30)}.sortedBy { it.startTime }
    }
}