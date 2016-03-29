package org.eurofurence.connavigator.webapi

import io.swagger.client.ApiInvoker
import org.eurofurence.connavigator.net.volleyService

/**
 * The API services manage extended API functionality
 */
object apiService {
    /**
     * Initializes the API services, requires the API services to be initialized
     */
    fun initialize() {
        // Sets up the API invoker, uses a small cache and a few threads
        ApiInvoker.initializeInstance(volleyService.requestQueue.cache, volleyService.network, 2, null, 30);
    }
}