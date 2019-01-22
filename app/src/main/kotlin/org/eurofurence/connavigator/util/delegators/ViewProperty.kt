@file:Suppress("unused")

package org.eurofurence.connavigator.util.delegators

import android.app.Activity
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import kotlin.reflect.KProperty

/**
 * A view is inserted and casted. There is a number of utility constructors, see [view], [header], [view],
 * [view].
 * @param T The method used to find a view in the container by string
 */
class ViewProperty<in T, U : View>(private val findView: (T, String) -> U) {

    operator fun getValue(container: T, property: KProperty<*>): U {
        return findView(container, property.name)
    }
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is an activity.
 * @param T The class of the view to inject
 */
@Deprecated("Use Anko instead of layout inflation.")
inline fun <reified T : View> view(nameInResource: String? = null) = ViewProperty {
    container: Activity, name ->
    // Find view by name, cast it
    val r = container.findViewById<T>(
            container.resources.getIdentifier(nameInResource ?: name, "id", container.packageName))

    if (r == null)
        throw IllegalArgumentException("Cannot locate ${nameInResource ?: name} in $container.")
    else
        r as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a fragment.
 * @param T The class of the view to inject
 */
@Deprecated("Use Anko instead of layout inflation.")
inline fun <reified T : View> Fragment.view(nameInResource: String? = null) = ViewProperty {
    container: Fragment, name ->
    if (container.view == null)
        throw NullPointerException("Fragments view is not populated")

    // Find view by name, cast it
    val r = container.view!!.findViewById<T>(
            container.resources.getIdentifier(nameInResource ?: name, "id", container.context!!.packageName))

    if (r == null)
        throw IllegalArgumentException("Cannot locate ${nameInResource ?: name} in $container.")
    else
        r as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param T The class of the view to inject
 */
@Deprecated("Use Anko instead of layout inflation.")
inline fun <reified T : View> View.view(nameInResource: String? = null) = ViewProperty {
    container: View, name ->
    // Find view by name, cast it
    val r = container.findViewById<T>(
            container.resources.getIdentifier(nameInResource ?: name, "id", container.context.packageName))
    if (r == null)
        throw IllegalArgumentException("Cannot locate ${nameInResource ?: name} in $container.")
    else
        r as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view holder.
 * @param T The class of the view to inject
 */
@Deprecated("Use Anko instead of layout inflation.")
inline fun <reified T : View> ViewHolder.view(nameInResource: String? = null) = ViewProperty {
    container: ViewHolder, name ->
    // Find view by name, cast it
    val r = container.itemView.findViewById<T>(
            container.itemView.resources.getIdentifier(
                    nameInResource ?: name, "id",
                    container.itemView.context.packageName))
    if (r == null)
        throw IllegalArgumentException("Cannot locate ${nameInResource ?: name} in $container.")
    else
        r as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param T The class of the view to inject
 */
@Deprecated("Use Anko instead of layout inflation.")
inline fun <reified T : View> Activity.header(
        crossinline navigationView: () -> NavigationView, index: Int = 0, nameInResource: String? = null) =
        ViewProperty {
            container: Activity, name ->
            // Resolve header
            val header = navigationView().getHeaderView(index)

            // Find view by name, cast it
            val r = header.findViewById<T>(container.resources.getIdentifier(
                    nameInResource ?: name, "id",
                    container.packageName))

            if (r == null)
                throw IllegalArgumentException("Cannot locate ${nameInResource ?: name} in $container.")
            else
                r as T
        }