@file:Suppress("unused")

package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.KotprefModel
import io.reactivex.subjects.BehaviorSubject
import org.eurofurence.connavigator.util.notify

/**
 * Created by requinard on 6/20/17.
 */
object AnalyticsPreferences : KotprefModel() {
    val observer: BehaviorSubject<Boolean> = BehaviorSubject.create<Boolean>()
    var enabled: Boolean by booleanPref(false).notify(observer)
    var performanceTracking: Boolean by booleanPref(false).notify(observer)
    var pollingInterval by intPref(100)
}