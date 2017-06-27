package org.eurofurence.connavigator.util.extensions

import android.graphics.Point
import android.graphics.Rect
import io.swagger.client.model.*
import org.eurofurence.connavigator.webapi.apiService

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