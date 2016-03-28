package org.eurofurence.connavigator.extensions

import android.content.Context
import android.content.Intent
import io.swagger.client.model.*
import org.eurofurence.connavigator.util.Bridge
import org.eurofurence.connavigator.util.Registered
import org.eurofurence.connavigator.webapi.QueryService
import java.util.*


/**
 * Queries the endpoint, be sure to register a receiver ([createEndpointReceiver]).
 */
fun Context.queryEndpoint(scope: String = "unscoped") {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "endpoint"
    request["scope"] = scope
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createEndpointReceiver(scope: String = "unscoped", handler: (Endpoint) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "endpoint", Endpoint::class.java, scope, Bridge.fromSingle(handler))

/**
 * Queries the conference days, be sure to register a receiver ([createEventConferenceDayReceiver]).
 */
fun Context.queryEventConferenceDay(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "eventConferenceDay"
    request["scope"] = scope
    request.objects["since"] = since

    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createEventConferenceDayReceiver(scope: String = "unscoped", handler: (List<EventConferenceDay>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "eventConferenceDay", EventConferenceDay::class.java, scope, Bridge.fromSome(handler))

/**
 * Queries the conference rooms, be sure to register a receiver ([createEventConferenceRoomReceiver]).
 */
fun Context.queryEventConferenceRoom(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "eventConferenceRoom"
    request["scope"] = scope
    request.objects["since"] = since
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createEventConferenceRoomReceiver(scope: String = "unscoped", handler: (List<EventConferenceRoom>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "eventConferenceRoom", EventConferenceRoom::class.java, scope, Bridge.fromSome(handler))

/**
 * Queries the conference tracks, be sure to register a receiver ([createEventConferenceTrackReceiver]).
 */
fun Context.queryEventConferenceTrack(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "eventConferenceTrack"
    request["scope"] = scope
    request.objects["since"] = since
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createEventConferenceTrackReceiver(scope: String = "unscoped", handler: (List<EventConferenceTrack>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "eventConferenceTrack", EventConferenceTrack::class.java, scope, Bridge.fromSome(handler))

/**
 * Queries the events, be sure to register a receiver ([createEventEntryReceiver]).
 */
fun Context.queryEventEntry(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "eventEntry"
    request["scope"] = scope
    request.objects["since"] = since
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createEventEntryReceiver(scope: String = "unscoped", handler: (List<EventEntry>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "eventEntry", EventEntry::class.java, scope, Bridge.fromSome(handler))

/**
 * Queries images, be sure to register a receiver ([createImageReceiver]).
 */
fun Context.queryImage(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "image"
    request["scope"] = scope
    request.objects["since"] = since
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createImageReceiver(scope: String = "unscoped", handler: (List<Image>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "image", Image::class.java, scope, Bridge.fromSome(handler))

/**
 * Queries info, be sure to register a receiver ([createInfoReceiver]).
 */
fun Context.queryInfo(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "info"
    request["scope"] = scope
    request.objects["since"] = since
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createInfoReceiver(scope: String = "unscoped", handler: (List<Info>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "info", Info::class.java, scope, Bridge.fromSome(handler))

/**
 * Queries info groups, be sure to register a receiver ([createInfoGroupReceiver]).
 */
fun Context.queryInfoGroup(scope: String = "unscoped", since: Date? = null) {
    val request = Intent(this, QueryService::class.java)
    request["method"] = "infoGroup"
    request["scope"] = scope
    request.objects["since"] = since
    this.startService(request)
}

/**
 * Creates a receiver, it will **not** be registered right away.
 */
fun Context.createInfoGroupReceiver(scope: String = "unscoped", handler: (List<InfoGroup>) -> Unit): Registered =
        QueryService.ResponseReceiver(this, "infoGroup", InfoGroup::class.java, scope, Bridge.fromSome(handler))