package org.eurofurence.connavigator.util.extensions

import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.Choice
import org.eurofurence.connavigator.util.Dispatcher

/**
 * Utilities for handling exceptions with automatic analytics handling.
 * Created by pazuzu on 4/10/17.
 */

/**
 * Handler proxy, this callback is invoked on exceptions in [catchAlternative], [catchToNull] and [catchToFalse].
 */
val proxyException = Dispatcher<Throwable>().apply {
    this += { ex: Throwable -> Analytics.exception(ex) }
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
 * @sample catchAlternativeExample
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchAlternative(handler: (E) -> R): R {
    try {
        return this()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        return handler(t)
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
 * @sample catchToNullExample
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchToNull(handler: (E) -> Unit): R? {
    try {
        return this()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
        return null
    }
}

/**
 * Catches an exception of type [E] in the [block]. If the [block] succeeded, uses it's return value, otherwise
 * returns null. Exceptions are just handled by logging with [Analytics].
 *
 * @param R Type of the shared return value
 * @param E Type of the exception
 * @param block The block to execute that might throw an exception
 * @return Returns the return value of the [block] if no exception occurred, otherwise returns null
 * value
 * @sample catchToNullExample
 */
inline fun <reified R, reified E : Throwable> catchToNull(block: () -> R): R? {
    try {
        return block()
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        return null
    }
}

/**
 * Catches an exception of type [E] in the receiver. Returns true only if the receiver succeeded.
 *
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns true only if the receiver succeeded.
 * @sample catchToFalseFromTrueExample
 */
@kotlin.jvm.JvmName("catchToFalseFromTrue")
inline infix fun <reified E : Throwable> (() -> Unit).catchToFalse(handler: (E) -> Unit): Boolean {
    try {
        this()
        return true
    } catch(t: Throwable) {
        if (t !is E)
            throw t
        proxyException(t)
        handler(t)
        return false
    }
}

/**
 * Catches an exception of type [E] in the receiver. Returns true only if the receiver succeeded and returned true.
 *
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @param handler The handler to execute on an exception
 * @return Returns true only if the receiver succeeded and returned true.
 * @sample catchToFalseFromReturnExample
 */
@kotlin.jvm.JvmName("catchToFalseFromReturn")
inline infix fun <reified E : Throwable> (() -> Boolean).catchToFalse(handler: (E) -> Unit): Boolean {
    try {
        return this()
    } catch(t: Throwable) {
        if (t !is E)
            throw t
        proxyException(t)
        handler(t)
        return false
    }
}

/**
 * Catches an exception of type [E] in the [block]. Returns true only if the [block] succeeded. Exceptions are just
 * handled by logging with [proxyException].
 *
 * @param E Type of the exception
 * @param block The block to execute that might throw an exception
 * @return Returns true only if the [block] succeeded.
 * @sample catchToFalseFromTrueExample
 */
@kotlin.jvm.JvmName("catchToFalseFromTrue")
inline fun <reified E : Throwable> catchToFalse(block: () -> Unit): Boolean {
    try {
        block()
        return true
    } catch(t: Throwable) {
        if (t !is E)
            throw t
        proxyException(t)
        return false
    }
}

/**
 * Catches an exception of type [E] in the [block]. Returns true only if the [block] succeeded and returned true.
 * Exceptions are just handled by logging with [proxyException].
 *
 * @param E Type of the exception
 * @param block The block to execute that might throw an exception
 * @return Returns true only if the [block] succeeded and returned true.
 * @sample catchToFalseFromReturnExample
 */
@kotlin.jvm.JvmName("catchToFalseFromReturn")
inline fun <reified E : Throwable> catchToFalse(block: () -> Boolean): Boolean {
    try {
        return block()
    } catch(t: Throwable) {
        if (t !is E)
            throw t
        proxyException(t)
        return false
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
 * @sample catchToChoiceExample
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchToChoice(handler: (E) -> Unit): Choice<R, E> {
    try {
        return Choice.pri(this())
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
        return Choice.snd(t)
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
 * @sample catchToChoiceExample
 */
inline fun <reified R, reified E : Throwable> catchToChoice(block: () -> R): Choice<R, E> {
    try {
        return Choice.pri(block())
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        return Choice.snd(t)
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
 * @sample catchToExceptionExample
 */
inline infix fun <reified R, reified E : Throwable> (() -> R).catchToException(handler: (E) -> Unit): E? {
    try {
        this()
        return null
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        handler(t)
        return t
    }
}


/**
 * Catches an exception of type [E] in the receiver. Returns the exception or null if succeeded.
 *
 * @param R Type of the return value
 * @param E Type of the exception
 * @receiver The block to execute that might throw an exception
 * @return Returns the exception or null if succeeded.
 * @sample catchToExceptionExample
 */
inline fun <reified R, reified E : Throwable> catchToException(block: () -> R): E? {
    try {
        block()
        return null
    } catch(t: Throwable) {
        if (t !is E)
            throw t

        proxyException(t)
        return t
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

/**
 * Catch and handle exception, use return value of handler block on failure.
 */
fun catchAlternativeExample() {
    {
        val success = 1 / 1
        "Success"
    } catchAlternative { _: ArithmeticException ->
        "Fail"
    } shouldBe "Success"

    {
        val fail = 1 / 0
        "Success"
    } catchAlternative { _: ArithmeticException ->
        "Fail"
    } shouldBe "Fail"
}

/**
 * Catch and handle exception, use null on failure.
 */
fun catchToNullExample() {
    {
        val success = 1 / 1
        "Success"
    } catchToNull { _: ArithmeticException ->
    } shouldBe "Success"

    {
        val fail = 1 / 0
        "Success"
    } catchToNull { _: ArithmeticException ->
    } shouldBe null

}

/**
 * Catch and handle exception, use false on failure.
 */
fun catchToFalseFromReturnExample() {
    {
        val success = 1 / 1
        true
    } catchToFalse { _: ArithmeticException ->
    } shouldBe true

    {
        val fail = 1 / 0
        true
    } catchToFalse { _: ArithmeticException ->
    } shouldBe false
}

/**
 * Catch and handle exception, use false on failure.
 */
fun catchToFalseFromTrueExample() {
    {
        val success = 1 / 1
    } catchToFalse { _: ArithmeticException ->
    } shouldBe true

    {
        val fail = 1 / 0
    } catchToFalse { _: ArithmeticException ->
    } shouldBe false
}

/**
 * Catch and handle exception, use false on failure.
 */
fun catchToChoiceExample() {
    {
        val success = 1 / 1
        "Success"
    } catchToChoice { _: ArithmeticException ->
    } shouldSatisfy {
        it.isPrimary && it.primary == "Success"
    }

    {
        val fail = 1 / 0
        "Fail"
    } catchToChoice { _: ArithmeticException ->
    } shouldSatisfy {
        !it.isPrimary && it.secondary is ArithmeticException
    }
}

fun catchToExceptionExample() {
    {
        val success = 1 / 1
        "Success"
    } catchToException { _: ArithmeticException ->
    } shouldBe null

    {
        val fail = 1 / 0
        "Fail"
    } catchToException { _: ArithmeticException ->
    } shouldSatisfy {
        it is ArithmeticException
    }
}

fun main(args: Array<String>) {
    // Reroute for testing
    proxyException.clear()
    proxyException += {
        println("Exception was caught: ${it.javaClass.simpleName}")
    }

    // Run all examples
    catchAlternativeExample()
    catchToNullExample()
    catchToFalseFromReturnExample()
    catchToFalseFromTrueExample()
    catchToChoiceExample()
    catchToExceptionExample()
}