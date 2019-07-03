package org.eurofurence.connavigator.util

import io.reactivex.subjects.Subject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class ObservedProperty<R, T>(val on: ReadWriteProperty<R, T>, private val notify: (T, T) -> Unit) : ReadWriteProperty<R, T> {
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return on.getValue(thisRef, property)
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        val previous = on.getValue(thisRef, property)
        if (previous != value) {
            on.setValue(thisRef, property, value)
            notify(previous, value)
        }
    }
}

fun <R, T> ReadWriteProperty<R, T>.notify(block: (T, T) -> Unit) =
        ObservedProperty(this, block)

fun <R, T> ReadWriteProperty<R, T>.notify(asyncSubject: Subject<T>) =
        ObservedProperty(this) { _, to -> asyncSubject.onNext(to) }