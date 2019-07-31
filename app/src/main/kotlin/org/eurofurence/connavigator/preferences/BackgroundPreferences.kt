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
    val observer = BehaviorSubject.create<LoadingState>().apply { onNext(LoadingState.UNINITIALIZED) }
    var eventPagerMode by enumValuePref(EventPagerMode.DAYS)
    var lastKnownVersion by stringPref("")
    var loadingState by enumValuePref(LoadingState.UNINITIALIZED).notify(observer)
    var hasLoadedOnce by booleanPref(false)
}

enum class LoadingState {
    UNINITIALIZED,
    PENDING,
    LOADING_DATA,
    LOADING_IMAGES,
    SUCCEEDED,
    FAILED
}