package org.eurofurence.connavigator.preferences

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import io.reactivex.subjects.BehaviorSubject
import org.eurofurence.connavigator.ui.fragments.EventPagerMode
import org.eurofurence.connavigator.util.notify

/**
 * Holds any non-public preferences, things  we don't show to the user
 */
object BackgroundPreferences : KotprefModel() {
    val observer = BehaviorSubject.create<Boolean>().apply { onNext(false) }
    var eventPagerMode by enumValuePref(EventPagerMode.DAYS)
    var closeAppImmediately by booleanPref(false)
    var lastKnownVersion by stringPref("")
    var isLoading by booleanPref(false).notify(this.observer)
}