package org.eurofurence.connavigator.app

import androidx.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref
import com.google.firebase.perf.metrics.AddTrace
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import net.danlew.android.joda.JodaTimeAndroid
import nl.komponents.kovenant.android.startKovenant
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.notifications.NotificationFactory
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.services.*

/**
 * The application initialization point.
 */
@Suppress("unused")
class ConnavigatorApplication : MultiDexApplication() {
    @AddTrace(name = "ConnavigatorApplication:onCreate", enabled = true)
    override fun onCreate() {
        super.onCreate()

        // Apply root theme for inheritance
        setTheme(R.style.AppTheme)

        // This configures the timezone database for JODA time, that way timezone info can be used without having the
        // impractical database of JODA
        JodaTimeAndroid.init(this)

        // Preferences
        Kotpref.init(this)
        RemotePreferences.init()

        // Initialize some services
        ImageService.initialize(this)
        apiService.initialize(this)
        AnalyticsService.init(this)

        // Promises
        startKovenant()

        // Verify database integrity.
        DatabaseCheckService(applicationContext).run()

        // Set up notificatioChannels
        NotificationFactory(applicationContext).setupChannels()

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