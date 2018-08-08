package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import io.reactivex.subjects.BehaviorSubject
import org.eurofurence.connavigator.util.notify
import org.joda.time.DateTime

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

    /**
     * Checks if a token is valid. if not, remove the token
     */
    fun validate() {
        if (token.isNotEmpty() && !isValid()) {
            token = ""
            uid = ""
            username = ""
        }
    }

    fun logout() = AuthPreferences.clear()
}