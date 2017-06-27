package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import org.eurofurence.connavigator.util.EmbeddedBroadcastReceiver
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver

/**
 * Creates a embedded broadcast receiver, needs to be registered to listen.
 * @param action The action name
 * @param method The method to invoke when triggered
 */
fun Context.receiver(action: String, method: Context.(Intent) -> Unit) =
        EmbeddedBroadcastReceiver(this, IntentFilter(action), method)

/**
 * Creates a embedded local broadcast receiver, needs to be registered to listen.
 * @param action The action name
 * @param method The method to invoke when triggered
 */
fun Context.localReceiver(action: String, method: Context.(Intent) -> Unit) =
        EmbeddedLocalBroadcastReceiver(this, IntentFilter(action), method)