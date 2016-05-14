package org.eurofurence.connavigator.util.extensions

import io.swagger.client.model.EntityBase
import org.eurofurence.connavigator.store.DB
import org.eurofurence.connavigator.store.IDB
import org.eurofurence.connavigator.store.SyncIDB
import org.eurofurence.connavigator.store.cached

/**
 * Uses the [indexer] function to index the database receiver.
 */
fun <T> DB<T>.indexBy(indexer: (T) -> Any) = object : IDB<T>() {
    override fun delete() {
        this@indexBy.delete()
    }

    override fun id(item: T): Any = indexer(item)

    override var items: Iterable<T>
        get() = this@indexBy.items
        set(values) {
            this@indexBy.items = values
        }

    override var keyValues: Map<Any, T>
        get() = this@indexBy.items.associateBy { id(it) }
        set(values) {
            this@indexBy.items = values.values.toList()
        }

    override val time: Long?
        get() = this@indexBy.time
}

/**
 * Pairs the left indexed database with the right by using shared keys.
 */
infix fun <T, U> IDB<T>.pairWith(second: IDB<U>) =
        object : IDB<Pair<T?, U?>>() {
            override fun delete() {
                this@pairWith.delete()
                second.delete()
            }

            override val time: Long?
                get() = this@pairWith.time max second.time

            override fun id(item: Pair<T?, U?>): Any =
                    if (item.first != null)
                        this@pairWith.id(item.first!!)
                    else if (item.second != null)
                        second.id(item.second!!)
                    else throw IllegalArgumentException()

            override var keyValues: Map<Any, Pair<T?, U?>>
                get() {
                    // First where key not in second
                    val f = this@pairWith.keyValues
                            .filterKeys { it !in second.keyValues }
                            .mapValues { it.value to null }

                    // Second where key not in first
                    val s = second.keyValues
                            .filterKeys { it !in this@pairWith.keyValues }
                            .mapValues { null to it.value }

                    // Both where keys join
                    val c = this@pairWith.keyValues
                            .filterKeys { it in second.keyValues }
                            .mapValues { it.value to second.keyValues[it.key]!! }

                    return f + c + s
                }
                set(value) {
                    // Assign to first where the first field is assigned
                    this@pairWith.keyValues = value.filterValues { it.first == null }.mapValues { it.value.first!! }

                    // Assign to second where the second field is assigned
                    second.keyValues = value.filterValues { it.second == null }.mapValues { it.value.second!! }
                }

        }

/**
 * Indexes the receiver by [EntityBase]s identifier, caches it returns a [SyncIDB] marking by the deleted flag.
 */
fun <T : EntityBase> DB<T>.cachedApiDB() =
        SyncIDB(EntityBase::deleted, indexBy(EntityBase::getId).cached())

/**
 * Returns the size of the database. The size of the [IDB] does not change since
 * the association *must* be unique.
 */
val DB<*>.size: Int get() = items.let {
    if (it is Collection<*>)
        it.size
    else
        it.count()
}

/**
 * Accesses the item with the given key.
 */
operator fun<T> IDB<T>.get(key: Any?) = keyValues[key]
