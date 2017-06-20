package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel

/**
 * Created by requinard on 6/20/17.
 */
object AppPreferences: KotprefModel() {
    var showOldAnnouncements by booleanPref()
    var shortenDates by booleanPref(true)
}