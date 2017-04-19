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
inline fun <reified T : View> view() = ViewProperty {
    container: Activity, name ->
    // Find view by name, cast it
    container.findViewById(container.resources.getIdentifier(name, "id", container.packageName)) as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a fragment.
 * @param T The class of the view to inject
 */
inline fun <reified T : View> Fragment.view() = ViewProperty {
    container: Fragment, name ->
    if (container.view == null)
        throw NullPointerException("Fragments view is not populated")

    // Find view by name, cast it
    container.view!!.findViewById(container.resources.getIdentifier(name, "id", container.context.packageName)) as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param T The class of the view to inject
 */
inline fun <reified T : View> View.view() = ViewProperty {
    container: View, name ->
    // Find view by name, cast it
    container.findViewById(container.resources.getIdentifier(name, "id", container.context.packageName)) as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view holder.
 * @param T The class of the view to inject
 */
inline fun <reified T : View> ViewHolder.view() = ViewProperty {
    container: ViewHolder, name ->
    // Find view by name, cast it
    container.itemView.findViewById(container
            .itemView
            .resources
            .getIdentifier(name, "id", container.itemView.context.packageName)) as T
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param T The class of the view to inject
 */
inline fun <reified T : View> Activity.header(crossinline navigationView: () -> NavigationView, index: Int = 0) =
        ViewProperty {
            container: Activity, name ->
            // Resolve header
            val header = navigationView().getHeaderView(index)

            // Find view by name, cast it
            header.findViewById(container.resources.getIdentifier(name, "id", container.packageName)) as T
        }