package org.eurofurence.connavigator.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.get
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.webapi.apiService

/**
 * Created by David on 28-4-2016.
 */
class ActivityStart : AppCompatActivity() {
    val buttonStart by view(Button::class.java)
    val textHelp by view(TextView::class.java)

    val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        if (it.booleans["success"]) {
            val database = Database(this)

            for (map in database.mapEntityDb.items) {
                logd { "Preloading map ${map.description}" }
                val image = database.imageDb[map.imageId]!!
                imageService.imageLoader.loadImage(apiService.formatUrl(image.url), SimpleImageLoadingListener())
            }

            for (image in database.imageDb.items) {
                logd { "Preloading image ${image.title}" }
                imageService.imageLoader.loadImage(apiService.formatUrl(database.imageDb[image.id]!!.url), SimpleImageLoadingListener())
            }

            textHelp.text = "There, all done!"
            buttonStart.text = "Get Started!"
            buttonStart.visibility = View.VISIBLE

            buttonStart.setOnClickListener { startRootActivity() }
        } else {
            textHelp.text = "Failed to successfully get data. Are you connected to the internet?"
            buttonStart.visibility = View.VISIBLE
            buttonStart.text = "Retry"
            buttonStart.setOnClickListener { UpdateIntentService.dispatchUpdate(this) }
        }
    }
    val database by lazy { Database(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        Analytics.screen("Start")

        // Version Check

        if (database.versionDb.items.count() > 0 && database.versionDb.items.first().split(".")[1] != BuildConfig.VERSION_NAME.split(".")[1]) {
            // We're on an old version (v1.8 instead of v1.9 So we empty the database and exit
            AlertDialog.Builder(this)
                    .setTitle("Outdated version found")
                    .setMessage("Your version is outdated. Because you might be missing critical data in your synced versions. Sadly you will need a complete resync.")
                    .setPositiveButton("Clear Data and restart", { dialogInterface, i -> database.clear(); System.exit(1) })
                    .setNeutralButton("No, just exit", { dialogInterface, i -> System.exit(1) })
                    .show()
        } else {
            // Data is present, if a database has a backing file
            if (database.eventConferenceDayDb.time != null)
                startRootActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        updateReceiver.register()
    }

    override fun onPause() {
        super.onPause()
        updateReceiver.unregister()
    }

    private fun startRootActivity() {
        database.versionDb.items = listOf(BuildConfig.VERSION_NAME)

        startActivity(Intent(this, ActivityRoot::class.java))
        finish()
    }
}