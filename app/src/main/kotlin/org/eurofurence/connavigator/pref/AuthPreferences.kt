package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTime
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObservedProperty<R,T>(val on:ReadWriteProperty<R,T>, val notify:BehaviorSubject<T>) : ReadWriteProperty<R,T>{
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

fun <R,T> ReadWriteProperty<R,T>.notify(asyncSubject: BehaviorSubject<T>) =
        ObservedProperty(this, asyncSubject)

object AuthPreferences : KotprefModel() {
    var observer = BehaviorSubject.create<String>().apply { onNext("init") }

    var token by stringPref("").notify(observer)

    var tokenValidUntil by longPref(0)
    var uid by stringPref("")
    var username by stringPref("").notify(observer)

    var lastReportedFirebaseToken by stringPref("").notify(observer)

    fun isLoggedIn() = token.isNotEmpty()
    fun asBearer() = "Bearer $token"
    fun isValid() = DateTime.now().isBefore(tokenValidUntil)

    fun logout() = AuthPreferences.clear()
}