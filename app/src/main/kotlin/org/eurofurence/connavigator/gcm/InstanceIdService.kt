package org.eurofurence.connavigator.gcm

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import io.swagger.client.model.PostFcmDeviceRegistrationRequest
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.webapi.apiService
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

        if (token == null) {
            warn { "No token to report" }
            return
        }

        info { "Submitting private message tokens" }
        info { "Token is $token" }

        setHeaders()

        task {
            info { "Making network request" }
            apiService.pushNotifications.apiV2PushNotificationsFcmDeviceRegistrationPost(
                    PostFcmDeviceRegistrationRequest().apply {
                        deviceId = token

                        topics = listOf(
                                "Version-${BuildConfig.VERSION_NAME}",
                                if (BuildConfig.DEBUG) "Debug" else ""
                        )
                    }
            )
        } success {
            info { "Token was successfully registered" }
            AuthPreferences.lastReportedFirebaseToken = token
        } fail {
            warn { "Token registration failed!" }
            warn { it.message }
            Analytics.exception(it)
        }
    }

    private fun setHeaders() {
        if (AuthPreferences.isLoggedIn()) {
            info { "User is logged in, associating token with user" }
            apiService.pushNotifications.addHeader("Authorization", AuthPreferences.asBearer())
        } else {
            warn { "User is not logged in, will not send token" }
        }
    }

    fun removeToken() {
        if (AuthPreferences.lastReportedFirebaseToken.isNullOrEmpty()) {
            info { "No known token to remove" }
            return
        }

        info { "Removing old token" }
        info { "Last known token is ${AuthPreferences.lastReportedFirebaseToken}"}

        setHeaders()
    }

    override fun onTokenRefresh() {
        info { "Token refreshed" }
        removeToken()
        reportToken()
    }
}