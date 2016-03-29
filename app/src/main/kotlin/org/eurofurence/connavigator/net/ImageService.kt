package org.eurofurence.connavigator.net

import android.content.Context
import android.widget.ImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageSize
import io.swagger.client.model.Image

/**
 * Provides methods for obtaining images from web and caching them.
 */
object imageService {
    /**
     * The image loader used to load the images.
     */
    lateinit var imageLoader: ImageLoader

    /**
     * Initializes the image services.
     */
    fun initialize(context: Context) {

        // Enable caching in default display options
        val defaultOptions = DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build()

        // Setup in loader
        val config = ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build()

        // Get and initialize the loader
        imageLoader = ImageLoader.getInstance()
        imageLoader.init(config)
    }

    /**
     * Loads the image at the URL and displays it in the image view.
     */
    fun load(image: Image, imageView: ImageView) = imageLoader.displayImage(image.url, imageView, ImageSize(image.width, image.height))

}