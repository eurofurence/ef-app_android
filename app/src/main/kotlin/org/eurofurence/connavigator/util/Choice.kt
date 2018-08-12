@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package org.eurofurence.connavigator.util

/**
 * Union type.
 * @param T The type of the first option
 * @param U The type of the second option
 * @param isLeft True if the type is the first option
 * @param it The value stored without explicit type information
 */
data class Choice<out T, out U>(val isLeft: Boolean, val it: Any?) {
    /**
     * True if not left.
     */
    val isRight get() = !isLeft

    /**
     * The value if choice is left. Calling when [isRight] results in an [IllegalArgumentException].
     */
    @Suppress("UNCHECKED_CAST")
    val left
        get() = if (isLeft) it as T else throw IllegalArgumentException("Choice is not left")

    /**
     * The value if choice is right. Calling when [isLeft] results in an [IllegalArgumentException].
     */
    @Suppress("UNCHECKED_CAST")
    val right
        get() = if (!isLeft) it as U else throw IllegalArgumentException("Choice is not right")

    override fun toString() =
            this onLeft {
                "($it, -)"
            } onRight {
                "(-, $it)"
            }
}

/**
 * First half of handling both options in the choice. When [onRight] is called, the appropriate mapping is invoked
 * for the choice.
 * @param X The input type of the mapping function, can be a supertype of the left option
 * @param T The type of the left option
 * @param U The type of the right option
 * @param R The return type of the operation, should be the more generic type
 * @param choice The original choice to handle
 * @param onLeft The transformation for the left option
 */
class OnLeft<X, out T : X, out U, R>(val choice: Choice<T, U>, val onLeft: (X) -> R) {
    /**
     * Specifies the right mapping and applies the transformation.
     * @param onRight The transformation for the right option
     * @return Returns the result of [onLeft] if [choice] is left, otherwise the result of [onRight].
     */
    infix fun onRight(onRight: (U) -> R) =
            if (choice.isLeft)
                onLeft(choice.left)
            else
                onRight(choice.right)
}

/**
 * First half of handling both options in the choice. When [onLeft] is called, the appropriate mapping is invoked
 * for the choice.
 * @param X The input type of the mapping function, can be a supertype of the right option
 * @param T The type of the left option
 * @param U The type of the right option
 * @param R The return type of the operation, should be the more generic type
 * @param choice The original choice to handle
 * @param onRight The transformation for the right option
 */
class OnRight<X, out T, out U : X, R>(val choice: Choice<T, U>, val onRight: (X) -> R) {
    /**
     * Specifies the left mapping and applies the transformation.
     * @param onLeft The transformation for the left option
     * @return Returns the result of [onLeft] if [choice] is left, otherwise the result of [onRight].
     */
    private infix fun onLeft(onLeft: (T) -> R) =
            if (choice.isLeft)
                onLeft(choice.left)
            else
                onRight(choice.right)
}

/**
 * Starts a choice transformation with the mapping function for the left option, see [OnLeft].
 */
infix fun <X, T : X, U, R> Choice<T, U>.onLeft(onLeft: (X) -> R) =
        OnLeft(this, onLeft)

/**
 * Starts a choice transformation with the mapping function for the right option, see [OnLeft].
 */
infix fun <X, T, U : X, R> Choice<T, U>.onRight(onRight: (X) -> R) =
        OnRight(this, onRight)


/**
 * Maps the left value to either a value of a new left type or the old right type.
 */
infix fun <T, U, V> Choice<T, U>.outerMapLeft(block: (T) -> Choice<V, U>) =
        if (isLeft) block(left) else Choice(false, right)

/**
 * Maps the right value to either a value of a new right type or the old right type.
 */
infix fun <T, U, V> Choice<T, U>.outerMapRight(block: (U) -> Choice<T, V>) =
        if (isRight) block(right) else Choice(true, left)

/**
 * Maps the left value to a value of a new left type.
 */
infix fun <T, U, V> Choice<T, U>.mapLeft(block: (T) -> V) =
        Choice<V, U>(isLeft, if (isLeft) block(left) else it)

/**
 * Maps the right value to a value of a new right type.
 */
infix fun <T, U, V> Choice<T, U>.mapRight(block: (U) -> V) =
        Choice<T, V>(isLeft, if (isRight) block(right) else it)

/**
 * For non nullable [T] and [U], deconstructs a choice into a pair of nullable [T] and [U].
 * @param T The left type
 * @param U The right type
 * @receiver The choice to deconstruct
 * @return Returns a pair of nullable left and nullable right
 */
val <T : Any, U : Any> Choice<T, U>.deconstructed
    get() = Pair(
            if (isLeft) left else null,
            if (isRight) right else null)

/**
 * Creates a choice that is the left option.
 * @param T The left type
 * @param U The right type
 * @param it The value
 * @return Returns a new choice
 */
fun <T, U> left(it: T) = Choice<T, U>(true, it)

/**
 * Creates a choice that is the right option.
 * @param T The left type
 * @param U The right type
 * @param it The value
 * @return Returns a new choice
 */
fun <T, U> right(it: U) = Choice<T, U>(false, it)

/**
 * For a non-nullable [T] and input of type [T]?, returns a choice of left [T] if not null, and right [Unit] if null.
 * @param T The type of the value, cannot be nullable
 * @receiver The value to convert
 * @return Returns a new choice
 */
val <T : Any> T?.orUnit get() = if (this != null) left<T, Unit>(this) else right(Unit)

/**
 * Tries to apply the [block] returning it as [left], if it fails in an exception of type [U], returning it as [right].
 * @param T The return type of the block
 * @param U The type of the exception
 * @param block The block to execute
 * @return Returns a choice of either the result or the exception of the block
 */
inline fun <T, reified U : Throwable> tryFor(block: () -> T): Choice<T, U> = try {
    left(block())
} catch (t: Throwable) {
    if (t is U)
        right(t)
    else
        throw t
}

/**
 * Instance of [tryFor], where all exceptions are caught.
 * @param T The return type of the block
 * @param block The block to execute
 * @return Returns a choice of either the result or the exception of the block
 */
inline fun <T> tryForThrowable(block: () -> T): Choice<T, Throwable> = tryFor(block)
