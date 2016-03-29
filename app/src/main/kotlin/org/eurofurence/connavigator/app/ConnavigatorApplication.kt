package org.eurofurence.connavigator.app

import android.app.Application
import io.swagger.client.ApiInvoker
import net.danlew.android.joda.JodaTimeAndroid
import org.eurofurence.connavigator.net.volleyService
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

        // Initialize the services
        volleyService.initialize(this)
        apiService.initialize()

    }
}