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
