package org.eurofurence.connavigator.workers

import android.content.Context
import androidx.work.*
import io.swagger.client.model.ImageRecord
import nl.komponents.kovenant.toVoid
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.services.ImageService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.warn
import java.lang.Exception
import java.util.*

class PreloadImageWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters), AnkoLogger {
    val db = RootDb(applicationContext)

    companion object {
        const val IMAGE_ID = "IMAGE_ID"
        const val TAG = "preload_images"

        private val constraints
            get() = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

        fun create(image: ImageRecord) =
                OneTimeWorkRequestBuilder<PreloadImageWorker>()
                        .setConstraints(constraints)
                        .addTag(TAG)
                        .setInputData(workDataOf(IMAGE_ID to image.id.toString()))
                        .build()
    }

    override fun doWork(): Result {
        try {
            val imageIdString = inputData.getString(IMAGE_ID)

            val imageId = UUID.fromString(imageIdString) ?: return Result.success()
            val image = db.images.entries[imageId] ?: return Result.success()

            info { "Preloading image: ${image.id.toString()}" }

            ImageService.preload(image).toVoid().get()

            return Result.success()
        } catch (ex: Exception) {
            warn ("Error occured during image fetching", ex)

            // We still return a success, because we don't want weird states
            return Result.success()
        }
    }
}