package org.eurofurence.connavigator.store

import org.eurofurence.connavigator.util.extensions.not

/**
 * Synchronizable database.
 * @param objectId  Method generating an object identity
 * @param deleted Predicate checking if the object is a deleted object.
 * @param delegate The delegation database storing and loading the objects
 *
 */
class SyncIDB<T>(val deleted: (T) -> Boolean, val synced: IDB<T>) : IDB<T>() {
    override val time: Long?
        get() = synced.time

    override fun id(item: T): Any = synced.id(item)

    override var keyValues: Map<Any, T>
        get() = synced.keyValues
        set(values) {
            synced.keyValues = values
        }

    /**
     * Synchronizes a sequence of elements into the database.
     * @param other The other sequence, can contain only a partial delta, in which case all untouched elements remain in the store
     */
    fun syncWith(other: List<T>) = if (time != null) {
        // Get all keys of objects that were dropped
        val drop = other.filter(deleted).map { id(it) }.toSet()

        //Associate new elements that are not deleted by their identities
        val put = other.filter(!deleted).associateBy { id(it) }

        // Remove dropped and replaced elements in old list, add new values
        elements = (keyValues.filterKeys { it !in drop && it !in put.keys }.values + put.values)
    } else {
        // If the database does not exist yet, put all that are not deleted
        elements = other.filter(!deleted)
    }
}
