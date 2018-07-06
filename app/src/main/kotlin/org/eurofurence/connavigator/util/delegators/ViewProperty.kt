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
class ViewProperty<in T, U : View>(val findView: (T, String) -> U) {
    /**
     * The status of the property delegate.
     */
    var initialized = false

    /**
     * The store for the item in the property delegate.
     */
    lateinit var item: U

    operator fun getValue(container: T, property: KProperty<*>): U {
        // If already initialized, return that value
        if (initialized)
            return item
        // Find by name, then set initialized and return the item
        item = findView(container, property.name)
        initialized = true
        return item
    }
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is an activity.
 * @param T The class of the view to inject
 */
inline fun <reified T : View> view(nameInResource: String? = null) = ViewProperty { container: Activity, name ->
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
inline fun <reified T : View> Fragment.view(nameInResource: String? = null) = ViewProperty { container: Fragment, name ->
    // Find view by name, cast it
    // Find ID in context
    val id = container.resources.getIdentifier(nameInResource ?: name, "id", requireContext().packageName)

    // Find view in view
    container.view?.let { view ->
        view.findViewById<T>(id) as T
    } ?: throw IllegalArgumentException("Cannot locate ${nameInResource ?: name} in $container.")
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param T The class of the view to inject
 */
inline fun <reified T : View> View.view(nameInResource: String? = null) = ViewProperty { container: View, name ->
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
inline fun <reified T : View> ViewHolder.view(nameInResource: String? = null) = ViewProperty { container: ViewHolder, name ->
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
inline fun <reified T : View> Activity.header(
        crossinline navigationView: () -> NavigationView, index: Int = 0, nameInResource: String? = null) =
        ViewProperty { container: Activity, name ->
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