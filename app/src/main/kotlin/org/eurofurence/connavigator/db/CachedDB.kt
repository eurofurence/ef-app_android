package org.eurofurence.connavigator.db

/**
 * The cached DB delegates to a backend, on setting, the cache will be replaced, on
 * getting it will be loaded or reused.
 */
class CachedDB<T>(val delegate: DB<T>) : DB<T> by delegate {
    /**
     * Cache of the elements, will be assigned to the backing on get and overwritten on set
     */
    var cache: List<T>? = null

    override var elements: List<T>
        get() = if (cache == null) {
            // Assign the cache if not yet assigned
            cache = delegate.elements
            cache!!
        } else
            cache!!

        set(values) {
            // Overwrite the cache
            cache = values
            delegate.elements = values
        }
}


/**
 * Creates a cached database for arbitrary objects.
 */
fun <T> DB<T>.cachedDB() = CachedDB(this)
