package org.eurofurence.connavigator.dropins

import android.content.Context
import android.content.Intent
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.shareString

fun Context.share(content: String, title:String) {
    // TODO: Verify
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TITLE,  title)
        putExtra(Intent.EXTRA_TEXT, content)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}