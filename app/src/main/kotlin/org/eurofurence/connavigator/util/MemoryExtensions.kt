package org.eurofurence.connavigator.util

/**
 * A tiny amount of disk space.
 */
val diskTiny = 16;
/**
 * Some of the available disk space.
 */
val diskSome = 1024;

/**
 * Small amount of the disk space.
 */
val diskSmall = 1024 * 1024;

/**
 * Medium amount of the disk space.
 */
val diskMedium = 10 * 1024 * 1024


/**
 * Returns a fraction of the available memory in bytes.
 * @param div The divisor
 */
fun memoryFraction(div: Int) = (Runtime.getRuntime().maxMemory() / div).toInt()

/**
 * A tiny amount of memory.
 */
val memoryTiny = memoryFraction(8192)

/**
 * Some of the available memory.
 */
val memorySome = memoryFraction(2048)

/**
 * Small amount of the available memory.
 */
val memorySmall = memoryFraction(1024)

/**
 * Medium amount of the available memory.
 */
val memoryMedium = memoryFraction(512)