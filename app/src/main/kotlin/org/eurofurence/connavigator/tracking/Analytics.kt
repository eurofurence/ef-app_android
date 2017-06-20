package org.eurofurence.connavigator.tracking

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.google.firebase.analytics.FirebaseAnalytics
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.limit
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv
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
        fun init(context: Context){
            analytics = FirebaseAnalytics.getInstance(context)
        }


        /**
         * Change screen and report
         */
        fun screen(screenName: String) = analytics.logEvent(
                FirebaseAnalytics.Event.VIEW_ITEM,
                bundleOf(FirebaseAnalytics.Param.ITEM_NAME to screenName,
                        FirebaseAnalytics.Param.CONTENT_TYPE to "screen")
        )

        fun event(category: String, action: String, label: String) = analytics.logEvent(
                FirebaseAnalytics.Event.SELECT_CONTENT,
                bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to category,
                        FirebaseAnalytics.Param.ITEM_CATEGORY to action,
                        FirebaseAnalytics.Param.ITEM_NAME to label
                )
        )

        fun exception(ex: Throwable) = Unit
    }
}