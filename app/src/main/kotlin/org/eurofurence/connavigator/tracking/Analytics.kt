package org.eurofurence.connavigator.tracking

import android.app.Activity
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.perf.FirebasePerformance
import com.pawegio.kandroid.i
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.jetbrains.anko.bundleOf

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
        val SETTINGS = "settings"
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
        val INCOMING = "Incoming from website"
        val CHANGED = "changed"
    }

    companion object {
        lateinit var analytics: FirebaseAnalytics
        val performance by lazy { FirebasePerformance.getInstance() }
        fun init(context: Context) {
            analytics = FirebaseAnalytics.getInstance(context).apply {
                setAnalyticsCollectionEnabled(AnalyticsPreferences.enabled)
            }

            performance.isPerformanceCollectionEnabled = AnalyticsPreferences.performanceTracking
        }

        /**
         * Send new screen to analytics
         */
        fun screen(activity: Activity, fragmentName: String) = analytics.setCurrentScreen(activity, fragmentName, null)


        /**
         * Send event to analytics
         */
        fun event(category: String, action: String, label: String) = analytics.logEvent(
                FirebaseAnalytics.Event.SELECT_CONTENT,
                bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to category,
                        FirebaseAnalytics.Param.ITEM_CATEGORY to action,
                        FirebaseAnalytics.Param.ITEM_NAME to label
                )
        )

        /**
         * Track caught exceptions
         */
        fun exception(ex: Throwable) = FirebaseCrash.report(ex)
    }
}