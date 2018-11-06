@file:Suppress("MemberVisibilityCanBePrivate")

package org.eurofurence.connavigator.pref

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.reactivex.subjects.BehaviorSubject
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

    val observer: BehaviorSubject<RemotePreferences> = BehaviorSubject.create<RemotePreferences>()

    val lastUpdatedMillis get() = remoteConfig.info.fetchTimeMillis
    val lastUpdatedDatetime get() = DateTime(lastUpdatedMillis)
    val timeSinceLastUpdate: DateTime get() = DateTime.now().minus(lastUpdatedMillis)

    // Booleans
    val mapsEnabled: Boolean get() = remoteConfig.getBoolean("${BuildConfig.CONVENTION_IDENTIFIER}_maps_enabled")
    val rotationEnabled: Boolean get() = remoteConfig.getBoolean("${BuildConfig.CONVENTION_IDENTIFIER}_rotation_enabled")
    val nativeFursuitGames: Boolean get() = remoteConfig.getBoolean("${BuildConfig.CONVENTION_IDENTIFIER}_native_fursuit_games")
    val autoUpdateDisabled: Boolean get() = remoteConfig.getBoolean("${BuildConfig.CONVENTION_IDENTIFIER}_auto_update_disabled")

    // Longs
    val nextConStart: Long get() = remoteConfig.getLong("${BuildConfig.CONVENTION_IDENTIFIER}_nextConStart")
    val lastConEnd: Long get() = remoteConfig.getLong("${BuildConfig.CONVENTION_IDENTIFIER}_lastConEnd")

    // Strings
    val supportChatUrl: String get() = remoteConfig.getString("${BuildConfig.CONVENTION_IDENTIFIER}_support_chat_url")
    val eventTitle: String get() = remoteConfig.getString("${BuildConfig.CONVENTION_IDENTIFIER}_event_title")
    val eventSubTitle: String get() = remoteConfig.getString("${BuildConfig.CONVENTION_IDENTIFIER}_event_subtitle")
}