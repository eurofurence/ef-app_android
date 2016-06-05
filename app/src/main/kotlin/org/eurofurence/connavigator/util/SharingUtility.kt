package org.eurofurence.connavigator.util

import android.content.Intent

/**
 * Created by David on 6/5/2016.
 */
class SharingUtility {
    companion object {
        fun share(text: String): Intent {
            val shareIntent = Intent();

            shareIntent.setAction(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, text)
            shareIntent.setType("text/plain")
            return shareIntent
        }
    }
}