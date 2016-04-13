package org.eurofurence.connavigator.util.extensions

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI

/**
 * Invokes the code if the container is a root API.
 */
fun Fragment.applyOnRoot(block: RootAPI.() -> Unit) = context.let {
    if (it is RootAPI)
        it.block()
}

/**
 * Invokes the code if the container is a root API.
 */
fun <R> Fragment.letRoot(block: (RootAPI) -> R): R? = context.let {
    if (it is RootAPI)
        block(it)
    else
        null
}

/**
 * Invokes the code if the item is a content API.
 */
fun FragmentActivity.applyOnContent(block: ContentAPI.() -> Unit) {
    supportFragmentManager.fragments.filterIsInstance(ContentAPI::class.java).forEach(block)
}

/**
 * Invokes the code if the item is a content API.
 */
fun <R> FragmentActivity.letContent(block: (ContentAPI) -> R): List<R> =
        supportFragmentManager.fragments.filterIsInstance(ContentAPI::class.java).map(block)