package org.eurofurence.connavigator.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState

class PreloadImageFinishedWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        BackgroundPreferences.loadingState = LoadingState.SUCCEEDED
        return Result.success()
    }

    companion object {
        const val TAG = "IMAGE_LOADING_DONE"
        fun create() = OneTimeWorkRequestBuilder<PreloadImageWorker>()
                .addTag(TAG)
                .build()
    }
}