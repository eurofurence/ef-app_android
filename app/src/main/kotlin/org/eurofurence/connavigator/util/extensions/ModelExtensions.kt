package org.eurofurence.connavigator.util.extensions

import android.util.Log
import io.swagger.client.model.*
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.webapi.apiService

fun MapRecord.findMatchingEntries(x: Float, y: Float) = this.entries.filter { (x - it.x power 2F) + (y - it.y power 2F) < it.tapRadius power 2 }


val ImageRecord.url: String get() = "${apiService.apiPath}/Api/v2/Images/$id/Content"

fun PrivateMessageRecord.markAsRead() {
    if (AuthPreferences.isLoggedIn()) {
        Log.i("PMR", "Marking message ${this.id} as read")

        apiService.communications.addHeader("Authorization", AuthPreferences.asBearer())
        apiService.communications.apiV2CommunicationPrivateMessagesByMessageIdReadPost(this.id)
    } else {
        throw Exception("User is not logged in!")
    }
}

fun EventRecord.fullTitle(): String {
    val builder = StringBuilder(this.title.trim().removeSuffix("\n"))

    if (this.subTitle.isNotEmpty()) {
        builder.append(": ")
        builder.append(this.subTitle.trim().removeSuffix("\n"))
    }

    return builder.toString()
}

fun EventRecord.startTimeString(): String = startDateTimeUtc.jodatime().toString("HH:mm")
fun EventRecord.endTimeString(): String = endDateTimeUtc.jodatime().toString("HH:mm")
fun EventRecord.ownerString(): String = "Hosted by $panelHosts"

fun DealerRecord.getName() = if (this.displayName.isNotEmpty()) this.displayName else this.attendeeNickname