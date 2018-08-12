package org.eurofurence.connavigator.util

import org.eurofurence.connavigator.util.extensions.catchToAnyException

/**
 * Event bus.
 */
class Dispatcher<T> {
    /**
     * Registered dipatchers
     */
    private val dispatchers = mutableListOf<(T) -> Unit>()

    /**
     * Registers [handler] with the dispatcher.
     * @param handler The handler to add
     */
    operator fun plusAssign(handler: (T) -> Unit) {
        dispatchers += handler
    }

    /**
     * Invokes the the handlers,
     */
    operator fun invoke(event: T): List<Throwable?> =
            dispatchers.map { catchToAnyException { it(event) } }

}