package org.eurofurence.connavigator.ui.filters

import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.util.DatetimeProxy
import org.joda.time.DateTime
import org.joda.time.Interval

// See also ModelExtensions

/**
 * Returns an interval from [now] to [upcomingTimeInMinutes] minutes in the
 * future.
 */
fun upcomingInterval(now: DateTime = DatetimeProxy.now(), upcomingTimeInMinutes: Int = 30) =
        Interval(now, now.plusMinutes(upcomingTimeInMinutes))


/**
 * True if the event starts within the given time.
 * @param interval The interval to check against.
 */
fun EventRecord.startsWithin(interval: Interval) =
        interval.contains(startDateTimeUtc.time)

/**
 * True if the event ends within the given time.
 * @param interval The interval to check against.
 */
fun EventRecord.endsWithin(interval: Interval) =
        interval.contains(endDateTimeUtc.time)

/**
 * Gets the interval the event is happening in.
 */
val EventRecord.interval
    get() = Interval(
            startDateTimeUtc.time,
            endDateTimeUtc.time)

/**
 * True if the [date] is during the event.
 */
operator fun EventRecord.contains(date: DateTime) =
        interval.contains(date)

/**
 * Returns the start date/time as a [DateTime].
 */
val EventRecord.start
    get() = DateTime(startDateTimeUtc.time)

/**
 * Returns the end date/time as a [DateTime].
 */
val EventRecord.end
    get() = DateTime(endDateTimeUtc.time)

/**
 * True if the event is happening right now.
 */
val EventRecord.isHappening
    get() = DateTime.now() in this

val EventRecord.isUpcoming
    get() = startsWithin(upcomingInterval())