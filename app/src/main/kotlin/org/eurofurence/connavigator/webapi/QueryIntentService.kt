package org.eurofurence.connavigator.webapi

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
import org.eurofurence.connavigator.util.booleans
import org.eurofurence.connavigator.util.get
import org.eurofurence.connavigator.util.objects
import org.eurofurence.connavigator.util.set
import org.eurofurence.connavigator.util.Bridge
import org.eurofurence.connavigator.util.Registered
import java.util.*


/**
 * The query service handles intents of a certain type. It executes a web query in the background and feed the result
 * back in another broadcast event. The core methods of operation are abstracted as accompanying methods. See for
 * example queryEndpoint and the corresponding createEndpointReceiver method in the query extensions. To start listening with a receiver,
 * call [Registered.register].
 */
class QueryIntentService(val api: DefaultApi = DefaultApi()) : IntentService("QueryIntentService") {
    companion object {
        /**
         * The intent used as a return will be classified by this name
         */
        val QUERY_ACTION = "org.eurofurence.connavigator.webapi.QueryService.QUERY_ACTION"

        /**
         * Schema of the data in the reply intent
         */
        val QUERY_SCHEME = "efapi"
    }

    /**
     * Handles the incoming request, the method specified in the extras will be executed and published
     */
    override fun onHandleIntent(p: Intent) {
        val method = p["method"]
        val scope = p["scope"]
        val since = p.objects["since", Date::class.java]

        /**
         * Publishes the value, iff not null, via the defined protocol
         */
        fun publish(value: Any?) {
            if (value != null) {
                // Make a reply
                val reply = Intent(QUERY_ACTION, Uri.fromParts(QUERY_SCHEME, "$method{$scope}", "$since"))

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
    class ResponseReceiver<T>(val context: Context, val method: String, val t: Class<T>, val scope: String, val handler: Bridge<T>) : BroadcastReceiver(), Registered {
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
