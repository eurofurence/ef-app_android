package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import at.grabner.circleprogress.CircleProgressView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState
import org.eurofurence.connavigator.util.extensions.circleProgress
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.eurofurence.connavigator.workers.DataUpdateWorker
import org.eurofurence.connavigator.workers.PreloadImageWorker
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class LoadingIndicatorFragment : DisposingFragment(), AnkoLogger {
    val ui = LoadingIndicatorFragmentUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onStart() {
        ui.quitButton.setOnClickListener { activity?.finish() }

        BackgroundPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    manageUI()
                }
                .collectOnDestroyView()

        manageUI()

        context?.let {
            WorkManager.getInstance(it).getWorkInfosByTagLiveData(PreloadImageWorker.TAG)
                    .observe(this, Observer {
                        info { "Changing progress bar" }
                        if (it != null) {
                            val progress = it.filter { it.state == WorkInfo.State.SUCCEEDED }.count().toFloat()

                            ui.progressIndicator.maxValue = it.count().toFloat()
                            ui.progressIndicator.setValue(it.filter { it.state == WorkInfo.State.SUCCEEDED }.count().toFloat())

                            info { "Finished $progress out of ${it.count().toFloat()}" }

                            // todo: hack -> should be run in the FinishedImagePreloadWorker, but the next item in the chain is never called
                            if (progress == it.count().toFloat() && BackgroundPreferences.loadingState == LoadingState.LOADING_IMAGES) {
                                BackgroundPreferences.loadingState = LoadingState.SUCCEEDED
                            }
                        }
                    })
        }

        super.onStart()
    }

    private fun manageUI() = when (BackgroundPreferences.loadingState) {
        LoadingState.UNINITIALIZED -> {
            ui.setText(R.string.loading_unititialized_title, R.string.loading_unitialized_description)
        }
        LoadingState.LOADING_IMAGES -> {
            ui.setText(R.string.loading_img_title, R.string.loading_img_description)
        }
        LoadingState.SUCCEEDED -> {
            ui.progressIndicator.spin()
            ui.invisible()
        }
        LoadingState.LOADING_DATA -> {
            ui.progressIndicator.spin()
            ui.setText(R.string.loading_data_title, R.string.loading_data_description)
        }
        LoadingState.PENDING -> {
            ui.progressIndicator.spin()
            ui.setText(R.string.loading_pending_title, R.string.loading_pending_description)
        }
        else -> {
            ui.setText(R.string.loading_failed_title, R.string.loading_failed_description, true)
        }
    }
}

class LoadingIndicatorFragmentUi : AnkoComponent<Fragment> {
    lateinit var layout: FrameLayout
    lateinit var errorButtonsLayout: LinearLayout

    lateinit var titleText: TextView
    lateinit var descriptionText: TextView
    lateinit var progressIndicator: CircleProgressView
    lateinit var quitButton: Button

    fun visible() {
        this.layout.visibility = View.VISIBLE
    }

    fun invisible() {
        this.layout.visibility = View.GONE
    }

    fun setText(titleRes: Int, descriptionRes: Int, isError: Boolean = false) {
        // Make the layout visible
        visible()

        titleText.textResource = titleRes
        descriptionText.textResource = descriptionRes

        if (isError) {
            descriptionText.textColorResource = R.color.error
            errorButtonsLayout.visibility = View.VISIBLE
            progressIndicator.visibility = View.INVISIBLE
        } else {
            descriptionText.compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
            errorButtonsLayout.visibility = View.GONE
            progressIndicator.visibility = View.VISIBLE

            if(!BackgroundPreferences.hasLoadedOnce) {
                descriptionText.text = "Please wait until we get a local copy of the data..."
            }
        }
    }

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        frameLayout {
            layout = this
            visibility = View.GONE

            linearLayout {
                weightSum = 20F
                backgroundResource = R.color.lightBackground

                setPadding(dip(5), dip(15), dip(5), dip(15))

                frameLayout {
                    progressIndicator = circleProgress {
                        spin()
                        textSize = 0
                        innerContourSize = 0f
                        outerContourSize = 0f
                        barWidth = dip(5)
                        rimWidth = dip(5)
                    }
                }.lparams(dip(0), dip(40)) {
                    weight = 3F
                }

                verticalLayout {
                    titleText = textView {
                        text = "Updating your data!"
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        textColorResource = R.color.textBlack
                    }
                    descriptionText = textView {
                        text = "Some of the data might not be completely present yet"
                        compatAppearance = android.R.style.TextAppearance_Material_Small
                    }

                    // Error buttons
                    errorButtonsLayout = linearLayout {
                        visibility = View.GONE
                        quitButton = button("Quit")

                        button("Retry") {
                            setOnClickListener { DataUpdateWorker.execute(context, true) }
                        }
                    }
                }.lparams(dip(0), wrapContent) {
                    weight = 17F
                }


            }.lparams(matchParent, wrapContent) {
                topMargin = dip(10)
            }
        }
    }
}