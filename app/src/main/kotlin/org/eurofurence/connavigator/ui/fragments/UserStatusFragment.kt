package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.pawegio.kandroid.notificationManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.AnkoLogger
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.notifications.cancelFromRelated
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.preferences.Authentication
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.ui.views.FontAwesomeType

import java.util.*

class UserStatusFragment : DisposingFragment(), AnkoLogger, HasDb {
    override val db by lazyLocateDb()
    lateinit var title: TextView
    lateinit var subtitle: TextView
    lateinit var userIcon: TextView
    lateinit var layout: ViewGroup


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        layout = linearLayout {
            layoutParams = viewGroupLayoutParams(matchParent, wrapContent) {
                setPadding(0, dip(20), 0, dip(20))
            }

            visibility = View.GONE
            isClickable = true

            weightSum = 100F
            backgroundResource = R.color.lightBackground

            userIcon = fontAwesomeView {
                layoutParams = linearLayoutParams(dip(0), matchParent, 15F)
                text = getString(R.string.fa_user)
                textSize = 30f
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            }

            verticalLayout {
                layoutParams = linearLayoutParams(dip(0), matchParent, 75F)
                title = textView {
                    textResource = R.string.misc_title
                    compatAppearance = android.R.style.TextAppearance_Medium
                }

                subtitle = textView {
                    textResource = R.string.misc_fetching
                    compatAppearance = android.R.style.TextAppearance_Small
                }
            }

            fontAwesomeView {
                type = FontAwesomeType.Solid
                layoutParams = linearLayoutParams(dip(0), matchParent, 10F)
                text = getString(R.string.fa_chevron_right_solid)
                textSize = 24f
                gravity = Gravity.CENTER
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Subscribe to changes in authentication, i.e., login status and user name.
        subscribeToAuthentication()

        // Subscribe to changes in PM availability.
        subscribeToPMs()

        // Subscriber to database changes
        subscribeToDatabase()
    }

    private fun subscribeToDatabase() {
        db.subscribe {
            val state = it.state
            val canLogin = state != null && state.toLowerCase() != "past"

            layout.visibility = if (canLogin) View.VISIBLE else View.GONE
            Unit
        }.collectOnDestroyView()
    }

    /**
     * Subscribes to authentication changes to display proper information on the UI and to activate a periodic refresh
     * of PMs.
     */
    private fun subscribeToAuthentication() {
        AuthPreferences
            .updated
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.isLoggedIn to it.username }
            .distinct()
            .subscribe { (isLoggedIn, username) ->
                // React to user login changes.
                if (isLoggedIn) {
                    // Log status change.
                    info { "User is logged in" }

                    // Display logged in, name and set UI action.
                    title.text = getString(R.string.misc_welcome_user, username.capitalize())
                    subtitle.textResource = R.string.login_tap_to_login
                    layout.setOnClickListener {
                        val action =
                            HomeFragmentDirections.actionFragmentViewHomeToFragmentViewMessageList()
                        findNavController().navigate(action)
                    }
                } else {
                    // Log status change.
                    info { "User is not logged in" }

                    // Display not logged in, reset UI action.
                    title.textResource = R.string.login_not_logged_in
                    subtitle.textResource = R.string.login_tap_to_login
                    layout.setOnClickListener {
                        findNavController().navigate(R.id.action_fragmentViewHome_to_loginActivity)
                    }
                }
            }
            .collectOnDestroyView()
    }

    /**
     * Subscribes to PM availability by displaying and cancelling notifications, as well as indicating the number
     * of unread messages.
     */
    private fun subscribeToPMs() {

        // Include update status in observable.
        Observable.combineLatest(
            AuthPreferences.updated, PMService.updated,
            BiFunction { l: Authentication, r: Map<UUID, PrivateMessageRecord> ->
                l to r
            })

            // Use UI thread for subscription.
            .observeOn(AndroidSchedulers.mainThread())

            // React to authentication and messages status.
            .subscribe { (auth, messages) ->
                if (auth.isLoggedIn)
                    context?.apply {
                        for ((_, m) in messages)
                            context?.notificationManager?.cancelFromRelated(m.id)

                        info { "Fetched messages, ${messages.count()} messages found" }

                        val unreadMessages = messages.values.filter { it.readDateTimeUtc == null }

                        if (unreadMessages.isNotEmpty()) {
                            info { "Unread messages are present! Giving attention." }
                            subtitle.text = getString(
                                R.string.message_you_have_unread_messages,
                                unreadMessages.size
                            )
                            subtitle.textColorResource = R.color.primaryDark
                            userIcon.textColorResource = R.color.primaryDark
                        } else {
                            info { "No unread messages found, displaying total message counts" }
                            subtitle.text =
                                getString(R.string.message_you_have_messages, messages.count())
                            subtitle.textColorResource = android.R.color.tertiary_text_dark
                            userIcon.textColorResource = android.R.color.tertiary_text_dark
                        }
                    }
            }
            .collectOnDestroyView()

    }
}
