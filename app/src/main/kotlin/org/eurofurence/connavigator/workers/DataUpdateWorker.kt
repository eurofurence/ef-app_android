package org.eurofurence.connavigator.workers

import android.content.Context
import androidx.work.*
import com.pawegio.kandroid.runOnUiThread
import io.swagger.client.model.AggregatedDeltaResponse
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.RootDb
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.services.ImageService
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.services.apiService
import org.eurofurence.connavigator.util.v2.convert
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import org.jetbrains.anko.warn

class DataUpdateWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), AnkoLogger {
    val db: Db = RootDb(context)

    companion object {
        const val SHOW_TOAST_ON_COMPLETION = "SHOW_TOAST"
        const val PRELOAD_CHANGED_IMAGES = "PRELOAD_CHANGED_IMAGES"
        const val TAG = "update_data"

        private val constraints
            get() = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

        // create work and return it
        fun create(showToastOnCompletion: Boolean, preloadChangedImages: Boolean) =
                OneTimeWorkRequestBuilder<DataUpdateWorker>()
                        .setConstraints(constraints)
                        .addTag(TAG)
                        .setInputData(workDataOf(SHOW_TOAST_ON_COMPLETION to showToastOnCompletion, PRELOAD_CHANGED_IMAGES to preloadChangedImages))
                        .build()

        // creates work and then executes it in the workmanager
        fun execute(context: Context, showToastOnCompletion: Boolean = false, preloadChangedImages: Boolean = true) =
                WorkManager.getInstance(context)
                        .beginUniqueWork(TAG, ExistingWorkPolicy.REPLACE, DataUpdatePendingWorker.create())
                        .then(create(showToastOnCompletion, preloadChangedImages))
                        .enqueue()
    }

    override fun doWork(): Result {
        val showToastOnCompletion = inputData.getBoolean(SHOW_TOAST_ON_COMPLETION, false)
        val preloadChangedImages = inputData.getBoolean(PRELOAD_CHANGED_IMAGES, true)
        val date = db.date

        // Fetch data
        val sync = try {
            BackgroundPreferences.loadingState = LoadingState.LOADING_DATA

            info { "Updating remote preferences" }
            RemotePreferences.update()

            info { "Retrieving sync since $date" }
            // Get sync from server
            apiService.sync.apiSyncGet(date).apply {
                if (this.conventionIdentifier != BuildConfig.CONVENTION_IDENTIFIER) {
                    throw Exception("Convention Identifier mismatch\n\nExpected: ${BuildConfig.CONVENTION_IDENTIFIER}\nReceived: ${this.conventionIdentifier}")
                }
            }
        } catch (ex: Throwable) {
            warn("Error occured during update", ex)
            BackgroundPreferences.loadingState = LoadingState.FAILED
            return Result.failure()
        }

        // Apply sync
        applySync(sync, showToastOnCompletion)

        // Fetch private messages
        FetchPrivateMessageWorker.execute(applicationContext)

        // Fetch images
        if (preloadChangedImages && sync.images.changedEntities.isNotEmpty()) {
            loadImages(sync)
        }

        return Result.success()
    }

    private fun loadImages(sync: AggregatedDeltaResponse) {
        BackgroundPreferences.loadingState = LoadingState.LOADING_IMAGES
        val workList = sync.images.changedEntities.map { PreloadImageWorker.create(it) }
        val workFinished = PreloadImageFinishedWorker.create()

        WorkManager.getInstance(applicationContext)
                .beginWith(workList)
                .then(workFinished)
                .enqueue()
    }

    private fun applySync(sync: AggregatedDeltaResponse, showToastOnCompletion: Boolean) {
        // Apply sync
        db.announcements.apply(sync.announcements.convert())
        db.dealers.apply(sync.dealers.convert())
        db.days.apply(sync.eventConferenceDays.convert())
        db.rooms.apply(sync.eventConferenceRooms.convert())
        db.tracks.apply(sync.eventConferenceTracks.convert())
        db.events.apply(sync.events.convert())
        db.images.apply(sync.images.convert())
        db.knowledgeEntries.apply(sync.knowledgeEntries.convert())
        db.knowledgeGroups.apply(sync.knowledgeGroups.convert())
        db.maps.apply(sync.maps.convert())
        db.state = sync.state;


        // Reconcile events with favorites
        db.faves = db.events.items.map { it.id }
                .toSet()
                .let { eventsIds ->
                    db.faves.filter { it in eventsIds }
                }

        /*
            All images that have been deleted or changed can get removed from cache to free
            space. We need to base the query on existing (local) records so the ImageService
            can access the cache by the "old" urls.
          */
        val invalidatedImages = db.images.items.filter {
            sync.images.deletedEntities.contains(it.id)
                    || sync.images.changedEntities.any { rec -> rec.id == it.id }
        }

        for (image in invalidatedImages)
            ImageService.removeFromCache(image)

        // Store new time
        db.date = sync.currentDateTimeUtc


        info { "Synced, current sync time is $db.date" }

        if (showToastOnCompletion) {
            runOnUiThread {
                applicationContext.longToast("Successfully updated all content.")
            }
        }

        BackgroundPreferences.hasLoadedOnce = true
        BackgroundPreferences.loadingState = LoadingState.SUCCEEDED
    }
}