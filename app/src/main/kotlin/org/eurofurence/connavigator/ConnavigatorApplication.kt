package org.eurofurence.connavigator

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by Pazuzu on 03.03.2016.
 */
class ConnavigatorApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // This configures the timezone database for JODA time, that way timezone info can be used without having the
        // impractical database of JODA
        JodaTimeAndroid.init(this)
    }
}