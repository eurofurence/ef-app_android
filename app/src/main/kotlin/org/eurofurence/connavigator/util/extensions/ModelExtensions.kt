package org.eurofurence.connavigator.util.extensions

import android.util.Log
import io.swagger.client.model.*
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.webapi.apiService
import java.util.*

fun MapRecord.findMatchingEntries(x: Float, y: Float) = this.entries.filter { (x - it.x power 2F) + (y - it.y power 2F) < it.tapRadius power 2 }


val ImageRecord.url: String get() = "${apiService.apiPath}/Api/v2/Images/$id/Content"

fun PrivateMessageRecord.markAsRead(): Promise<Date, java.lang.Exception> {
    return task {
        if (AuthPreferences.isLoggedIn()) {
            Log.i("PMR", "Marking message ${this.id} as read")

            apiService.communications.addHeader("Authorization", AuthPreferences.asBearer())
            apiService.communications.apiV2CommunicationPrivateMessagesByMessageIdReadPost(this.id)
        } else {
            throw Exception("User is not logged in!")
        }
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

fun DealerRecord.getName() = if(this.displayName.isNotEmpty()) this.displayName else this.attendeeNickname