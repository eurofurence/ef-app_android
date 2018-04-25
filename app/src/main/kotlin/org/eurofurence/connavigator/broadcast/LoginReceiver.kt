package org.eurofurence.connavigator.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.chibatching.kotpref.bulk
import com.github.kittinunf.fuel.Fuel
import io.swagger.client.model.RegSysAuthenticationRequest
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.gcm.InstanceIdService
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.toIntent
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * Receives a login attempt and tries to follow through with it
 */
class LoginReceiver : BroadcastReceiver(), AnkoLogger {
    companion object {
        val USERNAME = "USERNAME"
        val REGNUMBER = "REGNUMBER"
        val PASSWORD = "PASSWORD"

        val LOGIN_RESULT = "LOGIN_RESULT"
    }

    override fun onReceive(context: Context, intent: Intent) {
        info { "Received login attempt in broadcaster" }

        val regNumber = intent.getStringExtra(REGNUMBER)
        val username = intent.getStringExtra(USERNAME)
        val password = intent.getStringExtra(PASSWORD)

        info { "Attempting login for ${username} with reg number ${regNumber}, $username, $password" }

        task {
            apiService.tokens.apiV2TokensRegSysPost(RegSysAuthenticationRequest().apply {
                this.regNo = regNumber.toInt()
                this.username = username
                this.password = password
            })
        } success {
            info { "Succesfully logged in" }
            info {
                "UID: ${it.uid}\n" +
                        "Username: ${it.username}\n" +
                        "Token: ${it.token}"
            }

            AuthPreferences.bulk {
                token = it.token
                tokenValidUntil = it.tokenValidUntil.time
                uid = it.uid
                this@bulk.username = it.username
            }

            info { "Saved auth to preferences" }

            val intent = LOGIN_RESULT.toIntent {
                booleans["success"] = true
            }

            LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent)
            InstanceIdService().reportToken()
            DataChanged.fire(context, "Login successful")
        } fail {
            warn { "Failed to retrieve tokens" }
            error { it.printStackTrace() }

            val intent = LOGIN_RESULT.toIntent {
                booleans["success"] = false
            }

            LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent)
            DataChanged.fire(context, "Login failed")
        }
    }
}