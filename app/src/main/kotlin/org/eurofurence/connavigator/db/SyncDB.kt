package org.eurofurence.connavigator.db

import io.swagger.client.model.EntityBase
import org.eurofurence.connavigator.extensions.not
import java.math.BigInteger

/**
 * Synchronizable database.
 * @param objectId  Method generating an object identity
 * @param deleted Predicate checking if the object is a deleted object.
 * @param delegate The delegation database storing and loading the objects
 *
 */
class SyncDB<T>(val objectId: (T) -> Any, val deleted: (T) -> Boolean, delegate: DB<T>) : DB<T> by delegate {
    /**
     * Synchronizes a sequence of elements into the database.
     * @param other The other sequence, can contain only a partial delta, in which case all untouched elements remain in the store
     */
    fun syncWith(other: List<T>) = if (exists) {
        // Associate current elements by their identities
        val assoc = elements.associateBy(objectId)

        // Get all keys of objects that were dropped
        val drop = other.filter(deleted).map(objectId).toSet()

        //Associate new elements that are not deleted by their identities
        val put = other.filter(!deleted).associateBy(objectId)

        // Remove dropped and replaced elements in old list, add new values
        elements = (assoc.filterKeys { it !in drop && it !in put.keys }.values + put.values)
    } else {
        // If the database does not exist yet, put all that are not deleted
        elements = other.filter(!deleted)
    }
}

/**
 * Creates a synchronizable database for [EntityBase] objects.
 * @param delegate The delegation database storing and loading the objects
 */
fun <T : EntityBase> efSyncDB(delegate: DB<T>) = SyncDB(EntityBase::getId, { it.isDeleted == BigInteger.ONE }, delegate)