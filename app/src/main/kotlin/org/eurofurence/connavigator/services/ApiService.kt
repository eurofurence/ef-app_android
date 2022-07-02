

package org.eurofurence.connavigator.services

import android.content.Context
import com.android.volley.Network
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import io.swagger.client.ApiInvoker
import io.swagger.client.api.*
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.util.extensions.catchHandle
import org.eurofurence.connavigator.dropins.AnkoLogger

import java.io.File

/**
 * The API services manage extended API functionality
 */
object apiService : AnkoLogger {
    var apiPath: String = "${BuildConfig.API_BASE_URL}/${BuildConfig.CONVENTION_IDENTIFIER}"

    val announcements by lazy { AnnouncementsApi().apply { basePath = apiPath } }

    val communications by lazy { CommunicationApi().apply { basePath = apiPath } }

    val dealers by lazy { DealersApi().apply { basePath = apiPath } }

    val days by lazy { EventConferenceDaysApi().apply { basePath = apiPath } }

    val rooms by lazy { EventConferenceRoomsApi().apply { basePath = apiPath } }

    val tracks by lazy { EventConferenceTracksApi().apply { basePath = apiPath } }

    val feedbacks by lazy { EventFeedbackApi().apply { basePath = apiPath } }

    val events by lazy { EventsApi().apply { basePath = apiPath } }

    val images by lazy { ImagesApi().apply { basePath = apiPath } }

    val knowledgeEntries by lazy { KnowledgeEntriesApi().apply { basePath = apiPath } }

    val knowledgeGroups by lazy { KnowledgeGroupsApi().apply { basePath = apiPath } }

    val maps by lazy { MapsApi().apply { basePath = apiPath } }

    val pushNotifications by lazy { PushNotificationsApi().apply { basePath = apiPath } }

    val sync by lazy { SyncApi().apply { basePath = apiPath } }

    val tokens by lazy { TokensApi().apply { basePath = apiPath } }

    val fursuits by lazy { FursuitsApi().apply { basePath = apiPath } }

    /**
     * Initializes the API services
     */
    fun initialize(context: Context) {
        try {
            debug { "Initializing" }

            // Create the cache
            val cache = DiskBasedCache(File(context.cacheDir, "volley"))

            //Create the network
            val originalNetwork = BasicNetwork(HurlStack())
            val network = Network {
                debug { "Performing request: ${it.url}" }
                originalNetwork.performRequest(it)
            }

            // Sets up the API invoker
            ApiInvoker.initializeInstance(cache, network, 2, null, 60)
            debug { "Done initializing, ${ApiInvoker.getInstance()}" }
        } catch (t: Throwable) {
            debug { "Initialization failed with ${t.message}" }
        }
    }
}