package org.eurofurence.connavigator.services

import android.content.Context
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState
import org.eurofurence.connavigator.workers.DataUpdateWorker

/**
 * Verifies database integrity and clears DB if required.
 */
class DatabaseCheckService(val context: Context) {
    val db get () = RootDb(context)

    companion object {
        fun isSameMinorVersion(previous: String, current: String): Boolean {
            val semanticVersion = Regex("\\d+.(\\d+).\\d+")

            val currentMinorVersion = semanticVersion.find(current)?.groupValues?.get(1)
            val previousMinorVersion = semanticVersion.find(previous)?.groupValues?.get(1)

            return currentMinorVersion == previousMinorVersion
        }
    }

    fun run(): Boolean {
        val versionIsSame = isSameMinorVersion(BackgroundPreferences.lastKnownVersion, BuildConfig.VERSION_NAME)

        // If we're not on the same minor version, erase data before fetching
        if (!versionIsSame) {
            BackgroundPreferences.loadingState = LoadingState.UNINITIALIZED
            BackgroundPreferences.hasLoadedOnce = false
            db.clear()
        }

        BackgroundPreferences.lastKnownVersion = BuildConfig.VERSION_NAME

        DataUpdateWorker.execute(context)

        return versionIsSame
    }
}