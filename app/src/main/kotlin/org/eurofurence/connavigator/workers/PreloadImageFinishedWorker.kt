package org.eurofurence.connavigator.workers

import android.content.Context
import androidx.work.*
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class PreloadImageFinishedWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), AnkoLogger {
    override fun doWork(): Result {
        info {"Image preloading done"}
        BackgroundPreferences.loadingState = LoadingState.SUCCEEDED
        return Result.success()
    }

    companion object {
        const val TAG = "IMAGE_LOADING_DONE"

        fun create() = OneTimeWorkRequestBuilder<PreloadImageWorker>()
                .setInputMerger(ArrayCreatingInputMerger::class)
                .addTag(TAG)
                .build()
    }
}