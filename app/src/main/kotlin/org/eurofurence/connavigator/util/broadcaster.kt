package org.eurofurence.connavigator.util

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import org.eurofurence.connavigator.ui.FragmentViewEvent

/**
 * Created by David on 14-6-2016.
 */
object simpleBroadcaster {
    fun cast(message:String, context: Context){
        val broadcastIntent = Intent(FragmentViewEvent.EVENT_STATUS_CHANGED)
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent)
    }
}