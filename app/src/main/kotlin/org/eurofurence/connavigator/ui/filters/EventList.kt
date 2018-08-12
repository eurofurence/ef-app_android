@file:Suppress("unused")

package org.eurofurence.connavigator.ui.filters

import com.google.firebase.perf.metrics.AddTrace
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.eventIsHappening
import org.eurofurence.connavigator.database.eventIsUpcoming
import org.eurofurence.connavigator.ui.filters.FilterType.*
import org.eurofurence.connavigator.util.extensions.fullTitle
import org.joda.time.DateTime
import java.util.*
import kotlin.Comparator
import kotlin.collections.set

/**
 * Wraps event entries and provides sorting for these. Filters are async
 */
class EventList(override val db: Db) : HasDb {
    val filters = HashMap<FilterType, String>()
    private val upcomingTimeInMinutes = 30

    @AddTrace(name = "EventList:applyEventFilters", enabled = true)
    fun applyFilters(): List<EventRecord> {
        val events = events.items.toMutableList()

        val now = DateTime.now()
        for ((k, v) in filters)
            when (k) {
                BY_TITLE -> events.removeAll { !it.fullTitle().contains(v, true) }

                ON_DAY -> events.removeAll { it.conferenceDayId.toString() != v }
                ON_TRACK -> events.removeAll { it.conferenceTrackId.toString() != v }
                IN_ROOM -> events.removeAll { it.conferenceRoomId.toString() != v }

                ORDER_START_TIME -> events.sortWith(Comparator { a, b ->
                    a.startDateTimeUtc.compareTo(b.startDateTimeUtc).takeIf { it != 0 }
                            ?: a.endDateTimeUtc.compareTo(b.endDateTimeUtc)
                })

                ORDER_DAY -> events.sortBy { it.startDateTimeUtc.time / (24 * 60 * 60 * 1000) }
                ORDER_NAME -> events.sortBy { it.fullTitle() }
                ORDER_DAY_AND_TIME -> events.sortWith(Comparator { a, b ->
                    val dayA = a.startDateTimeUtc.time / (24 * 60 * 60 * 1000)
                    val dayB = b.startDateTimeUtc.time / (24 * 60 * 60 * 1000)

                    dayA.compareTo(dayB).takeIf { it != 0 }
                            ?: a.startDateTimeUtc.compareTo(b.startDateTimeUtc).takeIf { it != 0 }
                            ?: a.endDateTimeUtc.compareTo(b.endDateTimeUtc).takeIf { it != 0 }
                            ?: a.fullTitle().compareTo(b.fullTitle())
                })


                IS_UPCOMING -> events.removeAll {
                    !eventIsUpcoming(it, now, upcomingTimeInMinutes)
                }
                IS_FAVORITED -> events.removeAll {
                    it.id !in faves
                }
                IS_CURRENT -> events.removeAll {
                    !eventIsHappening(it, now)
                }
            }

        return events.toList()
    }

    /**
     * Filter by a date
     */
    fun onDay(dayID: UUID) =
            this.apply { filters[FilterType.ON_DAY] = dayID.toString() }

    /**
     * Filter by room
     */
    fun inRoom(roomID: UUID) =
            this.apply { filters[FilterType.IN_ROOM] = roomID.toString() }

    /**
     * Filter by track
     */
    fun onTrack(trackId: UUID) =
            this.apply { filters[FilterType.ON_TRACK] = trackId.toString() }

    /**
     * Search by title
     */
    fun byTitle(title: String) =
            this.apply { filters[FilterType.BY_TITLE] = title }

    /**
     * Filter by favourites
     */
    fun isFavorited() =
            this.apply { filters[FilterType.IS_FAVORITED] = "" }

    /**
     * Filter on current events
     */
    fun isCurrent() =
            this.apply { filters[FilterType.IS_CURRENT] = "" }

    /**
     * Filter to upcoming events
     */
    fun isUpcoming() =
            this.apply { filters[FilterType.IS_UPCOMING] = "" }

    /**
     * Sort events by date
     */
    fun sortByStartTime() =
            this.apply { filters[FilterType.ORDER_START_TIME] = "" }

    fun sortByDateAndTime() = this.apply { filters[FilterType.ORDER_DAY_AND_TIME] = "" }

    fun sortByDate() =
            this.apply { filters[FilterType.ORDER_DAY] = "" }

    fun sortByName() =
            this.apply { filters[FilterType.ORDER_NAME] = "" }
}

enum class FilterType {
    BY_TITLE,
    ON_DAY,
    ON_TRACK,
    ORDER_START_TIME,
    ORDER_DAY,
    ORDER_NAME,
    ORDER_DAY_AND_TIME,
    IN_ROOM,
    IS_UPCOMING,
    IS_FAVORITED,
    IS_CURRENT,
}