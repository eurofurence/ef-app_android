package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.ui.views.FontAwesomeType

class AppStatusFragment : DisposingFragment(), HasDb {
    override val db by lazyLocateDb()

    lateinit var stateLayout: LinearLayout
    lateinit var stateText: TextView
    lateinit var layout: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        layout = scrollView {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)
            backgroundResource = R.color.lightBackground

            verticalLayout {
                stateLayout = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent) {
                        setPadding(0, dip(20), 0, dip(20))
                    }

                    weightSum = 100f

                    fontAwesomeView {
                        type= FontAwesomeType.Solid
                        text = getString(R.string.fa_exclamation_triangle_solid)
                        textSize = 30f
                        textColorResource = R.color.textBlack
                        layoutParams = linearLayoutParams(dip(0), matchParent, 15F)
                        gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    }

                    verticalLayout {
                        layoutParams = linearLayoutParams(dip(0), wrapContent) {
                            weight = 75F
                        }
                        stateText = textView {
                            textResource = R.string.misc_version
                            compatAppearance = android.R.style.TextAppearance_Small
                            textColorResource = R.color.textBlack
                        }
                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        db.subscribe {

            val state = it.state
            if (state == null || state.toLowerCase() == "live") {
                layout.visibility = View.GONE
            } else {
                layout.visibility = View.VISIBLE

                if (state.toLowerCase() == "past") {
                    stateText.textResource = R.string.app_state_past
                    stateLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.errorBackground
                        )
                    )
                }
                if (state.toLowerCase() == "preview") {
                    stateText.textResource = R.string.app_state_preview
                    stateLayout.setBackgroundColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.warningBackground
                        )
                    )
                }
            }
        }.collectOnDestroyView()
    }
}
