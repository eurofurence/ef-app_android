package org.eurofurence.connavigator.app

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.gcm.MyInstanceIDListenerService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.webapi.apiService

/**
 * The application initialization point.
 */
class ConnavigatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // This configures the timezone database for JODA time, that way timezone info can be used without having the
        // impractical database of JODA
        JodaTimeAndroid.init(this)

        // Initialize some services
        imageService.initialize(this)
        apiService.initialize(this)
        logService.initialize(this)

        Analytics.init(this)

        MyInstanceIDListenerService.dispatchUpdate(this)

        // Now running an update every time application boots
        UpdateIntentService.dispatchUpdate(this)
    }
}