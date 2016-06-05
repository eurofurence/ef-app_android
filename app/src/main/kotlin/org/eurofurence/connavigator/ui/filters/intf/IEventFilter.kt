package org.eurofurence.connavigator.ui.filters.intf

import android.content.Context
import io.swagger.client.model.EventEntry

/**
 * Created by David on 6/4/2016.
 */
interface IEventFilter {
    fun filter(context: Context, filterVal: Any = Unit): Iterable<EventEntry>

    val scrolling: Boolean get() = false

    fun getTitle(): String

}