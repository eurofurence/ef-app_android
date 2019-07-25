package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.LoadingState
import org.eurofurence.connavigator.services.UpdateIntentService
import org.eurofurence.connavigator.services.dispatchUpdate
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class LoadingIndicatorFragment : Fragment() {
    val ui = LoadingIndicatorFragmentUi()
    var subscriptions = Disposables.empty()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ui.quitButton.setOnClickListener { activity?.finish() }

        subscriptions += BackgroundPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    manageUI()
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    fun manageUI() = when (BackgroundPreferences.loadingState) {
        LoadingState.UNINITIALIZED -> {
            ui.setText(R.string.loading_unititialized_title, R.string.loading_unitialized_description)
        }
        LoadingState.LOADING_IMAGES -> {
            ui.setText(R.string.loading_img_title, R.string.loading_img_description)
        }
        LoadingState.SUCCEEDED -> {
            ui.invisible()
        }
        LoadingState.LOADING_DATA -> {
            ui.setText(R.string.loading_data_title, R.string.loading_data_description)
        }
        else -> {
            ui.setText(R.string.loading_failed_title, R.string.loading_failed_description, true)
        }
    }
}

class LoadingIndicatorFragmentUi : AnkoComponent<Fragment> {
    lateinit var layout: FrameLayout
    lateinit var errorButtonsLayout: LinearLayout

    lateinit var titleText : TextView
    lateinit var descriptionText: TextView
    lateinit var progressIndicator: ProgressBar
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

        if(isError) {
            descriptionText.textColorResource = R.color.error_color_material_light
            errorButtonsLayout.visibility = View.VISIBLE
            progressIndicator.visibility = View.INVISIBLE

        } else {
            descriptionText.compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
            errorButtonsLayout.visibility = View.GONE
            progressIndicator.visibility = View.VISIBLE
        }
    }

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        frameLayout {
            layout = this
            visibility = View.GONE

            linearLayout {
                weightSum = 20F
                backgroundResource = R.color.cardview_light_background

                setPadding(dip(5), dip(15), dip(5), dip(15))

                progressIndicator = progressBar {

                }.lparams(dip(0), dip(50)) {
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
                            setOnClickListener { UpdateIntentService().dispatchUpdate(this.context, true) }
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