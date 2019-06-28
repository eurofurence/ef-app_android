package org.eurofurence.connavigator.util.extensions

import android.widget.TextView
import org.jetbrains.anko.textResource

var TextView.content: Any
    get() = text
    set(value) = when (value) {
        is CharSequence -> text = value
        is Int -> textResource = value
        else -> text = value.toString()
    }