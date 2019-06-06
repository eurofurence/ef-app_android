@file:Suppress("unused")

package org.eurofurence.connavigator.util.v2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.serialization.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Result of a broadcast action.
 */
enum class BroadcastResult {
    /**
     * Local broadcast was sent and a receiver could handle it.
     */
    SEEN_HANDLED,

    /**
     * Broadcast was sent, if handled is not clear.
     */
    SENT,

    /**
     * Local broadcast was sent and no receiver could handle it.
     */
    SEEN_UNHANDLED
}

/**
 * A listener specification.
 */
data class ListenerSpec<T : Any>(val external: Boolean, val action: String, val serializer: KSerializer<T>) {
    /**
     * Binds context for this listener specification
     */
    fun bind(context: Context) =
            ListenerSpecBinding(this, context)

    operator fun provideDelegate(context: Context, kProperty: KProperty<*>) =
            bind(context).let {
                object : ReadOnlyProperty<Context, ListenerSpecBinding<T>> {
                    override fun getValue(thisRef: Context, property: KProperty<*>) = it
                }
            }
}

data class ListenerSpecBinding<T : Any>(val listenerSpec: ListenerSpec<T>, val context: Context) {
    private val registered = arrayListOf<BroadcastReceiver>()

    /**
     * Sends [t] to the broadcast receivers of the same specification.
     * @return Returns the handling status.
     */
    fun send(t: T): BroadcastResult {
        // Write intent
        val intent = Intent(listenerSpec.action)
        val output = IntentOutput(intent)
        output.encode(listenerSpec.serializer, t)

        // Send intent as broadcast
        return if (listenerSpec.external) {
            context.sendBroadcast(intent)
            BroadcastResult.SENT
        } else {
            if (LocalBroadcastManager.getInstance(context).sendBroadcast(intent))
                BroadcastResult.SEEN_HANDLED
            else
                BroadcastResult.SEEN_UNHANDLED
        }
    }

    /**
     * Listens to broadcasts matching the specification.
     * @return Returns the registered broadcast receiver.
     */
    fun listen(block: Context.(T) -> Unit): BroadcastReceiver {
        val rec = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val input = IntentInput(intent)
                val item = input.decode(listenerSpec.serializer)
                context.block(item)
            }
        }

        registered += rec

        if (listenerSpec.external)
            context.registerReceiver(rec, IntentFilter(listenerSpec.action))
        else
            LocalBroadcastManager.getInstance(context).registerReceiver(rec, IntentFilter(listenerSpec.action))

        return rec
    }

    /**
     * Stops listening the given broadcast receiver, it must have been registered with [listen] before in the same
     * context.
     */
    fun unlisten(broadcastReceiver: BroadcastReceiver): Boolean {
        // Remove receiver
        if (!registered.remove(broadcastReceiver))
            return false

        // Do unregistration
        if (listenerSpec.external)
            context.unregisterReceiver(broadcastReceiver)
        else
            LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)

        // Return true since we passed a valid removal
        return true
    }

    /**
     * Stops listening to all broadcast receivers in this context.
     */
    fun unlistenAll(): Boolean {
        if (registered.isEmpty())
            return false

        // Do unregistration on all
        for (r in registered)
            if (listenerSpec.external)
                context.unregisterReceiver(r)
            else
                LocalBroadcastManager.getInstance(context).unregisterReceiver(r)

        // Clear backing
        registered.clear()

        return true
    }
}

/**
 * Makes a listener specification form [external] and [action], inferring the serializer.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> createSpec(external: Boolean, action: String) =
        ListenerSpec(external, action, T::class.serializer())

/**
 * Makes an internal listener specification form [action], inferring the serializer.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> internalSpec(action: String) =
        createSpec<T>(false, action)

/**
 * Makes an external listener specification form [action], inferring the serializer.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> externalSpec(action: String) =
        createSpec<T>(true, action)


/**
 * Makes a listener specification form [external], inferring the action from the type name and also the serializer.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> createSpec(external: Boolean) =
        ListenerSpec(external, T::class.qualifiedName ?: error("No qualified name!"), T::class.serializer())

/**
 * Makes an internal listener specification, inferring the action from the type name and also the serializer.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> internalSpec() =
        createSpec<T>(false)

/**
 * Makes an external listener specification, inferring the action from the type name and also the serializer.
 */
@ImplicitReflectionSerializer
inline fun <reified T : Any> externalSpec() =
        createSpec<T>(true)
