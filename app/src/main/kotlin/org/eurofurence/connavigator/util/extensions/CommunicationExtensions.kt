package org.eurofurence.connavigator.util.extensions

import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI

/**
 * Invokes the code if the container is a root API.
 */
fun ContentAPI.applyRoot(block: RootAPI.() -> Unit) = parent?.block()

/**
 * Invokes the code if the container is a root API.
 */
fun <R> ContentAPI.letRoot(block: (RootAPI) -> R): R = block(parent!!)
