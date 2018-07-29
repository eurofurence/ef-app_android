package org.eurofurence.connavigator.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.swagger.client.ApiException
import io.swagger.client.model.ApiSafeResultCollectTokenResponse
import io.swagger.client.model.CollectTokenResponse
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by requinard on 7/24/17.
 */
class FragmentViewFursuitGame : Fragment(), ContentAPI, HasDb, AnkoLogger {
    val ui = FursuitGameUi()
    override val db by lazyLocateDb()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

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
            info { "Making  network request" }
            val api = apiService.fursuits.apply {
                invoker.addDefaultHeader("Authorization", AuthPreferences.asBearer())
            }

            api.apiV2FursuitsCollectingGamePlayerParticipationCollectTokenSafePost(tag)
        } successUi {
            info { "Succesfully executed network request! Showing fursuit" }

            if(it.isSuccessful){
                ui.setFursuit(it.result)
                ui.setMode(FursuitUiMode.SHOW_SUIT)
            } else {
                ui.error.text = it.error.message
                ui.setMode(FursuitUiMode.ERROR)
            }
        } failUi {
            warn { "Network request failed!" }
            val throwable = it as ApiException

            ui.error.text =  when(throwable.code){
                400 -> it.message ?: "You've already caught this suiter"
                401 -> "You're not logged in!"
                else -> "An error occured!"
            }
            ui.setMode(FursuitUiMode.ERROR)
        }
    }

}

class FursuitGameUi : AnkoComponent<Fragment> {
    lateinit var loading: ProgressBar
    lateinit var error: TextView

    lateinit var submit: Button
    lateinit var fursuitLabel: EditText

    lateinit var fursuitName: TextView

    lateinit var startLayout: LinearLayout
    lateinit var fursuitLayout: LinearLayout

    fun setMode(mode: FursuitUiMode) {
        when (mode) {
            FursuitUiMode.START -> {
                loading.visibility = View.GONE
                startLayout.visibility = View.VISIBLE
                fursuitLayout.visibility = View.GONE
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
            FursuitUiMode.ERROR -> {
                loading.visibility = View.GONE
                startLayout.visibility = View.VISIBLE
            }
        }
    }

    fun setFursuit(token: CollectTokenResponse) {
        fursuitName.text = "${token.name} the ${token.species}"
    }

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
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

                error = textView {
                    text = ""
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

                imageView{
                    imageResource = R.drawable.placeholder_event
                }

                textView("You have collected"){
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                }

                fursuitName = textView{
                    textAlignment = View.TEXT_ALIGNMENT_CENTER
                    setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)
                }

                button("Return to the tagging menu"){
                    setOnClickListener { setMode(FursuitUiMode.START) }
                }
            }.lparams(matchParent, matchParent)
        }
    }
}

enum class FursuitUiMode {
    START,
    LOADING,
    SHOW_SUIT,
    ERROR
}