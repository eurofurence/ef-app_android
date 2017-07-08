package org.eurofurence.connavigator.util.v2

import android.support.v4.widget.TextViewCompat
import android.widget.TextView

/**
 * Sets the compat appearance
 */
var TextView.compatAppearance: Int
    get() = error("Setting is not possible")
    set(value) {
        TextViewCompat.setTextAppearance(this, value)
    }