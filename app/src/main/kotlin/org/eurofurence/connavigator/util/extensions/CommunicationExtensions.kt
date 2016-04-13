package org.eurofurence.connavigator.util.extensions

import android.support.v4.app.Fragment
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI

/**
 * Invokes the code if the container is a root API.
 */
fun Fragment.applyRoot(block: RootAPI.() -> Unit) = context.let {
    if (it is RootAPI)
        block(it)
}

/**
 * Invokes the code if the container is a root API.
 */
fun <R> Fragment.letRoot(block: (RootAPI) -> R): R = context.let {
    if (it is RootAPI)
        block(it)
    else
        throw IllegalStateException("Not in root")
}

/**
 * Invokes the code if the item is a content API.
 */
fun Fragment.applyContent(block: ContentAPI.() -> Unit) {
    if (this is ContentAPI)
        block(this)
}

/**
 * Invokes the code if the item is a content API.
 */
fun <R> Fragment.letContent(block: (ContentAPI) -> R):R =
        if (this is ContentAPI)
            block(this)
        else
            throw IllegalStateException("Not in root")