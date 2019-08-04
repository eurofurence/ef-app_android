@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package org.eurofurence.connavigator.preferences

import com.chibatching.kotpref.KotprefModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.eurofurence.connavigator.util.notify
import org.joda.time.DateTime

/**
 * An authentication model status.
 * @property token The authentication token.
 * @property tokenValidUntil The time until which the token is valid.
 * @property uid The user ID.
 * @property username The username.
 * @property lastReportedFirebaseToken The last reported firebase token.
 */
data class Authentication(
        val token: String,
        val tokenValidUntil: Long,
        val uid: String,
        val username: String,
        val lastReportedFirebaseToken: String) {
    val isLoggedIn get() = token.isNotEmpty()

    /**
     * Manually override hashcode as `Long` does not have a hashcode implementation on all platforms
     */
    override fun hashCode(): Int {
        return token.hashCode() // should be sorta unique
    }
}

object AuthPreferences : KotprefModel() {
    /**
     * The current token.
     */
    var token: String by stringPref("").notify { _, _ ->
        updatedSubject.onNext(authentication)
    }

    /**
     * The current token validity.
     */
    var tokenValidUntil by longPref(0).notify { _, _ ->
        updatedSubject.onNext(authentication)
    }

    /**
     * The current user ID.
     */
    var uid by stringPref("").notify { _, _ ->
        updatedSubject.onNext(authentication)
    }

    /**
     * The current user name.
     */
    var username: String by stringPref("").notify { _, _ ->
        updatedSubject.onNext(authentication)
    }

    /**
     * The last reported firebase token.
     */
    var lastReportedFirebaseToken: String by stringPref("").notify { _, _ ->
        updatedSubject.onNext(authentication)
    }

    /**
     * Gets the current authentication model.
     */
    val authentication: Authentication
        get() = Authentication(
                token,
                tokenValidUntil,
                uid,
                username,
                lastReportedFirebaseToken)

    /**
     * Subject collecting all updates.
     */
    private val updatedSubject: BehaviorSubject<Authentication> =
            BehaviorSubject.createDefault(authentication)

    /**
     * Authentication updates.
     */
    val updated: Observable<Authentication> = updatedSubject.distinct()

    /**
     * True if logged in, see [Authentication.isLoggedIn].
     */
    val isLoggedIn
        get() =
            authentication.isLoggedIn

    /**
     * Gets the token, or, iff not logged in, the string "empty".
     */
    fun tokenOrEmpty() =
            if (authentication.isLoggedIn) token else "empty"

    /**
     * Returns the token as a bearer authentication for use in HTTP requrests.
     */
    fun asBearer() = "Bearer $token"

    /*
        We'll consider the token to be invalid 24h before it expires. Since token lifetime is
        compared against local time, this catches some edge cases with small time drift
        or wrong timezones on the client device, and generally shouldn't be a problem
        since token lifetime is several weeks.
     */
    fun isValid() = DateTime.now().plusDays(1).isBefore(tokenValidUntil)

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