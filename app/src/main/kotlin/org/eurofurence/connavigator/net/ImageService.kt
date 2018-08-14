package org.eurofurence.connavigator.net

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.assist.ImageSize
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.nostra13.universalimageloader.utils.MemoryCacheUtils
import io.swagger.client.model.ImageRecord
import nl.komponents.kovenant.Deferred
import nl.komponents.kovenant.deferred
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.url
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

/**
 * Provides methods for obtaining images from web and caching them.
 */
object imageService : AnkoLogger {
    /**
     * Image loading progress listener that does nothing.
     */
    private object NopListener : ImageLoadingProgressListener {
        override fun onProgressUpdate(imageUri: String?, view: View?, current: Int, total: Int) {
        }
    }

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
    fun load(image: ImageRecord?, imageView: ImageView, showHide: Boolean = true) = deferred<Bitmap?, Exception>().also { promise ->
        // Load image if not null
        if (image != null)
            imageLoader.displayImage(image.url, ImageViewAware(imageView), null,
                    ImageSize(image.width ?: 0, image.height ?: 0), resolveOnCompletion(promise), NopListener)
        else
            imageLoader.displayImage("", imageView, resolveOnCompletion(promise))

        // If visibility modification desired, perform it
        if (showHide)
            imageView.visibility = if (image == null) View.GONE else View.VISIBLE
    }.promise

    /**
     * Preloads an image too memory
     */
    fun preload(image: ImageRecord) = deferred<Bitmap?, Exception>().also { promise ->
        imageLoader.loadImage(image.url, ImageSize(image.width ?: 0, image.height ?: 0), resolveOnCompletion(promise))
    }.promise

    private fun resolveOnCompletion(promise: Deferred<Bitmap?, Exception>) =
            object : SimpleImageLoadingListener() {
                override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason) {
                    promise.reject(RuntimeException(failReason.type.toString(), failReason.cause))
                }

                override fun onLoadingComplete(imageUri: String, view: View?, loadedImage: Bitmap?) {
                    promise.resolve(loadedImage)
                }
            }


    fun clear() {
        debug { "Clearing image cache" }
        imageLoader.clearDiskCache()
        imageLoader.clearMemoryCache()
    }

    /**
     * Reload an image
     */
    fun recache(image: ImageRecord) {
        val imageFile = imageLoader.diskCache.get(image.url)

        if (imageFile.exists()) {
            debug { "Deleting cached image" }
            imageFile.delete()
            preload(image)
        }

        MemoryCacheUtils.removeFromCache(image.url, imageLoader.memoryCache)
    }
}