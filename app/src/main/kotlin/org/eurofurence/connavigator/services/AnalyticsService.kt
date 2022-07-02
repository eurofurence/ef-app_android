package org.eurofurence.connavigator.services

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import org.eurofurence.connavigator.preferences.AnalyticsPreferences
import org.eurofurence.connavigator.dropins.AnkoLogger


/**o
 * Created by David on 20-4-2016.
 */
class AnalyticsService {
    /**
     * Collects all categories for analytics
     */
    object Category {
        const val EVENT = "event"
        const val DEALER = "dealer"
        const val INFO = "info"
        const val SETTINGS = "settings"
    }

    /**
     * Collects all actions for analytics
     */
    object Action {
        const val SHARED = "shared"
        const val OPENED = "opened"
        const val EXPORT_CALENDAR = "Exported to calendar"
        const val LINK_CLICKED = "Clicked external link"
        const val INCOMING = "Incoming from websites"
        const val CHANGED = "changed"
    }

    companion object : AnkoLogger {
        lateinit var analytics: FirebaseAnalytics
        val performance: FirebasePerformance by lazy { FirebasePerformance.getInstance() }
        fun init(context: Context) {
            analytics = FirebaseAnalytics.getInstance(context).apply {
                setAnalyticsCollectionEnabled(AnalyticsPreferences.enabled)
            }

            performance.isPerformanceCollectionEnabled = AnalyticsPreferences.performanceTracking
        }

        /**
         * Send new screen to analytics
         */
        fun screen(activity: Activity, fragmentName: String) =
            analytics.setCurrentScreen(activity, fragmentName, null)


        /**
         * Send event to analytics
         */
        fun event(category: String, action: String, label: String) = analytics.logEvent(
            FirebaseAnalytics.Event.SELECT_CONTENT,
            Bundle().also {
                it.putString(FirebaseAnalytics.Param.CONTENT_TYPE, category)
                it.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, action)
                it.putString(FirebaseAnalytics.Param.ITEM_NAME, label)
            }
        )

        fun updateSettings() {
            debug { "AnalyticsService: ${AnalyticsPreferences.enabled}; Performance: ${AnalyticsPreferences.performanceTracking}" }
            analytics.setAnalyticsCollectionEnabled(AnalyticsPreferences.enabled)
            performance.isPerformanceCollectionEnabled = AnalyticsPreferences.performanceTracking
        }

        // TODO Verify.
        fun ex(exception: Exception) = FirebaseCrashlytics.getInstance().recordException(exception)
    }
}