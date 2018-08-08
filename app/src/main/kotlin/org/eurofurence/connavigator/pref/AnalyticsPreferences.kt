package org.eurofurence.connavigator.pref

import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.KotprefModel
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.eurofurence.connavigator.util.notify

/**
 * Created by requinard on 6/20/17.
 */
object AnalyticsPreferences: KotprefModel() {
    val observer = BehaviorSubject.create<Boolean>()
    var enabled by booleanPref(false).notify(observer)
    var performanceTracking by booleanPref(false).notify(observer)
    var pollingInterval by intPref(100)
}