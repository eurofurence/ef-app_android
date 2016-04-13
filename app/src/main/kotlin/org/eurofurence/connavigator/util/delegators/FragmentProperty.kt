package org.eurofurence.connavigator.util.delegators

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import kotlin.reflect.KProperty

/**
 * A fragment is inserted and casted. See [fragment].
 * @param findFragment The method used to find a fragment in the container by string
 */
class FragmentProperty<T, U : Fragment>(val findFragment: (T, String) -> U) {
    operator fun getValue(container: T, property: KProperty<*>): U =
            findFragment(container, property.name)
}

/**
 * Returns a property delegate for fragment injection, returns a [FragmentProperty]. The context
 * is a fragment activity.
 * @param fragmentClass The class of the fragment to inject
 */
fun <T : Fragment> fragment(fragmentClass: Class<T>) = FragmentProperty {
    container: FragmentActivity, name ->
    // Find view by name, cast it
    fragmentClass.cast(container.fragmentManager.findFragmentById(container.resources.getIdentifier(name, "id", container.packageName)))
}

/**
 * Returns a property delegate for fragment injection, returns a [FragmentProperty]. The context
 * is a support fragment activity.
 * @param fragmentClass The class of the fragment to inject
 */
fun <T : Fragment> fragmentInSupport(fragmentClass: Class<T>) = FragmentProperty {
    container: FragmentActivity, name ->
    // Find view by name, cast it
    fragmentClass.cast(container.supportFragmentManager.findFragmentById(container.resources.getIdentifier(name, "id", container.packageName)))
}
