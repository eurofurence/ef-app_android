package org.eurofurence.connavigator.util.extensions

/**
 * Created by david on 9/12/16.
 */

/**
 * Bounds safe version of substring
 */
fun String.limit(size: Int): String {
    if (this.length > size) {
        return this.substring(size)
    } else {
        return this
    }
}