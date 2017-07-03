package org.eurofurence.connavigator.gcm

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import org.eurofurence.connavigator.pref.AuthPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * Created by requinard on 7/3/17.
 */
class InstanceIdService : FirebaseInstanceIdService(), AnkoLogger {
    /**
     * Checks if a user is logged in, if it is, we send a token to the appserver
     */
    fun reportToken() {
        val token = FirebaseInstanceId.getInstance().token

        info { "Submitting private message tokens" }
        info { "Token is ${token}" }

        if (AuthPreferences.isLoggedIn()) {
            info { "User is logged in, associating token with user" }

        } else {
            warn { "User is not logged in, will not send token" }
        }
    }

    override fun onTokenRefresh() {
        info { "Token refreshed" }
        reportToken()
    }
}