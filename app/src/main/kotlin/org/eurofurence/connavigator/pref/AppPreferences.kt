package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import org.eurofurence.connavigator.BuildConfig

/**
 * Created by requinard on 6/20/17.
 */
object AppPreferences: KotprefModel() {
    var isFirstRun by booleanPref(true)
    var lastKnownVersion by stringPref(BuildConfig.VERSION_NAME)
    var showOldAnnouncements by booleanPref()
    var shortenDates by booleanPref(true)
    var notificationMinutesBefore by intPref(30)
    var dialogOnEventPress by booleanPref(false)
}