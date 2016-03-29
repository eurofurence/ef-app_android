package org.eurofurence.connavigator.ui

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader
import org.eurofurence.connavigator.util.memorySmall

/**
 * Image cache using Androids LRU cache.
 * @param maxCacheSizeInBytes The maximum cache size in bytes, defaults to a fraction of the total memory
 */
class LruImageCache(val maxCacheSizeInBytes: Int = memorySmall) : LruCache<String, Bitmap>(maxCacheSizeInBytes), ImageLoader.ImageCache {
    override fun sizeOf(key: String, value: Bitmap): Int = value.rowBytes * value.height / 1024

    override fun getBitmap(url: String): Bitmap? = get(url)

    override fun putBitmap(url: String, bitmap: Bitmap?) {
        put(url, bitmap)
    }
}