package org.eurofurence.connavigator.ui.filters.factory

import org.eurofurence.connavigator.ui.filters.*
import org.eurofurence.connavigator.ui.filters.enums.EnumEventRecyclerViewmode

/**
 * Created by David on 6/4/2016.
 */
object EventFilterFactory {
    fun create(mode: EnumEventRecyclerViewmode): IEventFilter =
            when (mode) {
                EnumEventRecyclerViewmode.ALL -> AnyEventFilter()
                EnumEventRecyclerViewmode.DAY -> DayEventFilter()
                EnumEventRecyclerViewmode.FAVORITED -> AnyFavoritedEventFilter()
                EnumEventRecyclerViewmode.CURRENT -> CurrentEventFilter()
                EnumEventRecyclerViewmode.UPCOMING -> UpcomingEventFilter()
                EnumEventRecyclerViewmode.SEARCH -> SearchEventFilter()
                else -> AnyEventFilter()
            }

}