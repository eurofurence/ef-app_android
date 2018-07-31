package org.eurofurence.connavigator.pref

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.ReplaySubject
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import org.joda.time.DateTime

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
            warn { it.message ?: "No message given" }
        }.addOnSuccessListener {
            info { "Successfully updated remote configs" }
            remoteConfig.activateFetched()
            observer.onNext(this)
        }.addOnCompleteListener {
            info { "Update complete. Last activated config is now ${remoteConfig.info.fetchTimeMillis}" }
            info { }
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
        observer.onNext(this)

        update()
    }

    val observer = BehaviorSubject.create<RemotePreferences>()

    val lastUpdatedMillis get() = remoteConfig.info.fetchTimeMillis
    val lastUpdatedDatetime get() = DateTime(lastUpdatedMillis)
    val timeSinceLastUpdate get() = DateTime.now().minus(lastUpdatedMillis)

    // Booleans
    val mapsEnabled: Boolean get() = remoteConfig.getBoolean("maps_enabled")
    val rotationEnabled: Boolean get() = remoteConfig.getBoolean("rotation_enabled")
    val nativeFursuitGames get() = remoteConfig.getBoolean("native_fursuit_games")
    val autoUpdateDisabled get() = remoteConfig.getBoolean("auto_update_disabled")

    // Longs
    val nextConStart get() = remoteConfig.getLong("nextConStart")
    val lastConEnd get() = remoteConfig.getLong("lastConEnd")

    // Strings
    val apiBaseUrl get() =   remoteConfig.getString("api_base_url")
    val supportChatUrl get() = remoteConfig.getString("support_chat_url")
}