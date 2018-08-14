package org.eurofurence.connavigator.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

/**
 * A self-contained  broadcast receiver.
 * @param context The context this receiver operates in
 * @param intentFilter The intent filter to apply
 * @param method The method to invoke when receiving
 */
class EmbeddedBroadcastReceiver(
        val context: Context,
        private val intentFilter: IntentFilter,
        val method: Context.(Intent) -> Unit) : BroadcastReceiver(), Registered {
    override fun onReceive(context: Context, intent: Intent) =
            context.method(intent)

    override fun register() =
            context.registerReceiver(this, intentFilter).let { true }

    override fun unregister() =
            context.unregisterReceiver(this).let { true }
}

