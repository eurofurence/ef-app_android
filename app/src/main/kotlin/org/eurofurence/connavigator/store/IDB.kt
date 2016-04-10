package org.eurofurence.connavigator.store

/**
 * Represents an instance of a database where the primary focus lies on their indication, that
 * is, their representation as a pair of their key (index) and value.
 */
abstract class IDB<T> : DB<T> {
    /**
     * The element of the database.
     */
    override var elements: List<T>
        get() = keyValues.values.toList()
        set(values) {
            keyValues = values.associateBy { id(it) }
        }

    /**
     * Identifies an item in the database.
     */
    abstract fun id(item: T): Any

    /**
     * The elements of the database.
     */
    abstract var keyValues: Map<Any, T>
}