package org.eurofurence.connavigator.util

import org.eurofurence.connavigator.preferences.DebugPreferences
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Proxy the datetime object so we can fudge the dates
 */
object DatetimeProxy {
    private val addedPeriodInSeconds get() = Duration(DebugPreferences.addedTimeInSeconds)

    fun now() = DateTime.now().plus(addedPeriodInSeconds)
    val addedSeconds get() = addedPeriodInSeconds.standardSeconds.toInt()

    /**
     * Reset the internal offset.
     */
    fun reset() {
        DebugPreferences.addedTimeInSeconds = 0
    }

    fun addSeconds(seconds: Long) {
        DebugPreferences.addedTimeInSeconds += seconds * 1000
    }

    fun addMinutes(minutes: Long) = addSeconds(minutes * 60)

    fun addHours(hours: Long) = addMinutes(hours * 60)

    fun addDays(days: Long) = addHours(days * 24)
}