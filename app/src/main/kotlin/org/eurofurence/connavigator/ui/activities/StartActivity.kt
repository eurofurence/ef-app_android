package org.eurofurence.connavigator.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.*
import at.grabner.circleprogress.CircleProgressView
import com.google.firebase.perf.metrics.AddTrace
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.ResetReceiver
import org.eurofurence.connavigator.database.*
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.circleProgress
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.objects
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.eurofurence.connavigator.util.v2.thenNested
import org.jetbrains.anko.*

/**
 * Created by David on 28-4-2016.
 */
class StartActivity : AppCompatActivity(), AnkoLogger, HasDb {
    override val db: Db
        get() = locateDb()

    val ui = StartUi()

    var subscriptions = Disposables.empty()

    @AddTrace(name = "StartActivity:UpdateIntentService", enabled = true)
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
                    ui.progressText.text = getString(R.string.misc_fetching_assets, imgCountLoaded, imgCountTotal)
                    Unit
                }
            }

            // Await all images loaded, if one fails, mark but continue to UI.
            promises successUi {
                AppPreferences.isFirstRun = false

                longToast(getString(R.string.misc_fetching_done))

                this@StartActivity.startActivity(intentFor<NavActivity>())
            } failUi {
                AppPreferences.isFirstRun = false

                longToast(getString(R.string.misc_fetching_failed))

                this@StartActivity.startActivity(intentFor<NavActivity>())
            }
        } else {
            // If sync fails on the first start - we should tell the user why and exit afterwards.
            runOnUiThread {
                this@StartActivity.alert(
                        getString(R.string.misc_application_will_exit, (it.objects["reason"] as Exception).message ?: ""),
                        getString(R.string.error_retrieving_failed)
                    )
                {
                    okButton { System.exit(1) }
                    onCancelled { System.exit(1) }
                }
                .show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!AppPreferences.isFirstRun && checkVersion()) {
            startActivity(intentFor<NavActivity>())
            return
        }
        info { "Starting start activity" }

        ui.setContentView(this)

        ui.yesButton.setOnClickListener {
            ui.startLayout.visibility = View.GONE
            ui.loadingLayout.visibility = View.VISIBLE

            dispatchUpdate(this)
        }

        ui.noButton.setOnClickListener {
            longToast(getString(R.string.close_closing_application))
            System.exit(0)
        }

        subscriptions += RemotePreferences.observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ui.remoteLastUpdatedText.text = getString(R.string.misc_remote_config_were_updated, it.timeSinceLastUpdate.millis / 1000)
                }

        subscriptions += AnalyticsPreferences.observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Analytics.updateSettings()
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

        val current = regex.find(BuildConfig.VERSION_NAME)?.groupValues?.get(1) ?: ""
        val previous = regex.find(db.version.toString())?.groupValues?.get(1) ?: ""

        info { "Last Known Version: $current vs $previous" }
        // if versions differ, reset
        if (current != previous) {
            alert(getString(R.string.misc_data_outdated)) {
                yesButton {
                    clearData()
                }
                onCancelled {
                    clearData()
                }
            }.show()

            return false
        }

        db.version = BuildConfig.VERSION_NAME

        return current == previous
    }

    private fun clearData() {
        db.version = BuildConfig.VERSION_NAME
        ResetReceiver().clearData(this)
    }
}

class StartUi : AnkoComponent<StartActivity> {
    lateinit var yesButton: Button
    lateinit var noButton: Button
    lateinit var loadingSpinner: CircleProgressView
    lateinit var startLayout: LinearLayout
    lateinit var loadingLayout: RelativeLayout
    lateinit var remoteLastUpdatedText: TextView
    lateinit var versionIdentifierText: TextView
    lateinit var progressText: TextView

    lateinit var analyticalData: CheckBox
    lateinit var performanceData: CheckBox
    var initialized = false

    override fun createView(ui: AnkoContext<StartActivity>) = with(ui) {
        verticalLayout {
            backgroundResource = R.color.backgroundGrey

            linearLayout {
                backgroundResource = R.color.primaryDark
                ViewCompat.setElevation(this, 15f)

                imageView {
                    imageResource = R.mipmap.ic_launcher
                    padding = dip(30)
                }.lparams(dip(150), matchParent)

                textView {
                    textResource = R.string.misc_welcome_text
                    gravity = Gravity.CENTER
                    textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
                    compatAppearance = android.R.style.TextAppearance_Medium_Inverse
                    setPadding(0, 0, dip(30), 0)
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, dip(150))

            scrollView {
                startLayout = verticalLayout {
                    padding = dip(50)

                    textView {
                        textResource = R.string.misc_welcome_before_use
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault
                        textSize = 16F
                    }

                    textView{
                        textResource = R.string.close_no_will_close_the_app
                    }

                    linearLayout {
                        setPadding(0, 30, 0, 30)

                        yesButton = button {
                            textResource = R.string.misc_yes
                            background.setColorFilter(ContextCompat.getColor(context, R.color.primaryDark), PorterDuff.Mode.SRC)
                            textColor = ContextCompat.getColor(context, R.color.textWhite)
                        }

                        noButton = button {
                            textResource = R.string.misc_no_not_right_now
                        }.lparams(matchParent, wrapContent)
                    }.lparams(matchParent, wrapContent)

                    analyticalData = checkBox {
                        textResource = R.string.settings_analytics_allow_collecting_of_analytical_data
                        hintResource = R.string.settings_this_can_be_changed
                        isChecked = AnalyticsPreferences.enabled
                        setOnCheckedChangeListener { _, b -> AnalyticsPreferences.enabled = b }
                    }.lparams(matchParent, wrapContent) {
                        padding = dip(30)
                    }

                    performanceData = checkBox {
                        textResource = R.string.settings_analytics_allow_collecting_of_performance_data
                        hintResource = R.string.settings_this_can_be_changed
                        isChecked = AnalyticsPreferences.performanceTracking
                        setOnCheckedChangeListener { _, b -> AnalyticsPreferences.enabled = b }
                    }.lparams(matchParent, wrapContent) {
                        padding = dip(30)
                    }

                    remoteLastUpdatedText = textView {
                        text = resources.getString(R.string.misc_remote_config_were_updated, RemotePreferences.timeSinceLastUpdate.millis / 1000)
                        textSize = 10F
                    }

                    versionIdentifierText = textView {
                        text = resources.getString(R.string.misc_version_detailed, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, BuildConfig.CONVENTION_IDENTIFIER)
                        textSize = 10F
                    }
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

                progressText = textView {
                    textResource = R.string.misc_working_please_wait
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
