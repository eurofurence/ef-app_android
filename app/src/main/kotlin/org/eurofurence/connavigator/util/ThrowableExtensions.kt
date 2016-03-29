package org.eurofurence.connavigator.util

import java.io.IOException

/**
 * Performs an operation in a block that might throw
 * an [IOException]. If such an exception is caught, return null.
 * @param block The block to run
 * @return Returns the result of the block or null
 */
fun <T> forSuccess(block: () -> T): T? = try {
    // Execute a block that might run into an IO exception
    block()
} catch(exception: IOException) {
    // Return null
    null
}

/**
 * Runs a block for success, returns true in that case. False is returned on an exception
 * @param block The block to run
 */
fun ifSuccess(block: () -> Unit): Boolean = try {
    block()
    true
} catch(throwable: Throwable) {
    false
}