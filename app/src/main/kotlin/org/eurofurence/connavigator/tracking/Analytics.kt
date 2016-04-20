package org.eurofurence.connavigator.tracking

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker

/**
 * Created by David on 20-4-2016.
 */
class Analytics {
    companion object {
        lateinit var tracker: Tracker

        fun init(context: Context) {
            tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-1")
            tracker.setSampleRate(100.0)
        }

        fun changeScreenName(screenName: String){
            tracker.setScreenName(screenName)
            tracker.send(HitBuilders.ScreenViewBuilder().build())
        }

        fun trackEvent(eventBuilder: HitBuilders.EventBuilder){
            tracker.send(eventBuilder.build())
        }
    }
}