package org.eurofurence.connavigator.tracking

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 20-4-2016.
 */
class Analytics {
    object Category {
        val EVENT = "event"
        val DEALER = "dealer"
        val INFO = "info"
    }

    object Action {
        val SHARED = "shared"
        val OPENED = "opened"
        val FAVOURITE_ADD = "favourite added"
        val FAVOURITE_DEL = "favourite removed"
        val EXPORT_CALENDAR = "Exported to calendar"
        val LINK_CLICKED = "Clicked external link"
    }

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
            val analytics_on = preferences.getBoolean(context.resources.getString(R.string.settings_tag_analytics_enabled), true)

            GoogleAnalytics.getInstance(context).appOptOut = analytics_on


            // Start tracking
            if (BuildConfig.DEBUG) {
                tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-2")
            } else {
                tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-1")
            }

            var interval = 100.toDouble()

            tracker.setSampleRate(interval.toDouble())
        }

        fun changeScreenName(screenName: String) {
            tracker.setScreenName(screenName)
            tracker.send(HitBuilders.ScreenViewBuilder().build())
        }

        fun trackEvent(eventBuilder: HitBuilders.EventBuilder) {
            tracker.send(eventBuilder.build())
        }

        fun trackEvent(category: String, action: String, label: String) =
                trackEvent(HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label))
    }
}