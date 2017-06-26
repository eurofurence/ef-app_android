package org.eurofurence.connavigator.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.chibatching.kotpref.bulk
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.gson.Gson
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.toIntent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import org.joda.time.DateTime

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

        info { "Attempting login for ${username} with reg number ${regNumber}" }

        val payload = Gson().toJson(mapOf(
                "RegNo" to regNumber,
                "Username" to username,
                "Password" to password
        ))

        Fuel.post("https://app.eurofurence.org/Api/v2/Tokens/RegSys")
                .body(payload)
                .header(mapOf(
                        "Content-Length" to payload.length,
                        "Content-Type" to "application/json"
                ))
                .responseJson { request, response, result ->
                    result.fold(
                            { incoming ->
                                val data = incoming.obj()

                                info { "Succesfully logged in" }
                                info {
                                    "UID: ${data.getString("Uid")}\n" +
                                            "Username: ${data.getString("Username")}\n" +
                                            "Token: ${data.getString("Token")}"
                                }

                                AuthPreferences.bulk {
                                    token = data.getString("Token")
                                    tokenValidUntil = DateTime.parse(data.getString("TokenValidUntil")).millis
                                    uid = data.getString("Uid")
                                    this@bulk.username = data.getString("Username")
                                }

                                info { "Saved auth to preferences" }

                                val intent = LOGIN_RESULT.toIntent {
                                    booleans["success"] = true
                                }

                                LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent)
                            },
                            { err ->
                                warn { "Failed to retrieve tokens" }
                                debug { err.errorData }
                                Analytics.exception(err.exception)

                                val intent = LOGIN_RESULT.toIntent {
                                    booleans["success"] = false
                                }

                                LocalBroadcastManager.getInstance(context).sendBroadcastSync(intent)
                            }
                    )
                }
    }
}