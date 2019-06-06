@file:Suppress("unused")

package org.eurofurence.connavigator.util.delegators

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.KProperty

/**
 * A fragment is inserted and casted.
 * @param findFragment The method used to find a fragment in the container by string
 */
class FragmentProperty<in T, out U : Fragment>(private val findFragment: (T, String) -> U) {
    operator fun getValue(container: T, property: KProperty<*>): U =
            findFragment(container, property.name)
}

/**
 * Returns a property delegate for fragment injection, returns a [FragmentProperty]. The context
 * is a fragment activity. Names will be resolved by tag instead of IDs.
 * @param T The class of the fragment to inject
 */
inline fun <reified T : Fragment> Activity.fragmentByTag() = FragmentProperty { container: Activity, name ->
    // Find view by name, cast it
    container.fragmentManager.findFragmentByTag(name) as T
}

/**
 * Returns a property delegate for fragment injection, returns a [FragmentProperty]. The context
 * is a support fragment activity.Names will be resolved by tag instead of IDs.
 * @param T The class of the fragment to inject
 */
inline fun <reified T : Fragment> FragmentActivity.fragmentByTag() = FragmentProperty { container: FragmentActivity, name ->
    // Find view by name, cast it
    container.supportFragmentManager.findFragmentByTag(name) as T
}
