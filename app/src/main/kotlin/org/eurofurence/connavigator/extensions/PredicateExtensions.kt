package org.eurofurence.connavigator.extensions

/**
 * Negates a predicate by negating the result of the original applied
 */
operator fun <T> Function1<T, Boolean>.not(): (T) -> Boolean = { !this(it) }