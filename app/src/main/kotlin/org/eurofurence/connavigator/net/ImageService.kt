package org.eurofurence.connavigator.net

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.assist.ImageSize
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.nostra13.universalimageloader.utils.MemoryCacheUtils
import io.swagger.client.model.ImageRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.url
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
                .imageScaleType(ImageScaleType.NONE)
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
    fun load(image: ImageRecord?, imageView: ImageView, showHide: Boolean = true) {
        // Load image if not null
        if (image != null)
            imageLoader.displayImage(image.url, imageView, ImageSize(image.width, image.height))
        else
            imageLoader.displayImage("", imageView)

        // If visibility modification desired, perform it
        if (showHide)
            imageView.visibility = if (image == null) View.GONE else View.VISIBLE
    }

    /**
     * Preloads an image too memory
     */
    fun preload(image: ImageRecord) =
            imageLoader.loadImage(image.url, ImageSize(image.width, image.height), object : SimpleImageLoadingListener() {
                override fun onLoadingComplete(imageUri: String, view: View?, loadedImage: Bitmap) {

                    if (imageUri.contains("1f70fd04-39e5-11e6-bae0-066e23d209cf")) {
                        println("${image.width} x ${image.height}")
                        println("${loadedImage.width} x ${loadedImage.height}")
                    }
                }
            })

    fun getBitmap(image: ImageRecord): Bitmap =
            imageLoader.loadImageSync(image.url, ImageSize(image.width, image.height))

    fun clear() {
        logd { "Clearing image cache" }
        imageLoader.clearDiskCache()
        imageLoader.clearMemoryCache()
    }

    /**
     * Reload an image
     */
    fun recache(image: ImageRecord) {
        val imageFile = imageLoader.diskCache.get(image.url)

        if (imageFile.exists()) {
            logd { "Deleting cached image" }
            imageFile.delete()
            preload(image)
        }

        MemoryCacheUtils.removeFromCache(image.url, imageLoader.memoryCache)
    }
}