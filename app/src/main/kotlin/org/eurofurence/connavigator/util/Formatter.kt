package org.eurofurence.connavigator.util

import io.swagger.client.model.DealerRecord
import io.swagger.client.model.EventRecord
import java.util.*

/**
 * Created by David on 11-5-2016.
 */
object Formatter {
    private const val splitter_1 = "–"
    private const val splitter_2 = "—"

    /*
    Splits according to our splitters
     */
    private fun split(string: String): List<String> {
        return string.split(splitter_1, splitter_2)
    }

    fun shareEvent(event: EventRecord): String {
        return "Check out ${event.title}!\n${createUrl("event", event.id)}"
    }

    fun shareDealer(dealer: DealerRecord): String {
        return "Check out ${dealer.displayName} at the dealers den!\n${createUrl("dealer", dealer.id)}"
    }

    private fun createUrl(type: String, id: UUID): String {
        return "https://app.eurofurence.org/web/#/$type/$id"
    }
}