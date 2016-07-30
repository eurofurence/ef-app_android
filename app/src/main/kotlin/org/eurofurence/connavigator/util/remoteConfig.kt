package org.eurofurence.connavigator.util

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logd
import org.joda.time.DateTime

/**
 * Created by David on 30-7-2016.
 */
class RemoteConfig {
    companion object {
        val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        fun intitialize(context: Context) {
            remoteConfig.setConfigSettings(FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build())

            remoteConfig.setDefaults(R.xml.remote)

            logConfigStatus()

            remoteConfig.fetch(cacheExpiration)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            logd { "Fetch completed" }
                            remoteConfig.activateFetched()
                            logConfigStatus()
                        } else {
                            logd { "failed to update" }
                        }
                    }
                    .addOnFailureListener { logd { "Fetch failed ${it.toString()}" } }


        }

        private fun logConfigStatus() {
            val status = remoteConfig.getString("config_working_test")
            logd { "Config status: $status" }
            logd { "Config last fetch: ${DateTime(remoteConfig.info.fetchTimeMillis).toString()}}" }
        }

        val cacheExpiration: Long = when (BuildConfig.DEBUG) {
            true -> 5L
            else -> 3600L
        }
    }

    val mapsEnabled: Boolean = remoteConfig.getBoolean("maps_enabled")
    val rotationEnabled: Boolean = remoteConfig.getBoolean("rotation_enabled")
}