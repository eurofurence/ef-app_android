package org.eurofurence.connavigator.util.extensions

import android.graphics.Point
import android.graphics.Rect
import android.util.Log
import io.swagger.client.model.EventRecord
import io.swagger.client.model.ImageRecord
import io.swagger.client.model.MapEntryRecord
import io.swagger.client.model.PrivateMessageRecord
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.webapi.apiService
import java.util.*

/**
 * Gets the fixed coordinates of a map entity, fitted to a map
 */
fun MapEntryRecord.asRelatedCoordinates(image: ImageRecord) =
        Point(((this.relativeX.toFloat() / 100) * image.width).toInt(), ((this.relativeY.toFloat() / 100) * image.height).toInt())

fun MapEntryRecord.asRectangle(image: ImageRecord): Rect {
    val point = this.asRelatedCoordinates(image)
    val jitter = ((this.relativeTapRadius.toFloat() / 100) * image.width).toInt()

    return Rect(point.x - jitter, point.y - jitter, point.x + jitter, point.y + jitter)
}

val ImageRecord.url: String get() = "${apiService.apiPath}/Api/v2/Images/$id/Content"

fun PrivateMessageRecord.markAsRead(): Promise<Date, java.lang.Exception> {
    return task {
        if (AuthPreferences.isLoggedIn()) {
            Log.i("PMR", "Marking message ${this.id} as read")

            apiService.communications.addHeader("Authorization", AuthPreferences.asBearer())
            apiService.communications.apiV2CommunicationPrivateMessagesByMessageIdReadPost(this.id)
        } else{
            throw Exception("User is not logged in!")
        }
    }
}

fun EventRecord.fullTitle(): String {
    val builder = StringBuilder(this.title.trim().removeSuffix("\n"))

    if(this.subTitle.isNotEmpty()) {
        builder.append(": ")
        builder.append(this.subTitle.trim().removeSuffix("\n"))
    }

    return builder.toString()
}