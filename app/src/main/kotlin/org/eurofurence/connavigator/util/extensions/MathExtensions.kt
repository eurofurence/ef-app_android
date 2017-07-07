package org.eurofurence.connavigator.util.extensions

/**
 * Created by requinard on 7/7/17.
 */
infix fun Int.power(b: Int): Double = Math.pow(this.toDouble(), b.toDouble())
infix fun Float.power(b: Float) : Double = Math.pow(this.toDouble(), b.toDouble())
