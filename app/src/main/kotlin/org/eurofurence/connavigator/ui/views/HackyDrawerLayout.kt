package org.eurofurence.connavigator.ui.views

import android.content.Context
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.view.MotionEvent
import org.eurofurence.connavigator.util.extensions.catchToAnyFalse

/**
 * Override the standard drawer layout so we can actually use PhotoView
 */
class HackyDrawerLayout(context: Context, attr: AttributeSet?, i: Int) : DrawerLayout(context, attr, i) {
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 1)

    constructor(context: Context) : this(context, null, 1)

    override fun onInterceptTouchEvent(ev: MotionEvent?) = catchToAnyFalse {
        super.onInterceptTouchEvent(ev)
    }
}