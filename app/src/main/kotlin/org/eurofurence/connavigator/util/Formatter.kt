package org.eurofurence.connavigator.util

import android.text.Html
import android.text.Spanned
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.EventConferenceRoomRecord
import io.swagger.client.model.EventRecord
import io.swagger.client.model.KnowledgeEntryRecord
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.util.v2.get
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*

/**
 * Created by David on 11-5-2016.
 */
object Formatter {
    val splitter_1 = "–"
    val splitter_2 = "—"

    fun shortTime(string: String, date: DateTime? = null) =
            if (date == null) {
                string.subSequence(0, 5).toString()
            } else {
                string.subSequence(0, 5).toString() + " ${date.toString("EE")}"
            }

    /*
        Formats the full room using an html element
     */
    fun roomFull(room: EventConferenceRoomRecord): Spanned {
        return formatHTML(room.name)
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

    fun shareEvent(event: EventRecord): String {
        return "Check out ${event.title}!\n${createUrl("event", event.id)}"
    }

    fun shareDealer(dealer: DealerRecord): String {
        return "Check out ${dealer.displayName} at the dealers den!\n${createUrl("dealer", dealer.id)}"
    }

    fun shareInfo(knowledgeEntry: KnowledgeEntryRecord): String {
        return "Hey, this might help: ${knowledgeEntry.title}!\n${createUrl("info", knowledgeEntry.id)}"
    }

    fun createUrl(type: String, id: UUID): String {
        return "https://app.eurofurence.org/web/#/$type/${id.toString()}"
    }
}