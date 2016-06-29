package org.eurofurence.connavigator.webapi

import android.content.Context
import com.android.volley.Network
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import io.swagger.client.ApiInvoker
import io.swagger.client.api.DefaultApi
import io.swagger.client.model.Endpoint
import org.eurofurence.connavigator.util.extensions.logd
import java.io.File

/**
 * The API services manage extended API functionality
 */
object apiService {
    val endpoint: String get() = api.basePath

    val api by lazy {
        DefaultApi().apply { basePath = "https://app.eurofurence.org/api"; }
    }

    fun formatUrl(url: String) =
            url.replace(Regex("\\Q{Endpoint}\\E|\\Q{EndpointUrl}\\E"), endpoint)

    /**
     * Initializes the API services
     */
    fun initialize(context: Context) {
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
    }
}