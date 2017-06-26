package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel

object AuthPreferences: KotprefModel() {
    var token by stringPref("")

    fun isLoggedIn() = token.isNotEmpty()
}