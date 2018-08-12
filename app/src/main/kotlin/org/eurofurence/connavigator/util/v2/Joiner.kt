@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package org.eurofurence.connavigator.util.v2

/**
 * Definition of a model join.
 */
class Joiner<in L, in R, out I>(
        val leftId: (L) -> I?,
        val rightId: (R) -> I?) {

    val inverse get() = Joiner(rightId, leftId)
}

interface Source<out T : Any, in I> {
    operator fun get(i: I?): T?
}

class JoinerBinding<L : Any, R : Any, I>(
        val joiner: Joiner<L, R, I>,
        val leftBinding: Source<L, I>,
        val rightBinding: Source<R, I>) {
    operator fun invoke(left: L) =
            rightBinding[joiner.leftId(left)]

    operator fun invoke(left: Iterable<L>) =
            left.mapNotNull { invoke(it) }

    fun join(left: L) =
            rightBinding[joiner.leftId(left)]?.let {
                left to it
            }
}

operator fun <L : Any, R : Any, I> L.get(joinerBinding: JoinerBinding<L, R, I>) =
        joinerBinding(this)

operator fun <L : Any, R : Any, I> Iterable<L>.get(joinerBinding: JoinerBinding<L, R, I>) =
        joinerBinding(this)

/**
 * Defines a model join by key extraction functions.
 */
infix fun <L, R, I> ((L) -> I?).join(right: (R) -> I?) = Joiner(this, right)

class IdSource<T : Any> : Source<T, T> {
    override fun get(i: T?) = i
}