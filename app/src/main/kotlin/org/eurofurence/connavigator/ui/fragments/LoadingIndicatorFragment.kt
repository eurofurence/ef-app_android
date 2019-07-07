package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.preferences.BackgroundPreferences
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

        subscriptions += BackgroundPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    ui.layout.visibility = if (BackgroundPreferences.isLoading) View.VISIBLE else View.GONE
                }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }
}

class LoadingIndicatorFragmentUi : AnkoComponent<Fragment> {
    lateinit var layout: FrameLayout

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        frameLayout {
            layout = this
            visibility = View.GONE

            linearLayout {
                weightSum = 20F
                backgroundResource = R.color.cardview_light_background

                setPadding(dip(5), dip(15), dip(5), dip(15))

                progressBar {

                }.lparams(dip(0), dip(50)) {
                    weight = 3F
                }

                verticalLayout {
                    textView {
                        text = "Updating your data!"
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        textColorResource = R.color.textBlack
                    }
                    textView {
                        text = "Some of the data might not be completely present yet"
                        compatAppearance = android.R.style.TextAppearance_Material_Small
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