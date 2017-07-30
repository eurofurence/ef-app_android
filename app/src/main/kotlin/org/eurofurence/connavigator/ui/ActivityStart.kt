package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.chibatching.kotpref.bulk
import com.google.firebase.perf.metrics.AddTrace
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.ResetReceiver
import org.eurofurence.connavigator.database.Db
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.jetbrains.anko.*

/**
 * Created by David on 28-4-2016.
 */
class ActivityStart : AppCompatActivity(), AnkoLogger, HasDb {
    override val db: Db
        get() = locateDb()

    private lateinit var ui: StartUi

    @AddTrace(name = "ActivityStart:UpdateIntentService", enabled = true)
    private val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        if (it.booleans["success"]) {
            info { "Data update success" }
            ui.nextButton.text = "Got your data, now caching the images!"

            info { "Caching ${db.images.items.count()} images" }
            db.images.items.map { imageService.preload(it) }

            info { "Image caching done" }

            allowProceed()
        } else {
            ui.nextButton.text = "It seems like something went wrong during caching. We'll clear the database and let you try again"
            ResetReceiver.fire(this)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        info { "Starting start activity" }
        Analytics.screen(this, "Start")

        ui = StartUi()
        ui.setContentView(this)

        info { "Checking if database is filled" }

        if (db.version !== null && !checkIfDifferentVersion()) {
            info { "Database has already been filled" }
            proceed()
        } else {
            info { "No data in database yet, dispatching update" }
            db.clear()
            dispatchUpdate()
            ui.nextButton.setOnClickListener { proceed() }
            updateReceiver.register()
        }

        Analytics.screen(this, "Start")
    }

    /**
     * Checks the database for the last saved version. If it is not a bugfix release, we clear the database
     *
     * @return True if the builds match
     */
    private fun checkIfDifferentVersion() = db.version!!.split(".")[1] != BuildConfig.VERSION_NAME.split(".")[1]

    private fun proceed() {
        info { "Writing current version to database" }
        db.version = BuildConfig.VERSION_NAME
        info { "Starting Root activity from Start activity" }
        startActivity<ActivityRoot>()
    }

    private fun allowProceed() {
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

            imageView { imageResource = R.drawable.logo }.lparams(
                    matchParent, (displayMetrics.heightPixels * 0.5).toInt()
            )

            nextButton = button("We're just loading some data for you. Hang tight!") {
                isEnabled = false
            }

            downloadProgress = progressBar()

            checkBox {
                text = "Let us anonymously track some analytical and performance data. This can be changed in your settings"
                isChecked = true
                setOnCheckedChangeListener { _, value ->
                    AnalyticsPreferences.bulk {
                        enabled = value
                        performanceTracking = value
                    }
                }
            }
        }
    }

}
