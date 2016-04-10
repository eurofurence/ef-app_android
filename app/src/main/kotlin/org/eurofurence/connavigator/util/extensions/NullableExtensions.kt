package org.eurofurence.connavigator.util.extensions

/**
 * Returns true if other is not null and this is either null or actually less than
 */
infix fun <T : Comparable<T>> T?.lt(other: T?) =
        other != null && (this == null || this < other)


infix fun <T : Comparable<T>> T?.max(other: T?): T? =
        if (this == null)
            other
        else if (other == null)
            this
        else if (this <= other)
            other
        else
            this