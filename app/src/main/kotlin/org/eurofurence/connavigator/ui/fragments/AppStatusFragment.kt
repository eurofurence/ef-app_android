package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

class AppStatusFragment : DisposingFragment(), HasDb {
    val ui = AppStatusUi()
    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db.subscribe {

            val state = it.state
            if (state == null || state.toLowerCase() == "live") {
                ui.layout.visibility = View.GONE
            } else {
                ui.layout.visibility = View.VISIBLE

                if (state.toLowerCase() == "past") {
                    ui.stateText.textResource = R.string.app_state_past
                    ui.stateLayout.backgroundColor = ContextCompat.getColor(context!!, R.color.errorBackground);
                }
                if (state.toLowerCase() == "preview") {
                    ui.stateText.textResource = R.string.app_state_preview
                    ui.stateLayout.backgroundColor = ContextCompat.getColor(context!!, R.color.warningBackground);
                }
            }
        }.collectOnDestroyView()
    }
}

class AppStatusUi : AnkoComponent<Fragment> {
    lateinit var stateLayout: LinearLayout
    lateinit var stateText: TextView
    lateinit var layout: ViewGroup

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            this@AppStatusUi.layout = this
            lparams(matchParent, matchParent)
            backgroundResource = R.color.lightBackground

            verticalLayout {

                stateLayout = linearLayout {
                    lparams(matchParent, wrapContent) {
                        setPadding(0, dip(20), 0, dip(20))
                    }

                    weightSum = 100f

                    fontAwesomeView {
                        text = "{fa-warning 30sp}"
                        textColor = ContextCompat.getColor(context, R.color.textBlack)
                        lparams(dip(0), matchParent, 15F)
                        gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    }

                    verticalLayout {
                        stateText = textView {
                            textResource = R.string.misc_version
                            compatAppearance = android.R.style.TextAppearance_Small
                            textColor = ContextCompat.getColor(context, R.color.textBlack)
                        }

                    }.lparams(dip(0), wrapContent) {
                        weight = 75F
                    }

                }.lparams(matchParent, wrapContent) {
                    topMargin = dip(20)
                }
            }
        }
    }

}