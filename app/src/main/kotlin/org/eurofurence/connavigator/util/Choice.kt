package org.eurofurence.connavigator.util

/**
 * A choice between two items. See [isPrimary], [primary] and [secondary].
 * @param isPrimary True if the choice is of the primary type.
 */
class Choice<T, U> private constructor(val isPrimary: Boolean, private val item: Any?) {
    companion object {
        /**
         * Creates the choice of the primary type.
         * @param t The value
         */
        fun <T, U> pri(t: T) = Choice<T, U>(true, t)

        /**
         * Creates the choice of the secondary type.
         * @param t The value
         */
        fun <T, U> snd(u: U) = Choice<T, U>(false, u)
    }

    /**
     * Semi configured handler
     */
    inner class PrimaryHandled<R>(val primaryBlock: (T) -> R) {
        /**
         * Finishes configuration and executes the handler
         */
        infix fun onSecondary(secondaryBlock: (U) -> R) =
                if (isPrimary)
                    primaryBlock(primary)
                else
                    secondaryBlock(secondary)
    }

    /**
     * Semi configured handler
     */
    inner class SecondaryHandled<R>(val secondaryBlock: (U) -> R) {
        /**
         * Finishes configuration and executes the handler
         */
        infix fun onPrimary(primaryBlock: (T) -> R) =
                if (isPrimary)
                    primaryBlock(primary)
                else
                    secondaryBlock(secondary)
    }

    @Suppress("UNCHECKED_CAST")
    val primary: T get() = item as T

    @Suppress("UNCHECKED_CAST")
    val secondary: U get() = item as U

    /**
     * Starts a semi configured handler
     */
    infix fun <R> onPrimary(block: (T) -> R) = PrimaryHandled(block)

    /**
     * Starts a semi configured handler
     */
    infix fun <R> onSecondary(block: (U) -> R) = SecondaryHandled(block)

    /**
     * Applies the appropriate function for the choice's type.
     */
    fun apply(blockPrimary: T.() -> Unit, blockSecondary: U.() -> Unit) =
            if (isPrimary)
                blockPrimary(primary)
            else
                blockSecondary(secondary)

    /**
     * Passes the appropriate parameter for the choice's type.
     */
    fun <V> let(blockPrimary: (T) -> V, blockSecondary: (U) -> V) =
            if (isPrimary)
                primary.let(blockPrimary)
            else
                secondary.let(blockSecondary)

    override fun hashCode() =
            sequenceOf(isPrimary, item).hashCode()

    override fun equals(other: Any?) =
            other is Choice<*, *> && sequenceOf(isPrimary, item) == sequenceOf(other.isPrimary, other.item)

    override fun toString() =
            if (isPrimary)
                "($primary,-)"
            else
                "(-,$secondary)"
}