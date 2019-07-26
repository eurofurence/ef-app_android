package org.eurofurence.connavigator.workers

import android.content.Context
import androidx.work.*
import org.eurofurence.connavigator.services.PMService
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class FetchPrivateMessageWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams), AnkoLogger {
    companion object {
        const val NAME = "FETCH_PMS"

        private val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        fun create() = OneTimeWorkRequestBuilder<FetchPrivateMessageWorker>()
                .setConstraints(constraints)
                .build()

        fun execute(context: Context) = WorkManager.getInstance(context)
                .enqueueUniqueWork(NAME, ExistingWorkPolicy.REPLACE, create())
    }

    override fun doWork(): Result {
        info { "Starting worker to fetch PM" }

        PMService.fetch()

        return Result.success()
    }
}