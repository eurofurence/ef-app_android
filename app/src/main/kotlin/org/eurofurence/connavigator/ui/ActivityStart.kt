package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.jetbrains.anko.*

/**
 * Created by David on 28-4-2016.
 */
class ActivityStart : AppCompatActivity(), AnkoLogger {
    private lateinit var ui: StartUi
    private val database by lazy { Database(this) }

    private val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        if (it.booleans["success"]) {
            info { "Data update success" }
            ui.nextButton.text = "Got your data, now caching the images!"

            info { "Caching ${database.imageDb.items.count()} images" }
            database.imageDb.items.map { imageService.preload(it) }

            info { "Image caching done"}

            allowProceed()
        } else {
            ui.nextButton.text = "It seems like something went wrong during caching. We'll clear the database and let you try again"
            database.clear()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        info { "Starting start activity" }
        Analytics.screen(this, "Start")

        ui = StartUi()
        ui.setContentView(this)

        info { "Checking if database is filled" }

        if (database.versionDb.items.count() > 0 && !checkIfDifferentVersion()) {
            info { "Database has already been filled" }
            proceed()
        } else {
            info {"No data in database yet, dispatching update"}
            database.clear()
            dispatchUpdate()
            ui.nextButton.setOnClickListener { proceed() }
            updateReceiver.register()
        }

        Analytics.screen("Start")
    }

    /**
     * Checks the database for the last saved version. If it is not a bugfix release, we clear the database
     *
     * @return True if builds do not match
     */
    private fun checkIfDifferentVersion() = database.versionDb.items.first().split(".")[1] != BuildConfig.VERSION_NAME.split(".")[1]

    private fun proceed() {
        info { "Starting Root activity from Start activity" }
        startActivity<ActivityRoot>()
    }

    private fun allowProceed(){
        ui.nextButton.isEnabled = true
        ui.nextButton.text = "Cached data and images offline, we're good to go!"
        ui.downloadProgress.visibility = View.GONE
    }

    private fun dispatchUpdate() {
        info { "Asking for database update from API" }
        sendBroadcast(intentFor<UpdateIntentService>())
    }
}

class StartUi : AnkoComponent<ActivityStart> {
    lateinit var nextButton: Button
    lateinit var downloadProgress: ProgressBar

    override fun createView(ui: AnkoContext<ActivityStart>) = with(ui) {
        verticalLayout {
            backgroundResource = R.color.primary

            imageView { imageResource = R.drawable.logo }

            nextButton = button("We're just loading some data for you. Hang tight!") {
                isEnabled = false
            }

            downloadProgress = progressBar()
        }
    }

}
