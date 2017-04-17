package org.eurofurence.connavigator.util.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI

fun Fragment.findRoot(): RootAPI? = context.let {
    when (it) {
        is RootAPI -> it
        is Fragment -> it.findRoot()
        else -> null
    }
}

/**
 * Invokes the code if the container is a root API.
 */
inline fun Fragment.applyOnRoot(block: RootAPI.() -> Unit) = findRoot().let {
    if (it is RootAPI)
        it.block()
}

/**
 * Invokes the code if the container is a root API.
 */
inline fun <R> Fragment.letRoot(block: (RootAPI) -> R): R? = findRoot().let {
    if (it is RootAPI)
        block(it)
    else
        null
}

/**
 * Invokes the code if the item is a content API.
 */
fun FragmentActivity.applyOnContent(block: ContentAPI.() -> Unit) {
    // For all Content API fragments
    for (content in supportFragmentManager.fragments.orEmpty())
        if (content is Fragment && content is ContentAPI) {
            // Recursively apply first, then apply on item itself
            content.applyOnContent(block)
            content.block()
        }
}

/**
 * Invokes the code if the item is a content API.
 */
fun Fragment.applyOnContent(block: ContentAPI.() -> Unit) {
    // For all Content API fragments
    for (content in childFragmentManager.fragments.orEmpty())
        if (content is Fragment && content is ContentAPI) {
            // Recursively apply first, then apply on item itself
            content.applyOnContent(block)
            content.block()
        }
}

/**
 * Invokes the code if the item is a content API.
 */
inline fun <R> FragmentActivity.letContent(block: (ContentAPI) -> R): List<R> =
        supportFragmentManager.fragments.filterIsInstance(ContentAPI::class.java).map(block)
