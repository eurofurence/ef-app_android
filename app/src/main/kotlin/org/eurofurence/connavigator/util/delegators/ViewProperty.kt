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
 * @param findView The method used to find a view in the container by string
 */
class ViewProperty<T, U : View>(val findView: (T, String) -> U) {
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
 * @param viewClass The class of the view to inject
 */
fun <T : View> view(viewClass: Class<T>) = ViewProperty {
    container: Activity, name ->
    // Find view by name, cast it
    viewClass.cast(container.findViewById(container.resources.getIdentifier(name, "id", container.packageName)))
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a fragment.
 * @param viewClass The class of the view to inject
 */
fun <T : View> Fragment.view(viewClass: Class<T>) = ViewProperty {
    container: Fragment, name ->
    if (container.view == null)
        throw NullPointerException("Fragments view is not populated")

    // Find view by name, cast it
    viewClass.cast(container.view!!.findViewById(container.resources.getIdentifier(name, "id", container.context.packageName)))
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param viewClass The class of the view to inject
 */
fun <T : View> View.view(viewClass: Class<T>) = ViewProperty {
    container: View, name ->
    // Find view by name, cast it
    viewClass.cast(container.findViewById(container.resources.getIdentifier(name, "id", container.context.packageName)))
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view holder.
 * @param viewClass The class of the view to inject
 */
fun <T : View> ViewHolder.view(viewClass: Class<T>) = ViewProperty {
    container: ViewHolder, name ->
    // Find view by name, cast it
    viewClass.cast(container.itemView.findViewById(container.itemView.resources.getIdentifier(name, "id", container.itemView.context.packageName)))
}

/**
 * Returns a property delegate for view injection, returns a [ViewProperty]. The context is a view.
 * @param viewClass The class of the view to inject
 */
fun <T : View> Activity.header(viewClass: Class<T>, navigationView: () -> NavigationView, index: Int = 0) = ViewProperty {
    container: Activity, name ->
    // Resolve header
    val header = navigationView().getHeaderView(index)

    // Find view by name, cast it
    viewClass.cast(header.findViewById(container.resources.getIdentifier(name, "id", container.packageName)))
}