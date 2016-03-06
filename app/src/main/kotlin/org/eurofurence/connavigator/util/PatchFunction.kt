package org.eurofurence.connavigator.util

/**
 * Function on objects that may occur in singleton or in multiple occurrence.
 */
interface PatchFunction<T> {
    operator fun invoke(item: T)

    operator fun invoke(items: List<T>)
}

/**
 * Creates a patch function from the method to handle a single object. Multiple items will be fed consecutively.
 */
fun <T> patchFromSingle(function: (T) -> Unit) = object : PatchFunction<T> {
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
fun <T> patchFromSome(function: (List<T>) -> Unit) = object : PatchFunction<T> {
    override fun invoke(item: T) {
        function(listOf(item))
    }

    override fun invoke(items: List<T>) {
        function(items)
    }
}