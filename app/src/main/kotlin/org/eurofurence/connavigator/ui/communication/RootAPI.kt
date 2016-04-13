package org.eurofurence.connavigator.ui.communication

import io.swagger.client.model.EventEntry
import io.swagger.client.model.Info
import org.eurofurence.connavigator.database.Database

/**
 * Created by Pazuzu on 12.04.2016.
 */
interface RootAPI {
    val database: Database

    fun navigateToEvent(eventEntry: EventEntry)

    fun navigateToInfo(info: Info)
}