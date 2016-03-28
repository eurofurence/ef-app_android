package org.eurofurence.connavigator.extensions

import java.io.IOException

/**
 * Performs an operation in a block that might throw
 * an [IOException]. If such an exception is caught, return null.
 * @param block The block to run
 * @return Returns the result of the block or null
 */
fun <T> sec(block: () -> T): T? = try {
    // Execute a block that might run into an IO exception
    block()
} catch(exception: IOException) {
    // Return null
    null
}
