package org.eurofurence.connavigator.app

import android.support.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref
import com.google.firebase.perf.metrics.AddTrace
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import net.danlew.android.joda.JodaTimeAndroid
import nl.komponents.kovenant.android.startKovenant
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.gcm.InstanceIdService
import org.eurofurence.connavigator.gcm.PushListenerService
import org.eurofurence.connavigator.net.ImageService
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.webapi.ApiService

/**
 * The application initialization point.
 */
class ConnavigatorApplication : MultiDexApplication() {
    @AddTrace(name = "ConnavigatorApplication:onCreate", enabled = true)
    override fun onCreate() {
        super.onCreate()

        // Apply root theme for inheritance
        setTheme(R.style.AppTheme)

        // This configures the timezone database for JODA time, that way timezone info can be used without having the
        // impractical database of JODAgst
        JodaTimeAndroid.init(this)

        // Preferences
        Kotpref.init(this)
        RemotePreferences.init()

        // Initialize some services
        ImageService.initialize(this)
        LogService.initialize(this)
        ApiService.initialize(this)
        Analytics.init(this)

        // Promises
        startKovenant()

        // Listen to cloud updates
        PushListenerService().subscribe()

        // Report token
        InstanceIdService().reportToken()

        // Icons
        Iconify.with(FontAwesomeModule())

        // Check logged in tokens
        AuthPreferences.validate()
    }
}