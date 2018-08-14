package org.eurofurence.connavigator.broadcast

import android.content.Context
import android.support.v4.content.LocalBroadcastManager
import org.eurofurence.connavigator.util.extensions.toIntent
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * App level broadcasts for data updates that invalidates the UI
 */
object DataChanged : AnkoLogger {
    private const val DATACHANGED = "DATACHANGED"

    fun fire(context: Context, message: String) {
        info { "Sending data changed event with message \"$message\"" }
        val intent = DATACHANGED.toIntent { "message" to message }

        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}