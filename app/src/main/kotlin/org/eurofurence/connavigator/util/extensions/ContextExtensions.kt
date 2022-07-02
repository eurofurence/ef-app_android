package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import org.eurofurence.connavigator.ui.views.FontDrawable


@Deprecated("Try to avoid dropin")
fun Context.createFADrawable(faRes: Int, isSolid: Boolean = true, isBrand: Boolean = false) =
    FontDrawable(this, faRes, isSolid, isBrand)

fun Context.browse(url: String): Boolean {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        return true
    } catch (ex: Throwable) {
        return false
    }
}

// TODO: More config.
fun Context.alert(text: String, title: String) =
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(text)
