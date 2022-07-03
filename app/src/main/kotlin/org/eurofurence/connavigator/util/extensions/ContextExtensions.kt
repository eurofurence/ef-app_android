package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog

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
