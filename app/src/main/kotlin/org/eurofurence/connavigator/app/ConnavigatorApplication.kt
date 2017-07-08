package org.eurofurence.connavigator.app

import android.content.Intent
import android.support.multidex.MultiDexApplication
import com.chibatching.kotpref.Kotpref
import com.google.firebase.perf.metrics.AddTrace
import io.swagger.client.model.DealerRecord
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import com.joanzapata.iconify.fonts.FontAwesomeModule
import net.danlew.android.joda.JodaTimeAndroid
import nl.komponents.kovenant.android.startKovenant
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.gcm.InstanceIdService
import org.eurofurence.connavigator.gcm.PushListenerService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.v2.BundleInput
import org.eurofurence.connavigator.util.v2.BundleOutput
import org.eurofurence.connavigator.util.v2.IntentInput
import org.eurofurence.connavigator.util.v2.IntentOutput
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.util.*
import kotlin.serialization.*

@Serializable
data class Address(val street: String, val town: String)

@Serializable
data class User(val name: String, val id: Int, val address: Address, val dealerRecord: DealerRecord)


/**
 * The application initialization point.
 */
class ConnavigatorApplication : MultiDexApplication(), AnkoLogger {
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

        // Initialize some services
        imageService.initialize(this)
        logService.initialize(this)

        RemotePreferences.init()

        apiService.initialize(this)
        Analytics.init(this)

        // Promises
        startKovenant()

        // Listen to cloud updates
        PushListenerService().subscribe()

        // Icons
        Iconify.with(FontAwesomeModule())

        // Update every 5 minutes
        UpdateIntentService.dispatchUpdate(this)
    }
}