package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.bulk
import org.joda.time.DateTime

object AuthPreferences: KotprefModel() {
    var token by stringPref("")
    var tokenValidUntil by longPref(0)
    var uid by stringPref("")
    var username by stringPref("")

    fun isLoggedIn() = token.isNotEmpty()
    fun asBearer() = "Bearer $token"
    fun isValid() = DateTime.now().isBefore(tokenValidUntil)

    fun logout() = AuthPreferences.clear()
}