package org.eurofurence.connavigator.tracking

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv

/**
 * Created by David on 20-4-2016.
 */
class Analytics {
    /**
     * Collects all categories for analytics
     */
    object Category {
        val EVENT = "event"
        val DEALER = "dealer"
        val INFO = "info"
        val ANNOUNCEMENT = "announcement"
    }

    /**
     * Collects all actions for analytics
     */
    object Action {
        val SHARED = "shared"
        val OPENED = "opened"
        val FAVOURITE_ADD = "favourite added"
        val FAVOURITE_DEL = "favourite removed"
        val EXPORT_CALENDAR = "Exported to calendar"
        val LINK_CLICKED = "Clicked external link"
    }

    companion object : SharedPreferences.OnSharedPreferenceChangeListener {
        val LOGTAG = "ANAL"
        lateinit var tracker: Tracker
        lateinit var context: Context

        fun init(context: Context) {
            logd { "Initializing Google Analytics Tracking" }

            // Get shared preferences
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)!!

            //connect callback
            preferences.registerOnSharedPreferenceChangeListener(this)

            this.context = context

            updateTracking(preferences)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            if (key.contains("analytics")) {
                logd { "Analytics settings have been updated" }
                updateTracking(sharedPreferences);
            }
        }

        /**
         * Update the tracking settings
         */
        private fun updateTracking(preferences: SharedPreferences) {
            logv { "Updating tracking to new stats" }

            // Set app-level opt out
            val analytics_on = preferences.getBoolean(context.resources.getString(R.string.settings_tag_analytics_enabled), true)

            GoogleAnalytics.getInstance(context).appOptOut = analytics_on

            // Start tracking

            // Set debug or production version
            if (BuildConfig.DEBUG) {
                tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-2")
            } else {
                tracker = GoogleAnalytics.getInstance(context).newTracker("UA-76443357-1")
            }

            // Set sampling rate
            tracker.setSampleRate(100.0)

            //Track exceptions
            tracker.enableExceptionReporting(true)

            // Anonymize IP
            tracker.setAnonymizeIp(true)
        }

        /**
         * Change screen and report
         */
        fun screen(screenName: String) {
            tracker.setScreenName(screenName)
            tracker.send(HitBuilders.ScreenViewBuilder().build())
        }

        /**
         * Send an event to analytics
         */
        fun event(eventBuilder: HitBuilders.EventBuilder) =
                tracker.send(eventBuilder.build())

        /**
         * Send an event to analytics using predefined statuses
         */
        fun event(category: String, action: String, label: String) =
                event(HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label))

        /**
         * Track an exception
         */
        fun exception(description: String, fatal: Boolean = false) =
                tracker.send(HitBuilders.ExceptionBuilder()
                        .setDescription(description)
                        .setFatal(fatal)
                        .build())

        /**
         * Tracks a non-fatal exception, getting it's stacktrace from context
         */
        fun exception(ex: Throwable) {
            // Get the stats regarding the method outside
            val stackTrace = Thread.currentThread().stackTrace[2]
            val mess = "${stackTrace.className}.${stackTrace.methodName}:${stackTrace.lineNumber} ${ex.javaClass.simpleName}"

            if (mess.length >= 100) {
                exception(mess.substring(0, 100))
            } else {
                exception(mess)
            }
        }
    }
}