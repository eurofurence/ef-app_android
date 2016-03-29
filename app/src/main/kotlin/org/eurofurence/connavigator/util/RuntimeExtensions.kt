package org.eurofurence.connavigator.util

/**
 * Returns a fraction of the available memory in bytes.
 * @param div The divisor
 */
fun memoryFraction(div: Int) = (Runtime.getRuntime().maxMemory() / div).toInt()

/**
 * A small amount of memory.
 */
val memorySmall = memoryFraction(8192)

/**
 * Some memory of the available memory.
 */
val memorySome = memoryFraction(2048)