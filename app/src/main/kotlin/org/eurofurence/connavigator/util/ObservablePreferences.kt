package org.eurofurence.connavigator.util

import io.reactivex.subjects.Subject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObservedProperty<R,T>(val on: ReadWriteProperty<R, T>, private val notify: Subject<T>) : ReadWriteProperty<R, T> {
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        return on.getValue(thisRef, property)
    }

    override fun setValue(thisRef: R, property: KProperty<*>, value: T) {
        if(on.getValue(thisRef, property)!=value){
            on.setValue(thisRef, property, value)
            notify.onNext(value)
        }
    }
}

fun <R,T> ReadWriteProperty<R, T>.notify(asyncSubject: Subject<T>) =
        ObservedProperty(this, asyncSubject)