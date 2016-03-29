package org.eurofurence.connavigator.extensions

/**
 * Converts the number to the 1024-fold
 */
val Int.kb: Int get() = this * 1024

/**
 * Converts the number to the 1024²-fold
 */
val Int.mb: Int get() = this.kb.kb

/**
 * Converts the number to the 1024³-fold
 */
val Int.gb: Int get() = this.kb.kb.kb