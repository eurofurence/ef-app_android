package org.eurofurence.connavigator.ui.filters

import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.util.extensions.get
import java.util.*
import kotlin.collections.HashMap

/**
 * Wraps event entries and provides sorting for these. Filters are async
 */
class EventList(val database: Database) {
    val filters = HashMap<FilterType, Any>()
    val UPCOMING_TIME_IN_MINUTES = 30

    fun applyFilters(): List<EventEntry> {
        var events = database.eventEntryDb.items

        this.filters.keys.forEach { key ->
            when (key) {
                FilterType.ON_DAY -> events = events.filter { it -> it.conferenceDayId == filters[key] }
                FilterType.ON_TRACK -> events = events.filter { it -> it.conferenceTrackId == filters[key] }
                FilterType.IN_ROOM -> events = events.filter { it -> it.conferenceRoomId == filters[key] }
                FilterType.BY_TITLE -> events = events.filter { it -> it.title.contains(filters[key] as String, true) }
                FilterType.IS_UPCOMING -> events = events.filter { database.eventIsUpcoming(it, org.joda.time.DateTime.now(), UPCOMING_TIME_IN_MINUTES) }
                FilterType.IS_FAVORITED -> events = events.filter { database.favoritedDb[it.id] != null }
                FilterType.IS_CURRENT -> events = events.filter { database.eventIsHappening(it, org.joda.time.DateTime.now()) }
                FilterType.ORDER_START_TIME -> events = events.sortedBy { it.startTime }.sortedBy { it.endTime }
            }
        }

        return events.toList()
    }

    /**
     * Filter by a date
     */
    fun onDay(dayID: UUID) =
            this.apply { filters[FilterType.ON_DAY] = dayID }

    /**
     * Filter by room
     */
    fun inRoom(roomID: UUID) =
            this.apply { filters[FilterType.IN_ROOM] = roomID }

    /**
     * Filter by track
     */
    fun onTrack(trackId: UUID) =
            this.apply { filters[FilterType.ON_TRACK] = trackId }

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
}

enum class FilterType {
    BY_TITLE,
    ON_DAY,
    ON_TRACK,
    ORDER_START_TIME,
    IN_ROOM,
    IS_UPCOMING,
    IS_FAVORITED,
    IS_CURRENT,

}