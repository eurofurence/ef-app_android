package org.eurofurence.connavigator.ui.views

import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager

/**
 * Created by David on 6/5/2016.
 */
class NonScrollingLinearLayout(val context: FragmentActivity?) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean = false
}