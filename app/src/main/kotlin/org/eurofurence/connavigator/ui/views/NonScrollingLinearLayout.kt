package org.eurofurence.connavigator.ui.views

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager

/**
 * Created by David on 6/5/2016.
 */
class NonScrollingLinearLayout(val context: FragmentActivity?) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean = false
}