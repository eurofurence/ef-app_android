package org.eurofurence.connavigator.ui.views

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet


class FontAwesomeTextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        val tf = Typeface.createFromAsset(context.assets,
                "fontawesome.ttf")
        typeface = tf
    }
}