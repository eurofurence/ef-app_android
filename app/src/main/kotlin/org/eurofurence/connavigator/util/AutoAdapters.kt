package org.eurofurence.connavigator.util

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoContext
import java.util.*
import kotlin.collections.AbstractList

/**
 * Binding composer, see [from] and [into].
 * @param delegate The context to create the view with.
 */
class AutoBehaviour<E, T>(delegate: AnkoContext<T>) : AnkoContext<T> by delegate {

    /**
     * Backing for the resetter.
     */
    private var resetterBacking: () -> Unit = {}

    /**
     * Backing for the binder.
     */
    private var binderBacking: (E, List<E>, List<E>) -> Unit = { _, _, _ -> }

    /**
     * Gets the current binder, a composed method setting the properties in the given view.
     */
    val binder: (E, List<E>, List<E>) -> Unit
        get() = { it, before, after ->
            resetterBacking()
            binderBacking(it, before, after)
        }

    /**
     * Gets the current resetter, a composed method detaching the given view.
     */
    val resetter: () -> Unit get() = resetterBacking

    /**
     * In the context of an auto binder receiver, helps shorten the definition of a generator.
     */
    fun <V> from(f: E.() -> V) = f

    /**
     * In the context of an auto binder receiver, helps shorten the definition of a generator.
     */
    fun <V> fromAll(f: E.(List<E>, List<E>) -> V) = f

    /**
     * Binds a generator to a receiver.
     */
    infix fun <V> (E.() -> V).into(receiver: (V) -> Unit) {
        val current = binderBacking
        binderBacking = { it, before, after ->
            current(it, before, after)
            receiver(this(it))
        }
    }

    /**
     * Binds a generator to a receiver, the generator may inspect leading and trailing elements.
     */
    infix fun <V> (E.(List<E>, List<E>) -> V).into(receiver: (V) -> Unit) {
        val current = binderBacking
        binderBacking = { it, before, after ->
            current(it, before, after)
            receiver(this(it, before, after))
        }
    }

    /**
     * Adds a resetter.
     */
    fun reset(block: () -> Unit) {
        val current = resetterBacking
        resetterBacking = {
            current()
            block()
        }
    }
}

/**
 * View holder binding via an auto binder.
 * @property autoBehaviour The auto binder used to bind the view.
 * @property view The view that is bound and held.
 */
class AutoViewHolder<E>(val autoBehaviour: AutoBehaviour<E, ViewGroup>, view: View) : RecyclerView.ViewHolder(view)

/**
 * Base class for auto adapters.
 * @property id The identity computation for the element.
 * @property create The view creation and binding composition function.
 */
abstract class AutoAdapter<E>(val id: E.() -> Any?, val create: AutoBehaviour<E, ViewGroup>.() -> View)
    : RecyclerView.Adapter<AutoViewHolder<E>>() {
    init {
        super.setHasStableIds(true)
    }

    final override fun setHasStableIds(hasStableIds: Boolean) {
        throw IllegalStateException("Trying to reset stable IDs.")
    }

    abstract fun getItem(position: Int): E

    override fun getItemId(position: Int) =
            when (val from = getItem(position).id()) {
                null -> 0L
                // Anything up to a long is directly mapped.
                is Long -> from
                is Number -> from.toLong()

                // UUIDs are folded over to a single long.
                is UUID -> from.mostSignificantBits xor from.leastSignificantBits

                // Strings are hashed in two partitions.
                is String -> from.substring(0, from.length / 2).hashCode().toLong() shl 32 or
                        from.substring(from.length / 2).hashCode().toLong()

                // Anything else is just hashed.
                else -> from.hashCode().toLong()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AutoViewHolder<E> {
        val context = AnkoContext.createReusable(parent.context, parent, false)
        val autoBinder = AutoBehaviour<E, ViewGroup>(context)
        val view = autoBinder.create()
        return AutoViewHolder(autoBinder, view)
    }

    override fun onBindViewHolder(holder: AutoViewHolder<E>, position: Int) {
        holder.autoBehaviour.binder(
                getItem(position),
                object : AbstractList<E>() {
                    override val size: Int
                        get() = position

                    override fun get(index: Int) =
                            getItem(position - 1 - index)
                },
                object : AbstractList<E>() {
                    override val size: Int
                        get() = maxOf(0, itemCount - (position + 1))

                    override fun get(index: Int) =
                            getItem(position + 1 + index)
                })
    }

    override fun onViewDetachedFromWindow(holder: AutoViewHolder<E>) {
        holder.autoBehaviour.resetter()
    }
}

/**
 * A list auto adapter.
 * @param id The identity computation for the element.
 * @param create The view creation and binding composition function.
 */
class ListAutoAdapter<E>(id: E.() -> Any?, create: AutoBehaviour<E, ViewGroup>.() -> View) : AutoAdapter<E>(id, create) {
    val list = mutableListOf<E>()

    override fun getItemCount() = list.size

    override fun getItem(position: Int) = list[position]
}