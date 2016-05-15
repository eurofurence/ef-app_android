package org.eurofurence.connavigator.util

import android.text.Html
import android.text.Spanned
import io.swagger.client.model.EventConferenceRoom
import io.swagger.client.model.EventEntry
import org.eurofurence.connavigator.database.Database
import org.joda.time.DateTime

/**
 * Created by David on 11-5-2016.
 */
object Formatter {
    val splitter_1 = "–"
    val splitter_2 = "—"

    /*
    Formats an event starting and ending times to conform to our standards
     */
    fun eventToTimes(eventEntry: EventEntry, database: Database): Spanned {
        val string = "<b>%s</b> from <b>%s</b> to <b>%s</b>".format(
                DateTime(database.eventConferenceDayDb.keyValues[eventEntry.conferenceDayId]!!.date).dayOfWeek().asText,
                eventEntry.startTime.subSequence(0, 5),
                eventEntry.endTime.subSequence(0, 5)
        )

        return Html.fromHtml(string)
    }

    /*
    Formats an event title accoding to our rules
     */
    fun eventTitle(eventEntry: EventEntry): Spanned {
        return formatHTML(eventEntry.title)
    }

    /*
        Formats the full room using an html element
     */
    fun roomFull(room: EventConferenceRoom): Spanned {
        return formatHTML(room.name)
    }

    /*
    Get's the room name that we assign
     */
    fun roomName(room: EventConferenceRoom): String {
        return split(room.name)[0]
    }

    fun eventOwner(eventEntry: EventEntry): Spanned {
        return Html.fromHtml("Hosted by <i>%s</i>".format(eventEntry.panelHosts))
    }

    /*
    Takes an input and formats it as html
     */
    private fun formatHTML(string: String): Spanned {
        val title_split = split(string)
        val html = when (title_split.count()) {
            2 -> {
                "%s: <i>%s</i>".format(
                        title_split[0].trim(),
                        title_split[1]
                )
            }
            else -> title_split[0]
        }

        return Html.fromHtml(html)
    }

    /*
    Splits according to our splitters
     */
    private fun split(string: String): List<String> {
        val title_split = string.split(splitter_1, splitter_2)
        return title_split
    }

}