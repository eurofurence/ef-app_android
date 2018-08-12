package org.eurofurence.connavigator.util.extensions

/**
 * Apply filter to iterable, but only if condition is true
 */
fun <T> Iterable<T>.filterIf(condition: Boolean, predicate: (T) -> Boolean) =
        if (condition) this.filter(predicate) else this