package org.eurofurence.connavigator.dropins.fa

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class FaView : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
            : super(context, attrs, defStyle) {
        typeface = FaCache.fontAwesome(context)
    }

    constructor(context: Context, attrs: AttributeSet)
            : super(context, attrs) {
        typeface = FaCache.fontAwesome(context)
    }

    constructor(context: Context)
            : super(context) {
        typeface = FaCache.fontAwesome(context)
    }
}