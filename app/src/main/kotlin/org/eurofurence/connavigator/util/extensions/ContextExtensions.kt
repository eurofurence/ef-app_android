package org.eurofurence.connavigator.util.extensions

import android.content.Context
import info.androidhive.fontawesome.FontDrawable

fun Context.createFADrawable(faRes: Int, isSolid: Boolean = true, isBrand: Boolean = false) =
        FontDrawable(this, faRes, isSolid, isBrand)