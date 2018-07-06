package org.eurofurence.connavigator.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.ui.FragmentViewMessageList
import org.eurofurence.connavigator.ui.LoginActivity
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoContext.Companion
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

class UserStatusFragment : Fragment(), AnkoLogger {
    val ui = UserStatusUi()
    val loginObservable = Observable.just(AuthPreferences.token)
            .subscribeOn(AndroidSchedulers.mainThread())

    private lateinit var timer: Timer

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        checkMessages()
        info { "Check timer initializing" }
            timer = fixedRateTimer(period = 60000L) {
        }
    }

    override fun onDetach() {
        super.onDetach()
        timer.cancel()
        info { "Check timer canceled" }
    }

    fun checkMessages() = task {
        info { "Checking message counts" }
        val api = apiService.communications

        api.addHeader("Authorization", AuthPreferences.asBearer())

        api.apiV2CommunicationPrivateMessagesGet()
    } successUi { messages ->
        info { "Fetched messages, ${messages.count()} messages found" }

        val unreadMessages = messages.filter { it.readDateTimeUtc == null }

        if (unreadMessages.isNotEmpty()) {
            info { "Unread messages are present! Giving attention." }
            ui.subtitle.text = "You have ${unreadMessages.size} new, unread personal message(s)!"
            ui.subtitle.textColor = ContextCompat.getColor(requireContext(), R.color.primaryDark)
            ui.userIcon.textColor = ContextCompat.getColor(requireContext(), R.color.primaryDark)
        } else {
            info { "No unread messages found, displaying total message counts" }
            ui.subtitle.text = "You have ${messages.count()} personal message(s)."
            ui.subtitle.textColor = ContextCompat.getColor(requireContext(), android.R.color.tertiary_text_dark)
            ui.userIcon.textColor = ContextCompat.getColor(requireContext(), android.R.color.tertiary_text_dark)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(Companion.create(requireContext(), container!!))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginObservable.subscribe {
            if (!AuthPreferences.isLoggedIn()) {
                ui.title.text = "You are currently not logged in."
                ui.subtitle.text = "Tap here to login using your registration details and receive personalized information from Eurofurence!"
                ui.layout.setOnClickListener {
                    startActivity(intentFor<LoginActivity>())
                }
            } else {
                ui.title.text = "Welcome, ${AuthPreferences.username.capitalize()}"
                ui.layout.setOnClickListener {
                    applyOnRoot {
                        navigateRoot(FragmentViewMessageList::class.java)
                    }
                }
            }
        }
    }
}

class UserStatusUi : AnkoComponent<ViewGroup> {
    lateinit var title: TextView
    lateinit var subtitle: TextView
    lateinit var userIcon: TextView
    lateinit var layout: ViewGroup

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            this@UserStatusUi.layout = this
            isClickable = true

            lparams(matchParent, wrapContent) {
                topMargin = dip(20)
                setPadding(0, dip(20), 0, dip(20))
            }
            weightSum = 100F
            backgroundResource = R.color.cardview_light_background

            userIcon = fontAwesomeView {
                text = "{fa-user 30sp}"
                lparams(dip(0), matchParent, 15F)
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            }

            verticalLayout {
                title = textView("Title") {
                    compatAppearance = android.R.style.TextAppearance_Medium
                }

                subtitle = textView("Fetching . . .") {
                    compatAppearance = android.R.style.TextAppearance_Small
                }
                lparams(dip(0), matchParent, 75F)
            }

            fontAwesomeView {
                text = "{fa-chevron-right 24sp}"
                gravity = Gravity.CENTER
                lparams(dip(0), matchParent, 10F)
            }
        }
    }

}