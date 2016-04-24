package org.eurofurence.connavigator.tracking

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 20-4-2016.
 */
class Analytics {
    companion object : SharedPreferences.OnSharedPreferenceChangeListener {
        lateinit var tracker: Tracker
        lateinit var context: Context

        fun init(context: Context) {
            logv { "Initializing Google Analytics Tracking" }

            // Get shared preferences
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)

            //connect callback
            preferences.registerOnSharedPreferenceChangeListener(this)

            this.context = context

            updateTracking(context, preferences)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key?.contains("analytics")!!) {
                updateTracking(context, sharedPreferences!!);
            }
        }

        private fun updateTracking(context: Context, preferences: SharedPreferences) {
            logv { "Updating tracking to new stats" }
            // Set app-level opt out
            GoogleAnalytics.getInstance(context).appOptOut = preferences.getBoolean(R.string.settings_tag_analytics_enabled.toString(), true)


            // Start tracking
            tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-1")

            var interval = preferences.getString(R.string.settings_tag_analytics_interval.toString(), "50")

            try {
                interval.toDouble()
            } catch (exception: Exception) {
                interval = "50"
            }

            tracker.setSampleRate(interval.toDouble())
        }

        fun changeScreenName(screenName: String) {
            tracker.setScreenName(screenName)
            tracker.send(HitBuilders.ScreenViewBuilder().build())
        }

        fun trackEvent(eventBuilder: HitBuilders.EventBuilder) {
            tracker.send(eventBuilder.build())
        }
    }
}