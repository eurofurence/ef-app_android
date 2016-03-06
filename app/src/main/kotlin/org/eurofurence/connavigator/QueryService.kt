package org.eurofurence.connavigator

import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.PatternMatcher
import android.support.v4.content.LocalBroadcastManager
import io.swagger.client.JsonUtil
import io.swagger.client.api.DefaultApi
import io.swagger.client.model.*
import java.util.*
import org.eurofurence.connavigator.util.*

/**
 * The intent used as a return will be classified by this name
 */
val QUERY_ACTION = "org.eurofurence.connavigator.QueryService.QUERY_ACTION"

/**
 * Schema of the data in the reply intent
 */
val QUERY_SCHEME = "efapi"

/**
 * The query service handles intents of a certain type. It executes a web query in the background and feed the result
 * back in another broadcast event. The core methods of operation are abstracted as accompanying methods. See for
 * example [queryEndpoint] and the corresponding [createEndpointReceiver] method. To start listening with a receiver,
 * call [Registered.register].
 */
class QueryService(val api: DefaultApi = DefaultApi()) : IntentService("QueryIntentService") {
    /**
     * Handles the incoming request, the method specified in the extras will be executed and published
     */
    override fun onHandleIntent(p: Intent) {
        val method = p["method"]
        val scope = p["scope"]
        val since = p.objects["since"] as Date?

        /**
         * Publishes the value, iff not null, via the defined protocol
         */
        fun publish(value: Any?) {
            if (value != null) {
                // Make a reply
                val reply = Intent(QUERY_ACTION, Uri.fromParts(QUERY_SCHEME, "$method{$scope}", "$since"))
                println(reply.data)
                // Fill it, memorize if input was a list
                reply["value"] = JsonUtil.serialize(value)
                reply.booleans["list"] = value is List<*>

                // Broadcast
                LocalBroadcastManager.getInstance(this).sendBroadcast(reply)
            }
        }

        // Map the string to a method invocation
        publish(when (method) {
            "endpoint" -> api.endpointGet()
            "eventConferenceDay" -> api.eventConferenceDayGet(since)
            "eventConferenceRoom" -> api.eventConferenceRoomGet(since)
            "eventConferenceTrack" -> api.eventConferenceTrackGet(since)
            "eventEntry" -> api.eventEntryGet(since)
            "image" -> api.imageGet(since)
            "info" -> api.infoGet(since)
            "infoGroup" -> api.infoGroupGet(since)
            else -> null
        })
    }


    /**
     * Filters the intents based on the [method]. Translates them using reflection and using [t] and [JsonUtil]. An
     * appropriate method in [handler] will be called. If a [scope] is provided, receivers take only responses on that
     * channel.
     */
    class ResponseReceiver<T>(val context: Context, val method: String, val t: Class<T>, val scope: String, val handler: PatchFunction<T>) : BroadcastReceiver(), Registered {
        /**
         * Tracks if the receiver is registered
         */
        var registred = false

        override fun register(): Boolean {
            if (registred)
                return false

            // Filter for the query action, scheme, and method
            val filter = IntentFilter(QUERY_ACTION)
            filter.addDataScheme(QUERY_SCHEME)
            filter.addDataSchemeSpecificPart("$method{$scope}", PatternMatcher.PATTERN_LITERAL)

            LocalBroadcastManager.getInstance(context).registerReceiver(this, filter)
            registred = true
            return true
        }

        override fun unregister(): Boolean {
            if (!registred)
                return false

            LocalBroadcastManager.getInstance(context).unregisterReceiver(this)
            registred = false
            return true
        }

        override fun onReceive(context: Context, p: Intent) {
            // Deserialize appropriately
            if (p.booleans["list"])
                handler(JsonUtil.deserializeToList<List<T>>(p["value"], t))
            else
                handler(JsonUtil.deserializeToObject<T>(p["value"], t))
        }
    }
}


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
        QueryService.ResponseReceiver(this, "endpoint", Endpoint::class.java, scope, patchFromSingle(handler))

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
        QueryService.ResponseReceiver(this, "eventConferenceDay", EventConferenceDay::class.java, scope, patchFromSome(handler))

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
        QueryService.ResponseReceiver(this, "eventConferenceRoom", EventConferenceRoom::class.java, scope, patchFromSome(handler))

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
        QueryService.ResponseReceiver(this, "eventConferenceTrack", EventConferenceTrack::class.java, scope, patchFromSome(handler))

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
        QueryService.ResponseReceiver(this, "eventEntry", EventEntry::class.java, scope, patchFromSome(handler))

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
        QueryService.ResponseReceiver(this, "image", Image::class.java, scope, patchFromSome(handler))

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
        QueryService.ResponseReceiver(this, "info", Info::class.java, scope, patchFromSome(handler))

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
        QueryService.ResponseReceiver(this, "infoGroup", InfoGroup::class.java, scope, patchFromSome(handler))