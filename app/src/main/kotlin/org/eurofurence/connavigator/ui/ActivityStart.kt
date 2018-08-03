package org.eurofurence.connavigator.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.*
import at.grabner.circleprogress.CircleProgressView
import com.github.lzyzsd.circleprogress.CircleProgress
import com.github.lzyzsd.circleprogress.DonutProgress
import com.google.firebase.perf.metrics.AddTrace
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
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
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.circleProgress
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.eurofurence.connavigator.util.v2.thenNested
import org.jetbrains.anko.*

/**
 * Created by David on 28-4-2016.
 */
class ActivityStart : AppCompatActivity(), AnkoLogger, HasDb {
    override val db: Db
        get() = locateDb()

    val ui = StartUi()

    var subscriptions = Disposables.empty()

    @AddTrace(name = "ActivityStart:UpdateIntentService", enabled = true)
    private val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        if (it.booleans["success"]) {
            val imgCountTotal = db.images.items.count()
            var imgCountLoaded = 0
            ui.loadingSpinner.maxValue = imgCountTotal.toFloat()

            info { "Data update success. Downloading $imgCountTotal images" }

            val promises = db.images.items.fold(Promise.of<Any?>(Unit)) { p, img ->
                p thenNested {
                    // After the previous image, load the next one.
                    imageService.preload(img)
                } successUi {
                    // Increment the counter and display on UI.
                    imgCountLoaded++
                    ui.loadingSpinner.setValue(imgCountLoaded.toFloat())
                    ui.progressText.text = "Loading assets ($imgCountLoaded / $imgCountTotal)"
                    Unit
                }
            }

            // Await all images loaded, if one fails, mark but continue to UI.
            promises successUi {
                AppPreferences.isFirstRun = false

                longToast("Done with fetching!")

                this@ActivityStart.startActivity(intentFor<ActivityRoot>())
            } failUi {
                AppPreferences.isFirstRun = false

                longToast("Something went wrong while fetching!")

                this@ActivityStart.startActivity(intentFor<ActivityRoot>())
            }
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

        ui.yesButton.setOnClickListener {
            ui.startLayout.visibility = View.GONE
            ui.loadingLayout.visibility = View.VISIBLE

            UpdateIntentService.dispatchUpdate(this)
        }

        ui.noButton.setOnClickListener {
            longToast("Closing application.")
            System.exit(0)
        }

        subscriptions += RemotePreferences.observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ui.remoteLastUpdatedText.text = "Remote configs was updated ${it.timeSinceLastUpdate.millis / 1000} seconds ago."
                }

        savedInstanceState?.let {
            ui.analyticalData.isChecked = it.getBoolean("analyticalData", false)
            ui.performanceData.isChecked = it.getBoolean("performanceData", false)
            if (it.getBoolean("loading", false)) {
                ui.startLayout.visibility = View.GONE
                ui.loadingLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (ui.initialized) {
            outState.putBoolean("analyticalData", ui.analyticalData.isChecked)
            outState.putBoolean("performanceData", ui.performanceData.isChecked)
            outState.putBoolean("loading", ui.startLayout.visibility == View.GONE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    override fun onResume() {
        super.onResume()
        updateReceiver.register()
    }

    override fun onPause() {
        super.onPause()
        updateReceiver.unregister()
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
                    clearData()
                }
                onCancelled {
                    clearData()
                }
            }.show()

            return false
        }

        AppPreferences.lastKnownVersion = BuildConfig.VERSION_NAME

        return current == previous
    }

    private fun clearData() {
        AppPreferences.lastKnownVersion = BuildConfig.VERSION_NAME
        ResetReceiver.fire(this@ActivityStart)
    }
}

class StartUi : AnkoComponent<ActivityStart> {
    lateinit var yesButton: Button
    lateinit var noButton: Button
    lateinit var loadingSpinner: CircleProgressView
    lateinit var startLayout: LinearLayout
    lateinit var loadingLayout: RelativeLayout
    lateinit var remoteLastUpdatedText: TextView
    lateinit var progressText: TextView

    lateinit var analyticalData: CheckBox
    lateinit var performanceData: CheckBox
    var initialized = false

    override fun createView(ui: AnkoContext<ActivityStart>) = with(ui) {
        verticalLayout {
            backgroundResource = R.color.backgroundGrey

            linearLayout {
                backgroundResource = R.color.primaryDark
                ViewCompat.setElevation(this, 15f)

                imageView {
                    imageResource = R.mipmap.ic_launcher
                    padding = dip(30)
                }.lparams(dip(150), matchParent)

                textView("Welcome to the official Eurofurence App for Android!") {
                    gravity = Gravity.CENTER
                    textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                    compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                    setPadding(0, 0, dip(30), 0)
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, dip(150))

            scrollView {
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
                            background.setColorFilter(ContextCompat.getColor(context, R.color.primaryDark), PorterDuff.Mode.SRC)
                            textColor = ContextCompat.getColor(context, R.color.textWhite)
                        }

                        noButton = button("No, Not right now").lparams(matchParent, wrapContent)
                    }.lparams(matchParent, wrapContent)

                    analyticalData = checkBox("Allow Eurofurence to collect anonymous analytical data.") {
                        hint = "This can be changed in your settings at any time."
                        setOnCheckedChangeListener { compoundButton, b -> AnalyticsPreferences.enabled = b }
                    }.lparams(matchParent, wrapContent) {
                        padding = dip(30)
                    }

                    performanceData = checkBox("Allow Eurofurence to collect performance data.") {
                        hint = "This can be changed in your settings at any time."
                        setOnCheckedChangeListener { compoundButton, b -> AnalyticsPreferences.enabled = b }
                    }.lparams(matchParent, wrapContent) {
                        padding = dip(30)
                    }

                    remoteLastUpdatedText = textView("Remote configs was updated ${RemotePreferences.timeSinceLastUpdate.millis / 1000} seconds ago.")
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent)

            loadingLayout = relativeLayout {
                visibility = View.GONE

                loadingSpinner = circleProgress {
                    spin()
                    textSize = 0
                    innerContourSize = 0f
                    outerContourSize = 0f
                }.lparams(dip(100), dip(100)) {
                    above(1)
                    centerHorizontally()
                    padding = dip(50)
                }

                progressText = textView("Working... Please wait!") {
                    id = 1
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                }.lparams(matchParent, wrapContent) {
                    centerInParent()
                }
            }.lparams(matchParent, matchParent)
        }
    }.also {
        initialized = true
    }
}
