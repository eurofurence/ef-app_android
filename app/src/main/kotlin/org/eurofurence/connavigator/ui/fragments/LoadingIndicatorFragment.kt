package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.work.WorkInfo
import androidx.work.WorkManager
import at.grabner.circleprogress.CircleProgressView
import io.reactivex.android.schedulers.AndroidSchedulers
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState
import org.eurofurence.connavigator.workers.DataUpdateWorker
import org.eurofurence.connavigator.workers.PreloadImageWorker

class LoadingIndicatorFragment : DisposingFragment(), AnkoLogger {
    lateinit var layout: FrameLayout
    lateinit var errorButtonsLayout: LinearLayout

    lateinit var titleText: TextView
    lateinit var descriptionText: TextView
    lateinit var progressIndicator: CircleProgressView
    lateinit var quitButton: Button


    fun visible() {
        layout.visibility = View.VISIBLE
    }

    fun invisible() {
        layout.visibility = View.GONE
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

            if (!BackgroundPreferences.hasLoadedOnce) {
                descriptionText.text = "Please wait until we get a local copy of the data..."
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        frameLayout {
            layout = this
            visibility = View.GONE

            linearLayout {
                layoutParams = frameLayoutParams(matchParent, wrapContent) {
                    topMargin = dip(10)
                }
                weightSum = 20F
                backgroundResource = R.color.lightBackground

                setPadding(dip(5), dip(15), dip(5), dip(15))

                frameLayout {
                    layoutParams = linearLayoutParams(dip(0), dip(40), 3f)
                    progressIndicator = circleProgressView {
                        spin()
                        textSize = 0
                        innerContourSize = 0f
                        outerContourSize = 0f
                        barWidth = dip(5)
                        rimWidth = dip(5)
                    }
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(dip(0), wrapContent, 17f)
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
                        quitButton = button("Quit") {
                        }

                        button("Retry") {
                            setOnClickListener { DataUpdateWorker.execute(context, true) }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        quitButton.setOnClickListener { activity?.finish() }

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
                        val progress =
                            it.filter { it.state == WorkInfo.State.SUCCEEDED }.count().toFloat()

                        progressIndicator.maxValue = it.count().toFloat()
                        progressIndicator.setValue(it.filter { it.state == WorkInfo.State.SUCCEEDED }
                            .count().toFloat())

                        info { "Finished $progress out of ${it.count().toFloat()}" }

                        // todo: hack -> should be run in the FinishedImagePreloadWorker, but the next item in the chain is never called
                        if (progress == it.count()
                                .toFloat() && BackgroundPreferences.loadingState == LoadingState.LOADING_IMAGES
                        ) {
                            BackgroundPreferences.loadingState = LoadingState.SUCCEEDED
                        }
                    }
                })
        }

        super.onStart()
    }

    private fun manageUI() = when (BackgroundPreferences.loadingState) {
        LoadingState.UNINITIALIZED -> {
            setText(
                R.string.loading_unititialized_title,
                R.string.loading_unitialized_description
            )
        }
        LoadingState.LOADING_IMAGES -> {
            setText(R.string.loading_img_title, R.string.loading_img_description)
        }
        LoadingState.SUCCEEDED -> {
            progressIndicator.spin()
            invisible()
        }
        LoadingState.LOADING_DATA -> {
            progressIndicator.spin()
            setText(R.string.loading_data_title, R.string.loading_data_description)
        }
        LoadingState.PENDING -> {
            progressIndicator.spin()
            setText(R.string.loading_pending_title, R.string.loading_pending_description)
        }
        else -> {
            setText(R.string.loading_failed_title, R.string.loading_failed_description, true)
        }
    }
}
