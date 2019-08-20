package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.ui.LayoutConstants
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class QuickOverviewFragment : Fragment() {
    val ui = QuickOverviewUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = UI { ui.createView(this) }.view
}

class QuickOverviewUi : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            backgroundResource = R.color.lightBackground
            padding = dip(LayoutConstants.MARGIN_LARGE)

            lparams(matchParent, wrapContent) {
                topMargin = dip(LayoutConstants.MARGIN_LARGE)
                bottomMargin = dip(LayoutConstants.MARGIN_LARGE)
            }

            textView("Quick Overview") {
                verticalPadding = dip(5)
            }

            // Fursuit Lounge
            relativeLayout {
                textView("Fursuit Lounge") {
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    textColorResource = R.color.textBlack
                }.lparams {
                    alignParentLeft()
                }

                textView("Open") {
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    textColorResource = R.color.success
                }.lparams {
                    alignParentRight()
                }
            }.lparams(matchParent, wrapContent)

            // Dealers Den
            relativeLayout {
                textView("Dealer's Den") {
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    textColorResource = R.color.textBlack
                }.lparams {
                    alignParentLeft()
                }

                textView("Supersponsor") {
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    textColorResource = R.color.supersponsor
                }.lparams {
                    alignParentRight()
                }
            }.lparams(matchParent, wrapContent)

            // Art Auction
            relativeLayout {
                textView("Art Auction") {
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    textColorResource = R.color.textBlack
                }.lparams {
                    alignParentLeft()
                }

                textView("Set-up (Artists Only)") {
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    textColorResource = R.color.error
                }.lparams {
                    alignParentRight()
                }
            }.lparams(matchParent, wrapContent)
        }
    }
}