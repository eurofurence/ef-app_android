package org.eurofurence.connavigator.util.extensions

import android.widget.LinearLayout
import org.jetbrains.anko.dip

fun LinearLayout.weight(fl: Float) {
    val layoutParams = LinearLayout.LayoutParams(dip(0), LinearLayout.LayoutParams.WRAP_CONTENT, fl)
    this.layoutParams = layoutParams
}