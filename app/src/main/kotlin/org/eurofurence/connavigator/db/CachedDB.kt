package org.eurofurence.connavigator.db

/**
 * The cached DB delegates to a backend, on setting, the cache will be replaced, on
 * getting it will be loaded or reused.
 */
fun <T> DB<T>.cached() = let {
    object : DB<T> {
        var cache: List<T>? = null

        override val exists: Boolean
            get() = it.exists

        override var elements: List<T>
            get() = if (cache == null) {
                // Assign the cache if not yet assigned
                cache = it.elements
                cache!!
            } else
                cache!!

            set(values) {
                // Overwrite the cache
                cache = values
                it.elements = values
            }

    }
}

/**
 * The cached DB delegates to a backend, on setting, the cache will be replaced, on
 * getting it will be loaded or reused.
 */
fun <T> IDB<T>.cached() = let {
    object : IDB<T>() {
        override fun id(item: T): Any = it.id(item)

        var cache: Map<Any, T>? = null

        override val exists: Boolean
            get() = it.exists
        override var keyValues: Map<Any, T>
            get() = if (cache == null) {
                // Assign the cache if not yet assigned
                cache = it.keyValues
                cache!!
            } else
                cache!!
            set(values) {
                // Overwrite the cache
                cache = values
                it.keyValues = values
            }

    }
}