package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObservedProperty<R,T>(val on:ReadWriteProperty<R,T>, val notify:PublishSubject<T>) : ReadWriteProperty<R,T>{
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

fun <R,T> ReadWriteProperty<R,T>.notify(asyncSubject: PublishSubject<T>) =
        ObservedProperty(this, asyncSubject)

object AuthPreferences : KotprefModel() {
    var observer = PublishSubject.create<String>()

    var token by stringPref("").notify(observer)

    var tokenValidUntil by longPref(0)
    var uid by stringPref("")
    var username by stringPref("")

    var lastReportedFirebaseToken by stringPref("")

    fun isLoggedIn() = token.isNotEmpty()
    fun asBearer() = "Bearer $token"
    fun isValid() = DateTime.now().isBefore(tokenValidUntil)

    fun logout() = AuthPreferences.clear()
}