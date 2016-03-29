package org.eurofurence.connavigator.net

import android.content.Context
import com.android.volley.Network
import com.android.volley.RequestQueue
import com.android.volley.toolbox.*
import com.android.volley.toolbox.ImageLoader.ImageCache
import org.eurofurence.connavigator.ui.LruImageCache

/**
 * Contains shared Volley singletons.
 */
object volleyService {
    /**
     * Shared Volley HTTP stack.
     */
    lateinit var stack: HttpStack

    /**
     * Shared Volley network.
     */
    lateinit var network: Network

    /**
     * Shared request queue.
     */
    lateinit var requestQueue: RequestQueue

    /**
     * Image loader, may be used with [NetworkImageView].
     */
    lateinit var imageLoader: ImageLoader

    /**
     * Cache for the images.
     */
    lateinit var imageCache: ImageCache

    /**
     * Initializes the Volley network services.
     * @param context The context to operate in
     */
    fun initialize(context: Context) {
        stack = HurlStack()
        network = BasicNetwork(stack)
        requestQueue = Volley.newRequestQueue(context, stack)
        imageCache = LruImageCache()
        imageLoader = ImageLoader(requestQueue, imageCache)
    }


}