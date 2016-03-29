package org.eurofurence.connavigator.webapi

import android.content.Context
import io.swagger.client.ApiInvoker

/**
 * The API services manage extended API functionality
 */
object apiService {
    /**
     * Initializes the API services
     */
    fun initialize(context: Context) {
        // Sets up the API invoker, uses no cache
        ApiInvoker.initializeInstance();
    }
}