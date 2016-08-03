package org.eurofurence.connavigator.util

import android.content.Context
import android.media.AudioAttributes
import android.os.Vibrator
import android.provider.MediaStore

/**
 * Created by david on 8/3/16.
 */
class TouchVibrator(context: Context) {
    companion object {
        private val TIME = 30L
    }
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun short() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(TIME)
        }
    }

    fun long() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(longArrayOf(0, TIME, TIME*2), -1)
        }
    }
}
