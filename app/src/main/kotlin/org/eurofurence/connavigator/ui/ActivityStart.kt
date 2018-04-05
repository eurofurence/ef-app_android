package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
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
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.above
import org.jetbrains.anko.alert
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.button
import org.jetbrains.anko.centerHorizontally
import org.jetbrains.anko.centerInParent
import org.jetbrains.anko.checkBox
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.longToast
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.progressBar
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent
import org.jetbrains.anko.yesButton

/**
 * Created by David on 28-4-2016.
 */
class ActivityStart : AppCompatActivity(), AnkoLogger, HasDb {
    override val db: Db
        get() = locateDb()

    val ui = StartUi()

    @AddTrace(name = "ActivityStart:UpdateIntentService", enabled = true)
    private val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        if (it.booleans["success"]) {
            val imgCount = db.images.items.count()

            info { "Data update success. Downloading $imgCount images" }

            ui.loadingSpinner.apply {
                max = imgCount
                progress = 1
            }

            db.images.items.forEach {
                imageService.preload(it)
                ui.loadingSpinner.incrementProgressBy(1)
            }

            AppPreferences.isFirstRun = false

            longToast("Done with fetching!")

            startActivity(intentFor<ActivityRoot>())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AppPreferences.isFirstRun && checkVersion()) {
            startActivity(intentFor<ActivityRoot>())
            return
        }

        info { "Starting start activity" }

        ui.setContentView(this)

        updateReceiver.register()

        ui.yesButton.setOnClickListener {
            ui.startLayout.visibility = View.GONE
            ui.loadingLayout.visibility = View.VISIBLE

            UpdateIntentService.dispatchUpdate(this)
        }

        ui.noButton.setOnClickListener {
            longToast("Closing application.")
            System.exit(0)
        }
    }

    /**
     * Checks if a major or minor update has occurred
     */
    private fun checkVersion(): Boolean {
        val regex = Regex("\\d+.(\\d+).\\d+")

        val current = regex.findAll(BuildConfig.VERSION_NAME).first().value
        val previous = regex.findAll(AppPreferences.lastKnownVersion).first().value

        info { "Last Known Version: $current vs $previous" }
        // if versions differ, reset
        if (current != previous) {
            alert("Your data is outdated. You will need to re-download all data") {
                yesButton {
                    ResetReceiver.fire(this@ActivityStart)
                }
            }.show()

            return false
        }

        AppPreferences.lastKnownVersion = BuildConfig.VERSION_NAME

        return current == previous
    }
}

class StartUi : AnkoComponent<ActivityStart> {
    lateinit var yesButton: Button
    lateinit var noButton: Button
    lateinit var loadingSpinner: ProgressBar
    lateinit var startLayout: LinearLayout
    lateinit var loadingLayout: RelativeLayout

    override fun createView(ui: AnkoContext<ActivityStart>) = with(ui) {
        verticalLayout {
            backgroundResource = R.color.backgroundGrey

            linearLayout {
                backgroundResource = R.color.primaryDark
                imageView {
                    imageResource = R.mipmap.ic_launcher
                    padding = dip(30)
                }.lparams(dip(150), matchParent)

                textView("Welcome to the official \nEurofurence App for Android!") {
                    gravity = Gravity.CENTER
                    textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large_Inverse
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, dip(150))

            startLayout = verticalLayout {
                padding = dip(50)

                textView("""Before you can use this application we need to download some data from the Eurofurence servers to your phone.

This will consume a few megabytes of traffic and can take anywhere from a few seconds up to a few minutes, depending on the speed of your connection.

Is it okay to download the data now?
                    """) {
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault
                    textSize = 16F
                }

                textView("Choosing 'No' will close the application at this point.")

                linearLayout {
                    setPadding(0, 30, 0, 30)

                    yesButton = button("Yes!") {
                        setBackgroundColor(ContextCompat.getColor(context, R.color.primaryDark))
                        textColor = ContextCompat.getColor(context, R.color.textWhite)
                    }

                    noButton = button("No, Not right now").lparams(matchParent, wrapContent)
                }.lparams(matchParent, wrapContent)

                checkBox("Allow Eurofurence to collect anonymous analytical data.") {
                    hint = "This can be changed in your settings at any time."
                    setOnCheckedChangeListener { compoundButton, b -> AnalyticsPreferences.enabled = b }
                }.lparams(matchParent, wrapContent) {
                    padding = dip(30)
                }

                checkBox("Allow Eurofurence to collect performance data.") {
                    hint = "This can be changed in your settings at any time."
                    setOnCheckedChangeListener { compoundButton, b -> AnalyticsPreferences.enabled = b }
                }.lparams(matchParent, wrapContent) {
                    padding = dip(30)
                }
            }.lparams(matchParent, wrapContent)

            loadingLayout = relativeLayout {
                visibility = View.GONE

                loadingSpinner = progressBar().lparams(dip(100), dip(100)) {
                    above(1)
                    centerHorizontally()
                    padding = dip(50)
                }

                textView("Working... Please wait!") {
                    id = 1
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                }.lparams(matchParent, wrapContent) {
                    centerInParent()
                }
            }.lparams(matchParent, matchParent)
        }
    }

}
