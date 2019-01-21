package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import org.eurofurence.connavigator.ui.fragments.EventPagerMode

/**
 * Holds any non-public preferences, things  we don't show to the user
 */
object BackgroundPreferences: KotprefModel() {
    var eventPagerMode by enumValuePref(EventPagerMode.DAYS)
    var closeAppImmediately by booleanPref(false)
}