package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref

/**
 * Holds preferences for debug status
 */
object DebugPreferences: KotprefModel() {
    // See if we should edit event dates
    var debugDates by booleanPref()

    // How many days to offset by
    var eventDateOffset by intPref()

    // Schedule notifications 5 minutes into the future
    var scheduleNotificationsForTest by booleanPref()
}

enum class DateOffset {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE
}