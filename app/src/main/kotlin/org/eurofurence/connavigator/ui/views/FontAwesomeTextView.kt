package org.eurofurence.connavigator.ui.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

enum class FontAwesomeType {
    Solid,
    Brands,
    Regular,
    Old
}

class FontAwesomeTextView : AppCompatTextView {
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        type = FontAwesomeType.Old
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        type = FontAwesomeType.Old
    }

    constructor(context: Context) : super(context) {
        type = FontAwesomeType.Old
    }

    var type: FontAwesomeType?
        get() {
            return if (typeface === FontCache[context, FontCache.FA_FONT_SOLID])
                FontAwesomeType.Solid
            else if (typeface === FontCache[context, FontCache.FA_FONT_BRANDS])
                FontAwesomeType.Brands
            else if (typeface === FontCache[context, FontCache.FA_FONT_REGULAR])
                FontAwesomeType.Regular
            else if (typeface === FontCache[context, FontCache.FA_OLD])
                FontAwesomeType.Old
            else
                null
        }
        set(value) {
            when (value) {
                FontAwesomeType.Solid -> typeface = FontCache[context, FontCache.FA_FONT_SOLID]
                FontAwesomeType.Brands -> typeface = FontCache[context, FontCache.FA_FONT_BRANDS]
                FontAwesomeType.Regular -> typeface = FontCache[context, FontCache.FA_FONT_REGULAR]
                FontAwesomeType.Old -> typeface = FontCache[context, FontCache.FA_OLD]
                null -> throw IllegalArgumentException("Cannot be null on set")
            }
        }
}