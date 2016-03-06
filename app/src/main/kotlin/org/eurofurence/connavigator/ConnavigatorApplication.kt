package org.eurofurence.connavigator

import android.app.Application
import android.os.StrictMode
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by Pazuzu on 03.03.2016.
 */
class ConnavigatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enabling some not very pretty threading options that will only be used in the early phase
        // TODO: replace dummy threading with the appropriate patterns
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        // This configures the timezone database for JODA time, that way timezone info can be used without having the
        // impractical database of JODA
        JodaTimeAndroid.init(this)
    }
}