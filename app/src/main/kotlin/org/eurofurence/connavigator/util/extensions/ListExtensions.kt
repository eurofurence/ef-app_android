package org.eurofurence.connavigator.util.extensions

/**
 * Apply filter to iterable, but only if condition is true
 */
fun <T> Iterable<T>.filterIf(condition: Boolean, pred: (T) -> Boolean) =
        if (condition) this.filter(pred) else this

fun <T> List<T>.limit(count: Int) =
        if(this.count() > count) this.subList(0, 24) else this