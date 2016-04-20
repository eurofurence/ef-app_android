package org.eurofurence.connavigator.tracking

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker

/**
 * Created by David on 20-4-2016.
 */
class Analytics {
    companion object {
        lateinit var tracker: Tracker

        fun Init(context: Context) {
            tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-1")
        }
    }
}