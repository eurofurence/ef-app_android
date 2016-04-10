package org.eurofurence.connavigator.util.extensions

/**
 * Negates a predicate by negating the result of the original applied
 */
operator fun <T> Function1<T, Boolean>.not(): (T) -> Boolean = { !this(it) }