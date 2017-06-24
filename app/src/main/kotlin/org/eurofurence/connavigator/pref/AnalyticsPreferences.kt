package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel

/**
 * Created by requinard on 6/20/17.
 */
object AnalyticsPreferences: KotprefModel() {
    var enabled by booleanPref(true)
    var performanceTracking by booleanPref(true)
    var pollingInterval by intPref(100)
}