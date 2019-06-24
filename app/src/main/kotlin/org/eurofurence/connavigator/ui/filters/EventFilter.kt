package org.eurofurence.connavigator.ui.filters

import android.os.Parcel
import android.os.Parcelable
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.database.Db
import java.util.*

// TODO: Parcelables are shit as well.

/**
 * Base class for parcelable event filters, as applied on [EventStream]s.
 */
sealed class EventFilter : Parcelable {
    /**
     * The disambiguation code for polymorphic deserialize.
     */
    abstract val code: Byte

    /**
     * Applies the filter with the [db] on the [eventStream], producing a new [eventStream].
     * @param db The database for cross referencing filters.
     * @param eventStream The event stream to filter.
     * @return Returns an event stream with the semantics of the filter applied.
     */
    abstract fun apply(db: Db, eventStream: EventStream): EventStream

    /**
     * Applies the filter via [apply] on the stream of [Db.events].
     * @param db The database to resolve from and to use for cross referencing filters.
     * @return Returns an event stream with the semantics of the filter applied.
     */
    fun resolve(db: Db) = apply(db, db.events.stream())
}

/**
 * Concatenates the filters, added to the existing filters of a filter chain.
 */
infix fun FilterChain.then(next: EventFilter) =
        FilterChain(filters + next)

/**
 * Concatenates the filters. If the receiver is a filter chain, the appropriate method is called.
 */
infix fun EventFilter.then(next: EventFilter) =
        if (this is FilterChain)
            then(next)
        else
            FilterChain(listOf(this, next))

/**
 * Filters by [title], as per [byTitle]
 * @property title The title to filter on, argument to [byTitle].
 */
data class FilterByTitle(val title: String) : EventFilter() {
    constructor(parcel: Parcel) : this(parcel.readString()
            ?: error("Inconsistent parcelable data."))

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.byTitle(title)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
    }

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterByTitle> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 1.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterByTitle(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterByTitle>(size)
    }
}

/**
 * Returns no element.
 */
class FilterNothing() : EventFilter() {
    constructor(parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream): EventStream =
            EventStream.empty<EventRecord>()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
    }

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterNothing> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 0.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterNothing(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterNothing>(size)
    }
}

/**
 * Filters by [dayId], as per [onDay]
 * @property dayId The day ID to filter on, argument to [onDay].
 */
data class FilterOnDay(val dayId: UUID) : EventFilter() {
    constructor(parcel: Parcel) : this(parcel.readSerializable() as? UUID
            ?: error("Inconsistent parcelable data."))

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.onDay(dayId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(dayId)
    }

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterOnDay> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 2.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterOnDay(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterOnDay>(size)
    }
}

/**
 * Filters by [trackId], as per [onTrack]
 * @property trackId The track ID to filter on, argument to [onTrack].
 */
data class FilterOnTrack(val trackId: UUID) : EventFilter() {
    constructor(parcel: Parcel) : this(parcel.readSerializable() as? UUID
            ?: error("Inconsistent parcelable data."))

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.onTrack(trackId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(trackId)
    }

    override fun describeContents(): Int = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterOnTrack> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 3.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterOnTrack(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterOnTrack>(size)
    }
}

/**
 * Filters by [roomId], as per [inRoom]
 * @property roomId The room ID to filter on, argument to [inRoom].
 */
data class FilterInRoom(val roomId: UUID) : EventFilter() {
    constructor(parcel: Parcel) : this(parcel.readSerializable() as? UUID
            ?: error("Inconsistent parcelable data."))

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.inRoom(roomId)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(roomId)
    }

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterInRoom> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 4.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterInRoom(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterInRoom>(size)
    }
}

/**
 * Orders by time, as per [orderTime].
 */
class OrderTime() : EventFilter() {
    constructor(@Suppress("unused_parameter") parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.orderTime()

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<OrderTime> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 5.toByte()

        override fun createFromParcel(parcel: Parcel) =
                OrderTime(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<OrderTime>(size)
    }
}

/**
 * Orders by name, as per [OrderName].
 */
class OrderName() : EventFilter() {
    constructor(@Suppress("unused_parameter") parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.orderName()

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<OrderName> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 6.toByte()

        override fun createFromParcel(parcel: Parcel) =
                OrderName(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<OrderName>(size)
    }
}

/**
 * Orders by day, then time, as per [orderDayAndTime].
 */
class OrderDayAndTime() : EventFilter() {
    constructor(@Suppress("unused_parameter") parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.orderDayAndTime()

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<OrderDayAndTime> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 7.toByte()

        override fun createFromParcel(parcel: Parcel) =
                OrderDayAndTime(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<OrderDayAndTime>(size)
    }
}

/**
 * Filters by upcoming events, as per [isUpcoming].
 */
class FilterIsUpcoming() : EventFilter() {
    constructor(@Suppress("unused_parameter") parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.isUpcoming()

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterIsUpcoming> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 8.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterIsUpcoming(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterIsUpcoming>(size)
    }
}

/**
 * Filters by favorited events, as per [isFavorited] applied on the database's [Db.faves].
 */
class FilterIsFavorited() : EventFilter() {
    constructor(@Suppress("unused_parameter") parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.isFavorited(db.faves)

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterIsFavorited> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 9.toByte()

        override fun createFromParcel(parcel: Parcel) = FilterIsFavorited(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterIsFavorited>(size)
    }
}

/**
 * Filters by current events, as per [isCurrent].
 */
class FilterIsCurrent() : EventFilter() {
    constructor(@Suppress("unused_parameter") parcel: Parcel) : this()

    override fun apply(db: Db, eventStream: EventStream) =
            eventStream.isCurrent()

    override fun writeToParcel(parcel: Parcel, flags: Int) = Unit

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterIsCurrent> {
        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 10.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterIsCurrent(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterIsCurrent>(size)
    }
}

/**
 * Applies multiple [filters] in sequence. If no filter is given, all events are returned.
 * @property filters The filters to apply in the sequence they are to be applied.
 */
data class FilterChain(val filters: List<EventFilter>) : EventFilter() {
    constructor(parcel: Parcel) : this(
            // Read the size of the list
            (0 until parcel.readInt()).map {
                when (val parcelCode = parcel.readByte()) {
                    FilterNothing.CODE -> FilterNothing.createFromParcel(parcel)
                    FilterByTitle.CODE -> FilterByTitle.createFromParcel(parcel)
                    FilterOnDay.CODE -> FilterOnDay.createFromParcel(parcel)
                    FilterOnTrack.CODE -> FilterOnTrack.createFromParcel(parcel)
                    FilterInRoom.CODE -> FilterInRoom.createFromParcel(parcel)
                    OrderTime.CODE -> OrderTime.createFromParcel(parcel)
                    OrderName.CODE -> OrderName.createFromParcel(parcel)
                    OrderDayAndTime.CODE -> OrderDayAndTime.createFromParcel(parcel)
                    FilterIsUpcoming.CODE -> FilterIsUpcoming.createFromParcel(parcel)
                    FilterIsFavorited.CODE -> FilterIsFavorited.createFromParcel(parcel)
                    FilterIsCurrent.CODE -> FilterIsCurrent.createFromParcel(parcel)
                    CODE -> createFromParcel(parcel)
                    else -> error("Filter parcel code $parcelCode is unknown.")
                }
            })

    override fun apply(db: Db, eventStream: EventStream) =
            filters.fold(eventStream) { l, r ->
                r.apply(db, l)
            }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(filters.size)
        for (f in filters) {
            parcel.writeByte(f.code)
            f.writeToParcel(parcel, flags)
        }
    }

    override fun describeContents() = 0

    override val code = CODE

    companion object CREATOR : Parcelable.Creator<FilterChain> {
        /**
         * An empty filter chain.
         */
        val empty = FilterChain(emptyList())

        /**
         * The disambiguation code used to read from a parcel.
         */
        const val CODE = 11.toByte()

        override fun createFromParcel(parcel: Parcel) =
                FilterChain(parcel)

        override fun newArray(size: Int) =
                arrayOfNulls<FilterChain>(size)
    }
}