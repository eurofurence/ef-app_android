package org.eurofurence.connavigator.net

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.assist.ImageSize
import io.swagger.client.model.Image
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.webapi.apiService

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
                .showImageForEmptyUri(R.drawable.placeholder_event)// TODO: Maybe add specific placeholders
                .showImageOnFail(R.drawable.placeholder_event)
                .imageScaleType(ImageScaleType.EXACTLY)
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
    fun load(image: Image?, imageView: ImageView, showHide: Boolean = true) {
        // Load image if not null
        if (image != null)
            imageLoader.displayImage(apiService.formatUrl(image.url), imageView, ImageSize(image.width, image.height))
        else
            imageLoader.displayImage("", imageView)

        // If visibility modification desired, perform it
        if (showHide)
            imageView.visibility = if (image == null) View.GONE else View.VISIBLE
    }

    /**
     * Resizes the image view to fit the image, hiding it if desired and resetting to zero if not
     */
    fun resizeFor(image: Image?, imageView: ImageView) {
        // TODO
    }


}