package org.eurofurence.connavigator.pref

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


        val cacheExpiration: Long = when (BuildConfig.DEBUG) {
            true -> 5L
            else -> 3600L
        }

        fun clear() {
            remoteConfig.setDefaults(R.xml.remote)
        }

        fun refresh(seconds: Int = 3600) {
            remoteConfig.fetch(seconds.toLong()).addOnCompleteListener {
                if (it.isSuccessful) {
                    remoteConfig.activateFetched()
                } else {
                    logd { "Failed to refresh cache" }
                }
            }
        }
    }

    fun intitialize(context: Context) {
        remoteConfig.setConfigSettings(FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build())

        remoteConfig.setDefaults(R.xml.remote)

        logConfigStatus()
    }

    private fun logConfigStatus() {
        val status = remoteConfig.getString("config_working_test")
        logd { "Config status: $status" }
        logd { "Config last fetch: ${DateTime(remoteConfig.info.fetchTimeMillis).toString()}}" }
    }

    // Booleans
    val mapsEnabled: Boolean = remoteConfig.getBoolean("maps_enabled")
    val rotationEnabled: Boolean = remoteConfig.getBoolean("rotation_enabled")
    val showEventGlyphs: Boolean = remoteConfig.getBoolean("show_event_glyphs")
    val showConflictingEvents: Boolean = remoteConfig.getBoolean("show_conflicting_events")

    // Longs
    val dealerMapWidth = remoteConfig.getLong("dealer_map_width")
    val dealerMapHeight = remoteConfig.getLong("dealer_map_height")
}