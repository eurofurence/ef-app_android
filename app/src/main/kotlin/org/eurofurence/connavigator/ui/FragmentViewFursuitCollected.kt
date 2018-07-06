package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.TextViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*

/**
 * Created by requinard on 7/24/17.
 */
class FragmentViewFursuitCollected : Fragment(), AnkoLogger {
    val ui = FursuitCollectedUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            if (container == null) null else ui.createView(AnkoContext.Companion.create(requireContext(), container))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        task {
            val api = apiService.fursuits.apply {
                invoker.addDefaultHeader("Authorization", AuthPreferences.asBearer())
            }

            api.apiV2FursuitsCollectingGamePlayerParticipationGet()
        } successUi {
            info { "succesfully retrieved data!" }
            info { it }

            ui.name.text = "Hello ${it.name}"
            ui.score.text = "You have collected ${it.collectionCount} suits, ranking you at number ${it.scoreboardRank}!"

            it.recentlyCollected.map {
                ui.collectedLayout.addView(
                        TextView(context).apply {
                            text = "${it.name}"
                            horizontalPadding = dip(10)
                        }
                )
            }
        } failUi {
            warn { "Failed to retrieve data" }
            error { it }
        }
    }
}

class FursuitCollectedUi : AnkoComponent<ViewGroup> {
    lateinit var name: TextView
    lateinit var score: TextView

    lateinit var collectedLayout: LinearLayout
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout{
            padding =  dip(25)

            name = textView{
                setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)
            }

            score = textView{
                setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Medium)
            }

            collectedLayout = verticalLayout {  }
        }
    }

}