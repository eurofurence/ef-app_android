package org.eurofurence.connavigator.util

/**
 * Function on objects that may occur in singleton or in multiple occurrence.
 */
interface Bridge<T> {
    companion object {

        /**
         * Creates a patch function from the method to handle a single object. Multiple items will be fed consecutively.
         */
        fun <T> fromSingle(function: (T) -> Unit) = object : Bridge<T> {
            override fun invoke(item: T) {
                function(item)
            }

            override fun invoke(items: List<T>) {
                for (item in items)
                    function(item)
            }
        }

        /**
         * Creates a patch function from the method to handle multiple objects. Single items will be fed in a singleton list.
         */
        fun <T> fromSome(function: (List<T>) -> Unit) = object : Bridge<T> {
            override fun invoke(item: T) {
                function(listOf(item))
            }

            override fun invoke(items: List<T>) {
                function(items)
            }
        }
    }

    operator fun invoke(item: T)

    operator fun invoke(items: List<T>)
}
