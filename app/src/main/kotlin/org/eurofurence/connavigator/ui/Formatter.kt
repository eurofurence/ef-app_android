package org.eurofurence.connavigator.ui

import android.text.Html
import android.text.Spanned
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.joda.time.DateTime
import java.util.regex.Pattern

/**
 * Created by David on 11-5-2016.
 */
object Formatter {
    val timePattern = Pattern.compile("(\\d+:\\d+):(\\d+)")

    fun eventToTimes(eventEntry: EventEntry, database: Database): Spanned {
        val string =  "<b>%s</b> from <b>%s</b> to <b>%s</b>".format(
                DateTime(database.eventConferenceDayDb.keyValues[eventEntry.conferenceDayId]!!.date).dayOfWeek().asText,
                eventEntry.startTime.subSequence(0, 5),
                eventEntry.endTime.subSequence(0, 5)
        )

        return Html.fromHtml(string)
    }
}