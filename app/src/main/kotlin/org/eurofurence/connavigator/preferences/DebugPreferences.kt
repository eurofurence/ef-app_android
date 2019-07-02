package org.eurofurence.connavigator.preferences

import com.chibatching.kotpref.KotprefModel

/**
 * Holds preferences for debug status
 */
object DebugPreferences : KotprefModel() {
    var addedTimeInSeconds by longPref(0)

    // Schedule notifications 5 minutes into the future
    var scheduleNotificationsForTest by booleanPref()
}
