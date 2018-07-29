package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.ui.FragmentViewMessageList
import org.eurofurence.connavigator.ui.LoginActivity
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.intentFor
import java.util.*
import kotlin.concurrent.fixedRateTimer

class UserStatusFragment : Fragment(), AnkoLogger {
    val ui = UserStatusUi()

    private var timer: Timer? = null


    var subscriptions = Disposables.empty()

    fun checkMessages() = task {
        info { "Checking message counts" }
        val api = apiService.communications

        api.addHeader("Authorization", AuthPreferences.asBearer())

        api.apiV2CommunicationPrivateMessagesGet()
    } successUi { messages ->
        context?.let {
            info { "Fetched messages, ${messages.count()} messages found" }

            val unreadMessages = messages.filter { it.readDateTimeUtc == null }

            if (unreadMessages.isNotEmpty()) {
                info { "Unread messages are present! Giving attention." }
                ui.subtitle.text = "You have ${unreadMessages.size} new, unread personal message(s)!"
                ui.subtitle.textColor = ContextCompat.getColor(it, R.color.primaryDark)
                ui.userIcon.textColor = ContextCompat.getColor(it, R.color.primaryDark)
            } else {
                info { "No unread messages found, displaying total message counts" }
                ui.subtitle.text = "You have ${messages.count()} personal message(s)."
                ui.subtitle.textColor = ContextCompat.getColor(it, android.R.color.tertiary_text_dark)
                ui.userIcon.textColor = ContextCompat.getColor(it, android.R.color.tertiary_text_dark)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        subscriptions += AuthPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info { "Received from observable" }
                    updateState()
                }

        updateState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()

        timer?.cancel()
        info { "Check timer canceled" }
    }

    private fun updateState() {
        info { "Updating status fragment" }
        if (!AuthPreferences.isLoggedIn()) {
            info { "User is not logged in" }
            timer?.cancel()

            ui.apply {
                title.text = "You are currently not logged in."
                subtitle.text = "Tap here to login using your registration details and receive personalized information from Eurofurence!"
                layout.setOnClickListener {
                    startActivity(intentFor<LoginActivity>())
                }
            }

        } else {
            info { "User is logged in" }
            timer = fixedRateTimer(period = 60000L) {
                checkMessages()
            }
            ui.apply {
                title.text = "Welcome, ${AuthPreferences.username.capitalize()}"
                layout.setOnClickListener {
                    applyOnRoot {
                        navigateRoot(FragmentViewMessageList::class.java)
                    }
                }
            }
        }
    }
}

class UserStatusUi : AnkoComponent<Fragment> {
    lateinit var title: TextView
    lateinit var subtitle: TextView
    lateinit var userIcon: TextView
    lateinit var layout: ViewGroup

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
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