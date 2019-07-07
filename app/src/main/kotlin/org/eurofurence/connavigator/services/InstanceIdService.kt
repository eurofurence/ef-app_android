package org.eurofurence.connavigator.services

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import io.swagger.client.model.PostFcmDeviceRegistrationRequest
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * Created by requinard on 7/3/17.
 */
class InstanceIdService : FirebaseInstanceIdService(), AnkoLogger {
    /**
     * Checks if a user is logged in, if it is, we send a token to the app server
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
            var sendTopics = listOf(
                    "android",
                    "version-${BuildConfig.VERSION_NAME}",
                    "cid-${BuildConfig.CONVENTION_IDENTIFIER}"
            )

            if (BuildConfig.DEBUG) sendTopics += "debug"

            info { "Making network request" }
            apiService.pushNotifications.apiPushNotificationsFcmDeviceRegistrationPost(
                    PostFcmDeviceRegistrationRequest().apply {
                        deviceId = token
                        topics = sendTopics
                    }
            )
        } success {
            info { "Token was successfully registered" }
            AuthPreferences.lastReportedFirebaseToken = token
        } fail {
            warn { "Token registration failed!" }
            warn { it.toString() }
        }
    }

    private fun setHeaders() {
        if (AuthPreferences.isLoggedIn) {
            info { "User is logged in, associating token with user" }
            apiService.pushNotifications.addHeader("Authorization", AuthPreferences.asBearer())
        } else {
            warn { "User is not logged in, will not send token" }
        }
    }

    override fun onTokenRefresh() {
        info { "Token refreshed" }
        reportToken()
    }
}