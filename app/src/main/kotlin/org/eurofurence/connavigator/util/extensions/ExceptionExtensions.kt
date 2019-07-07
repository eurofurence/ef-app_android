@file:Suppress("unused")

package org.eurofurence.connavigator.util.extensions

import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.util.Choice
import org.eurofurence.connavigator.util.Dispatcher
import org.eurofurence.connavigator.util.left
import org.eurofurence.connavigator.util.right

/**
 * Utilities for handling exceptions with automatic analytics handling.
 * Created by pazuzu on 4/10/17.
 */

/**
 * Handler proxy, this callback is invoked on exceptions in [catchAlternative], [catchToNull] and [catchToFalse].
 */
val proxyException = Dispatcher<Throwable>().apply {
    this += { _: Throwable -> /* pass */ }
}

/**
 * Catches and dismisses an exception, should not be used but have it anyway.
 * @param R Type of the return value
 * @param block The block to execute
 */
inline fun <reified R> dismissExceptionIn(block: () -> R) {
    try {
        block()
    } catch(_: Throwable) {
    }
}


@kotlin.jvm.JvmName("blockUnit")
inline fun <reified T> block(noinline block: (T) -> Unit) = block

@kotlin.jvm.JvmName("blockReturn")
inline fun <reified T, reified U> block(noinline block: (T) -> U) = block

@kotlin.jvm.JvmName("handlerUnit")
fun block(block: (Unit) -> Unit) = block

@kotlin.jvm.JvmName("handlerReturn")
inline fun <reified U> block(noinline block: (Unit) -> U) = block

/**
 * Catches an exception of type [E] in the receiver.
 *
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 */
inline infix fun <reified E : Throwable> (() -> Unit).catchHandle(handler: (E) -> Unit) {
    try {
        this()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
    }
}

/**
 * Catches an exception of type [E] in the receiver. If the receiver succeeded, uses it's return value, otherwise
 * uses the [handler]s return value.
 *
 * @param R Type of the shared return value
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns the return value of the receiver if no exception occurred, otherwise returns the handlers return
 * value
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchAlternative(handler: (E) -> R): R {
    return try {
        this()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
    }
}

/**
 * Catches an exception of type [E] in the receiver. If the receiver succeeded, uses it's return value, otherwise
 * returns null.
 *
 * @param R Type of the shared return value
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns the return value of the receiver if no exception occurred, otherwise returns null
 * value
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchToNull(handler: (E) -> Unit): R? {
    return try {
        this()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
        null
    }
}

/**
 * Catches an exception of type [E] in the [block]. If the [block] succeeded, uses it's return value, otherwise
 * returns null. Exceptions are just handled by logging with [AnalyticsService].
 *
 * @param R Type of the shared return value
 * @param E Type of the exception
 * @param block The block to execute that might throw an exception
 * @return Returns the return value of the [block] if no exception occurred, otherwise returns null
 * value
 */
inline fun <reified R, reified E : Throwable> catchToNull(block: () -> R): R? {
    return try {
        block()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        null
    }
}

/**
 * Catches an exception of type [E] in the receiver. If [R] is boolean, returns the result of the receiver if succeeded,
 * otherwise returns true if the receiver succeeded.
 *
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns true only if the receiver succeeded and returned true.
 */
inline infix fun <reified E : Throwable, reified R> (() -> R).catchToFalse(handler: (E) -> Unit): Boolean {
    return try {
        this() as? Boolean ?: true
    } catch(t: Throwable) {
        if (t !is E)
            throw t
        proxyException(t)
        handler(t)
        false
    }
}

/**
 * Catches an exception of type [E] in the [block]. If [R] is boolean, returns the result of the [block] if succeeded,
 * otherwise returns true if the [block] succeeded. Exceptions are just handled by logging with [proxyException].
 *
 * @param E Type of the exception
 * @param block The block to execute that might throw an exception
 * @return Returns true only if the [block] succeeded and returned true.
 */
inline fun <reified E : Throwable, reified R> catchToFalse(block: () -> R): Boolean {
    return try {
        block() as? Boolean ?: true
    } catch(t: Throwable) {
        if (t !is E)
            throw t
        proxyException(t)
        false
    }
}


/**
 * Catches an exception in the [block]. If [R] is boolean, returns the result of the [block] if succeeded, otherwise
 * returns true if the [block] succeeded. Exceptions are just handled by logging with [proxyException].
 *
 * @param block The block to execute that might throw an exception
 * @return Returns true only if the [block] succeeded.
 */
inline fun <reified R> catchToAnyFalse(block: () -> R): Boolean {
    return try {
        block() as? Boolean ?: true
    } catch(t: Throwable) {
        proxyException(t)
        false
    }
}

/**
 * Catches an exception in the [block]. Returns true only if the [block] succeeded and returned true.
 * Exceptions are just handled by logging with [proxyException].
 *
 * @param block The block to execute that might throw an exception
 * @return Returns true only if the [block] succeeded and returned true.
 */
@kotlin.jvm.JvmName("catchToAnyFalseFromReturn")
inline fun catchToAnyFalse(block: () -> Boolean): Boolean {
    return try {
        block()
    } catch(t: Throwable) {
        proxyException(t)
        false
    }
}

/**
 * Catches an exception of type [E] in the receiver. Returns a choice of either receivers return value or the
 * exception itself on failure.
 *
 * @param R Type of the return value
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns a choice of either receivers return value or the exception itself on failure.
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchToChoice(handler: (E) -> Unit): Choice<R, E> {
    return try {
        left(this())
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
        right(t)
    }
}


/**
 * Catches an exception of type [E] in the [block]. Returns a choice of either [block]s return value or the
 * exception itself on failure.  Exceptions are just handled by logging with [proxyException].
 *
 * @param R Type of the return value
 * @param E Type of the exception
 * @param block The block to execute that might throw an exception
 * @return Returns a choice of either [block]s return value or the exception itself on failure.
 */
inline fun <reified R, reified E : Throwable> catchToChoice(block: () -> R): Choice<R, E> {
    return try {
        left(block())
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        right(t)
    }
}


/**
 * Catches an exception of type [E] in the receiver. Returns the exception or null if succeeded.
 *
 * @param R Type of the return value
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns the exception or null if succeeded.
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchToException(handler: (E) -> Unit): E? {
    return try {
        this()
        null
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
        t
    }
}


/**
 * Catches an exception of type [E] in the receiver. Returns the exception or null if succeeded.
 *
 * @param R Type of the return value
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @return Returns the exception or null if succeeded.
 */
inline fun <reified R, reified E : Throwable> catchToException(block: () -> R): E? {
    return try {
        block()
        null
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        t
    }
}

/**
 * Substitute for [catchToException] for any exception that is thrown.
 */
inline fun <reified R> catchToAnyException(block: () -> R): Throwable? {
    return catchToException(block)
}

/**
 * Utility to write tests
 */
private infix fun Any?.shouldBe(value: Any?) {
    if (this != value)
        throw IllegalStateException("$this should be $value")
}

/**
 * Utility to write tests
 */
private inline infix fun <reified R> R.shouldSatisfy(predicate: (R) -> Boolean) {
    if (!predicate(this))
        throw IllegalStateException("$this should satisfy condition")
}
