@file:Suppress("unused")

package org.eurofurence.connavigator.ui.filters

import io.reactivex.Flowable
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.util.DatetimeProxy
import org.eurofurence.connavigator.util.extensions.fullTitle
import org.joda.time.DateTime
import org.joda.time.Interval
import java.util.*

/**
 * An observable of event records.
 */
typealias EventStream = Flowable<EventRecord>

/**
 * Filters on title by checking invariant case containment.
 */
fun EventStream.byTitle(title: String): EventStream =
        filter { it.fullTitle().contains(title, true) }

/**
 * Filters on the day ID.
 */
fun EventStream.onDay(dayId: UUID): EventStream =
        filter { it.conferenceDayId == dayId }

/**
 * Filters on the track ID.
 */
fun EventStream.onTrack(trackId: UUID): EventStream =
        filter { it.conferenceTrackId == trackId }

/**
 * Filters on the room ID.
 */
fun EventStream.inRoom(roomId: UUID): EventStream =
        filter { it.conferenceRoomId == roomId }

/**
 * Orders the stream by start time, then by end time.
 */
fun EventStream.orderTime(): EventStream =
        sorted(compareBy<EventRecord> {
            it.startDateTimeUtc
        }.thenBy {
            it.endDateTimeUtc
        })

/**
 * Orders the stream by name,
 */
fun EventStream.orderName(): EventStream =
        sorted(compareBy {
            it.fullTitle()
        })

/**
 * Milliseconds in a day, serves as a rough approximation
 * for [Date.getDay].
 */
const val millisInDay = 24 * 60 * 60 * 1000

/**
 * Orders by day first, then by start time, then by name.
 */
fun EventStream.orderDayAndTime(): EventStream =
        sorted(compareBy<EventRecord> {
            it.startDateTimeUtc.time / millisInDay
        }.thenBy {
            it.startDateTimeUtc
        }.thenBy {
            it.endDateTimeUtc
        }.thenBy {
            it.fullTitle()
        })

/**
 * Filters on starting within [now] and [upcomingTimeInMinutes] minutes from [now].
 */
fun EventStream.isUpcoming(now: DateTime = DatetimeProxy.now(), upcomingTimeInMinutes: Int = 30): EventStream =
        // Compute the interval that is considered as upcoming, reuse it in the filter.
        upcomingInterval().let { interval ->
            // Filter on start time contained in interval.
            filter {
                it.startsWithin(interval)
            }
        }

/**
 * Returns events that have an ID in [faves].
 */
fun EventStream.isFavorited(faves: Collection<UUID>): EventStream =
        filter {
            it.id in faves
        }

/**
 * Returns events that are running [now].
 */
fun EventStream.isCurrent(now: DateTime = DatetimeProxy.now()): EventStream =
        filter {
            Interval(it.startDateTimeUtc.time, it.endDateTimeUtc.time).contains(now)
        }

