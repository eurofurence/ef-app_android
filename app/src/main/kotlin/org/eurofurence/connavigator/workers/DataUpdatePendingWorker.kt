package org.eurofurence.connavigator.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState

class DataUpdatePendingWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        if(BackgroundPreferences.loadingState != LoadingState.UNINITIALIZED)
            BackgroundPreferences.loadingState = LoadingState.PENDING

        return Result.success()
    }

    companion object {
        val TAG = "UPDATE_PENDING"

        fun create() = OneTimeWorkRequestBuilder<DataUpdatePendingWorker>()
                .addTag(TAG)
                .build()
    }
}