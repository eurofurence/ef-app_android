package org.eurofurence.connavigator.webapi

import android.content.Context
import com.android.volley.Network
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.swagger.client.ApiInvoker
import io.swagger.client.api.AnnouncementsApi
import io.swagger.client.api.CommunicationApi
import io.swagger.client.api.DealersApi
import io.swagger.client.api.EventConferenceDaysApi
import io.swagger.client.api.EventConferenceRoomsApi
import io.swagger.client.api.EventConferenceTracksApi
import io.swagger.client.api.EventFeedbackApi
import io.swagger.client.api.EventsApi
import io.swagger.client.api.FursuitsApi
import io.swagger.client.api.ImagesApi
import io.swagger.client.api.KnowledgeEntriesApi
import io.swagger.client.api.KnowledgeGroupsApi
import io.swagger.client.api.MapsApi
import io.swagger.client.api.PushNotificationsApi
import io.swagger.client.api.SyncApi
import io.swagger.client.api.TokensApi
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.util.extensions.catchHandle
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.loge
import java.io.File

/**
 * The API services manage extended API functionality
 */
object apiService {
    var apiPath = RemotePreferences.apiBaseUrl

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
        {
            apiPath = RemotePreferences.apiBaseUrl

            RemotePreferences.observer
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { apiPath = it.apiBaseUrl }

            logd("API") { "Initializing" }

            // Create the cache
            val cache = DiskBasedCache(File(context.cacheDir, "volley"))

            //Create the network
            val originalNetwork = BasicNetwork(HurlStack())
            val network = Network {
                logd("NET") { "Performing request: ${it.url}" }
                originalNetwork.performRequest(it)
            }

            // Sets up the API invoker
            ApiInvoker.initializeInstance(cache, network, 2, null, 60)
            logd("API") { "Done initializing, ${ApiInvoker.getInstance()}" }
        } catchHandle { t: Throwable ->
            loge("API") { "Initialization failed with ${t.message}" }
        }
    }
}