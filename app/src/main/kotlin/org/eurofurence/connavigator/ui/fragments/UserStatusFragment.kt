package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.gcm.cancelFromRelated
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import java.util.*
import kotlin.concurrent.fixedRateTimer

class UserStatusFragment : Fragment(), AnkoLogger {
    val ui = UserStatusUi()

    private var timer: Timer? = null


    var subscriptions = Disposables.empty()

    private fun checkMessages() = task {
        info { "Checking message counts" }
        apiService.communications.let {
            it.addHeader("Authorization", AuthPreferences.asBearer())
            it.apiCommunicationPrivateMessagesGet()
        }
    } successUi { messages ->
        context?.let {
            it.notificationManager.apply {
                for (m in messages)
                    cancelFromRelated(m.id)
            }

            info { "Fetched messages, ${messages.count()} messages found" }

            val unreadMessages = messages.filter { it.readDateTimeUtc == null }

            if (unreadMessages.isNotEmpty()) {
                info { "Unread messages are present! Giving attention." }
                ui.subtitle.text = getString(R.string.message_you_have_unread_messages, unreadMessages.size)
                ui.subtitle.textColor = ContextCompat.getColor(it, R.color.primaryDark)
                ui.userIcon.textColor = ContextCompat.getColor(it, R.color.primaryDark)
            } else {
                info { "No unread messages found, displaying total message counts" }
                ui.subtitle.text = getString(R.string.message_you_have_messages, messages.count())
                ui.subtitle.textColor = ContextCompat.getColor(it, android.R.color.tertiary_text_dark)
                ui.userIcon.textColor = ContextCompat.getColor(it, android.R.color.tertiary_text_dark)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
                title.textResource = R.string.login_not_logged_in
                subtitle.textResource = R.string.login_tap_to_login
                layout.setOnClickListener {
                    findNavController().navigate(R.id.action_fragmentViewHome_to_loginActivity)
                }
            }

        } else {
            info { "User is logged in" }
            timer?.cancel()
            timer = fixedRateTimer(period = 60000L) {
                checkMessages()
            }
            ui.apply {
                title.text = getString(R.string.misc_welcome_user, AuthPreferences.username.capitalize())
                layout.setOnClickListener {
                    val action = FragmentViewHomeDirections.ActionFragmentViewHomeToFragmentViewMessageList()
                    findNavController().navigate(action)
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
                title = textView {
                    textResource = R.string.misc_title
                    compatAppearance = android.R.style.TextAppearance_Medium
                }

                subtitle = textView {
                    textResource = R.string.misc_fetching
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