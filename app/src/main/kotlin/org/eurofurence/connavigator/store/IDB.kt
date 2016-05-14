package org.eurofurence.connavigator.store

/**
 * Represents an instance of a database where the primary focus lies on their indication, that
 * is, their representation as a pair of their key (index) and value.
 */
abstract class IDB<T> : DB<T> {
    /**
     * The element of the database.
     */
    override var items: Iterable<T>
        get() = keyValues.values
        set(values) {
            keyValues = values.associateBy { id(it) }
        }

    /**
     * The elements of the database in ascending order sorted by selector.
     */
    fun<R : Comparable<R>> asc(by: (T) -> R?) = items.sortedBy(by)

    /**
     * The elements of the database in descending order sorted by selector.
     */
    fun<R : Comparable<R>> desc(by: (T) -> R?) = asc(by).asReversed()

    /**
     * Identifies an item in the database.
     */
    abstract fun id(item: T): Any

    /**
     * The elements of the database.
     */
    abstract var keyValues: Map<Any, T>
}