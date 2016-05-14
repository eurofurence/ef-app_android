package org.eurofurence.connavigator.store

/**
 * A database on objects of type [T].
 */
interface DB<T> {
    /**
     * Deletes the database
     */
    fun delete()

    /**
     * A time stamp, or null if the backend does not exist
     */
    val time: Long?
    /**
     * The elements of the database
     */
    var items: Iterable<T>
}

