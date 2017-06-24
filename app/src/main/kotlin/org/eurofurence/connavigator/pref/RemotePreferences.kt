package org.eurofurence.connavigator.pref

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.tracking.Analytics
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn

/**
 * Created by David on 30-7-2016.
 */

object RemotePreferences : AnkoLogger {
    private val remoteConfig by lazy { FirebaseRemoteConfig.getInstance() }

    private val cacheExpiration = if (BuildConfig.DEBUG) 0L else 3600L

    fun update() {
        info { "Updating remote configs" }
        remoteConfig.fetch(cacheExpiration).addOnFailureListener {
            warn { "Failed to update remote configs" }
            error { it.localizedMessage }
            Analytics.exception(it)
        }.addOnSuccessListener {
            info { "Successfully updated remote configs" }
            remoteConfig.activateFetched()
        }.addOnCompleteListener {
            info { "Update complete. Last activated config is now ${remoteConfig.info.fetchTimeMillis}" }
            info {  }
        }
    }

    fun init() {
        info { "Initializing remote configs" }
        val config = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()

        info { "Develop mode enabled:  ${BuildConfig.DEBUG}" }

        remoteConfig.setConfigSettings(config)
        remoteConfig.setDefaults(R.xml.remote)

        update()
    }

    // Booleans
    val mapsEnabled: Boolean = remoteConfig.getBoolean("maps_enabled")
    val rotationEnabled: Boolean = remoteConfig.getBoolean("rotation_enabled")
    val showEventGlyphs: Boolean = remoteConfig.getBoolean("show_event_glyphs")
    val showConflictingEvents: Boolean = remoteConfig.getBoolean("show_conflicting_events")

    // Longs
    val dealerMapWidth = remoteConfig.getLong("dealer_map_width")
    val dealerMapHeight = remoteConfig.getLong("dealer_map_height")
    val nextConStart = remoteConfig.getLong("nextConStart")
    val lastConEnd = remoteConfig.getLong("lastConEnd")
}