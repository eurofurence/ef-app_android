package org.eurofurence.connavigator.util

import android.content.Context
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.EventRecord
import org.eurofurence.connavigator.R
import java.util.*

/**
 * Created by David on 11-5-2016.
 */
object Formatter {

    fun shareEvent(event: EventRecord, ctx: Context): String {
        return ctx.getString(R.string.event_check_out_url, event.title, createUrl("event", event.id))
    }

    fun shareDealer(dealer: DealerRecord, ctx: Context?): String {
        return ctx?.let {
            it.getString(R.string.dealer_check_out_dealer_url, dealer.displayName, createUrl("dealer", dealer.id))
        } ?: "Sorry, but I can't do that"
    }

    private fun createUrl(type: String, id: UUID): String {
        return "https://app.eurofurence.org/web/#/$type/$id"
    }
}