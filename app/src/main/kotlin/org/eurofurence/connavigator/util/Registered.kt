package org.eurofurence.connavigator.util

/**
 * A context-registered object.
 */
interface Registered {
    /**
     * Registers the receiver
     * @return Returns true if registration changed the state of the receiver holder
     */
    fun register(): Boolean

    /**
     * Un-registers the receiver
     * @return Returns true if registration changed the state of the receiver holder
     */
    fun unregister(): Boolean
}