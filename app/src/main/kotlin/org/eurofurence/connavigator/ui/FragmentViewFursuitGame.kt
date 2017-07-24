package org.eurofurence.connavigator.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.jetbrains.anko.*

/**
 * Created by requinard on 7/24/17.
 */
class FragmentViewFursuitGame : Fragment(), ContentAPI, HasDb, AnkoLogger {
    val ui = FursuitGameUi()
    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            if (container == null) null else ui.createView(AnkoContext.Companion.create(context, container))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Fursuit Games")

        applyOnRoot { changeTitle("Fursuit Games") }

        ui.submit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        info { "Submitting to fursuit games!" }
        info { "Switching UI to loading" }

        ui.setMode(FursuitUiMode.LOADING)

        val tag = ui.fursuitLabel.text.toString()

        info { "Looking for tag $tag" }

        task {

        }
    }

}

class FursuitGameUi : AnkoComponent<ViewGroup> {
    lateinit var loading: ProgressBar

    lateinit var submit: Button
    lateinit var fursuitLabel: EditText

    lateinit var startLayout: LinearLayout
    lateinit var fursuitLayout: LinearLayout

    fun setMode(mode: FursuitUiMode) {
        when (mode) {
            FursuitUiMode.START -> {
                loading.visibility = View.GONE
                startLayout.visibility = View.VISIBLE
            }
            FursuitUiMode.LOADING -> {
                loading.visibility = View.VISIBLE
                startLayout.visibility = View.GONE
                fursuitLayout.visibility = View.GONE
            }
            FursuitUiMode.SHOW_SUIT -> {
                loading.visibility = View.GONE
                fursuitLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            lparams(matchParent, matchParent)

            loading = progressBar {
                visibility = View.GONE
            }.lparams(wrapContent, wrapContent) {
                centerHorizontally()
                centerVertically()
            }

            startLayout = verticalLayout {
                padding = dip(25)

                textView {
                    text = "Fill in a fursuiter ID!"
                }

                fursuitLabel = editText {
                    hint = "They should have a tag somewhere!"
                }.lparams(matchParent, wrapContent)

                submit = button {
                    text = "Submit"
                }
            }

            fursuitLayout = verticalLayout {
                visibility = View.GONE
                textView("This shows a fursuiter!")
            }
        }
    }
}

enum class FursuitUiMode {
    START,
    LOADING,
    SHOW_SUIT
}