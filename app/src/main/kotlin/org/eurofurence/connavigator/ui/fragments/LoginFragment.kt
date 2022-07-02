package org.eurofurence.connavigator.ui.fragments

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pawegio.kandroid.longToast
import com.pawegio.kandroid.runOnUiThread
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.events.LoginReceiver
import org.eurofurence.connavigator.events.LogoutReceiver
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.browse
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.workers.FetchPrivateMessageWorker
import org.joda.time.DateTime

/**
 * Created by requinard on 6/26/17.
 */
class LoginFragment : Fragment(), AnkoLogger {

    lateinit var username: EditText
    lateinit var regNumber: EditText
    lateinit var password: EditText
    lateinit var errorText: TextView
    lateinit var submit: Button
    lateinit var logout: Button
    lateinit var moreInformation: Button
    lateinit var layoutMain: LinearLayout
    lateinit var layoutBusy: LinearLayout
    var initialized = false
    private lateinit var loginReceiver: EmbeddedLocalBroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        scrollView {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)
            verticalLayout {
                layoutParams = viewGroupLayoutParams(matchParent, wrapContent)

                layoutBusy = verticalLayout {
                    layoutParams = linearLayoutParams(matchParent, matchParent)
                    visibility = View.GONE
                    gravity = Gravity.CENTER
                    orientation = LinearLayout.VERTICAL

                    progressBar {
                        layoutParams = linearLayoutParams(wrapContent, wrapContent)
                    }

                    textView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                        textResource = R.string.login_authenticating
                        padding = 20
                        gravity = Gravity.CENTER
                    }
                }

                layoutMain = verticalLayout {
                    layoutParams = linearLayoutParams(matchParent, matchParent)

                    // If not logged in
                    verticalLayout {
                        layoutParams = linearLayoutParams(matchParent, matchParent)
                        visibility = if (AuthPreferences.isLoggedIn) View.GONE else View.VISIBLE

                        username = editText {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), dip(16), dip(16), dip(16))
                            }

                            hintResource = R.string.login_username
                            inputType = InputType.TYPE_CLASS_TEXT
                        }

                        regNumber = editText {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), dip(16), dip(16), dip(16))
                            }

                            hintResource = R.string.login_reg_number
                            inputType = InputType.TYPE_CLASS_NUMBER
                        }

                        password = editText {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), dip(16), dip(16), dip(16))
                            }

                            hintResource = R.string.login_password
                            inputType =
                                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        }

                        errorText = textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), dip(16), dip(16), dip(16))
                            }

                            textResource = R.string.login_unsuccessful
                            visibility = View.GONE
                            textColorResource = R.color.primary

                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), dip(16), dip(16), dip(16))
                            }

                            textResource = R.string.login_information
                        }

                        submit = button {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), dip(16), dip(16), dip(16))
                            }

                            textResource = R.string.login
                            background.setColorFilter(
                                ContextCompat.getColor(
                                    context,
                                    R.color.primaryDark
                                ), PorterDuff.Mode.SRC
                            )
                            textColorResource = R.color.textWhite
                        }

                        moreInformation = button {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(dip(16), 0, dip(16), dip(16))
                            }

                            textResource = R.string.misc_more_information
                        }
                    }

                    // If logged in
                    verticalLayout {
                        visibility = if (AuthPreferences.isLoggedIn) View.VISIBLE else View.GONE

                        padding = dip(16)

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(0, 0, 0, dip(8))
                            }

                            text = resources.getString(
                                R.string.login_logged_in_as,
                                AuthPreferences.username
                            )
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault
                            typeface = Typeface.DEFAULT_BOLD
                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(0, 0, 0, dip(8))
                            }

                            textResource = R.string.login_benefits
                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent)
                            text = resources.getString(
                                R.string.login_valid_until,
                                DateTime(AuthPreferences.tokenValidUntil).minusDays(1).toDate()
                                    .toRelative()
                            )
                        }

                        logout = button {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(0, dip(16), 0, dip(16))
                            }
                            textResource = R.string.login_logout
                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(0, 0, 0, dip(8))
                            }
                            textResource = R.string.login_privacy_notice
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault
                            typeface = Typeface.DEFAULT_BOLD
                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent)
                            textResource = R.string.login_telemetry_disclaimer
                        }
                    }

                }
            }
        }.also {
            initialized = true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Replace this at some point with something not as poo.
        loginReceiver = requireContext().localReceiver(LoginReceiver.LOGIN_RESULT) {
            val success = it.booleans["success"]

            info { "Received broadcast from LoginReceiver" }

            // Just logged in, refresh messages.
            FetchPrivateMessageWorker.execute(this)

            runOnUiThread {
                if (success) {
                    info { "Login was success! Closing activity" }
                    longToast(getString(R.string.login_logged_in_as, AuthPreferences.username))

                    findNavController().popBackStack(R.id.navHome, false)
                } else {
                    info { "Login failed! Showing error message" }
                    longToast(getString(R.string.login_failed))
                    errorText.visibility = View.VISIBLE
                    layoutMain.visibility = View.VISIBLE
                    layoutBusy.visibility = View.GONE
                }
            }
        }


        info { "Starting login attempt" }

        submit.setOnClickListener { attemptSubmit() }

        logout.setOnClickListener {
            info { "Logging user out" }

            // Invalidate cache.
            PMService.invalidate()

            longToast(getString(R.string.login_logged_out))
            context?.sendBroadcast(Intent(context, LogoutReceiver::class.java))


            findNavController().popBackStack(R.id.navHome, false)
        }

        moreInformation.setOnClickListener {
            requireContext().browse("https://app.eurofurence.org/redir/why-login")
        }

        savedInstanceState?.let {
            regNumber.setText(it.getString("regNumber", ""))
            username.setText(it.getString("username", ""))
            password.setText(it.getString("password", ""))
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (initialized) {
            outState.putString("regNumber", regNumber.text.toString())
            outState.putString("username", username.text.toString())
            outState.putString("password", password.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        loginReceiver.register()
    }

    override fun onPause() {
        super.onPause()
        loginReceiver.unregister()
    }

    /**
     * Validates the fields from the UI
     */
    private fun attemptSubmit() {
        val emptyText = getString(R.string.error_field_is_empty)

        if (regNumber.text.isEmpty()) {
            regNumber.error = emptyText
            return
        }

        if (username.text.isEmpty()) {
            username.error = emptyText
            return
        }

        if (password.text.isEmpty()) {
            password.error = emptyText
            return
        }

        layoutMain.visibility = View.GONE
        layoutBusy.visibility = View.VISIBLE

        context?.sendBroadcast(
            Intent(context, LoginReceiver::class.java).apply {
                putExtra(LoginReceiver.REGNUMBER, regNumber.text.toString())
                putExtra(LoginReceiver.USERNAME, username.text.toString())
                putExtra(LoginReceiver.PASSWORD, password.text.toString())
            }
        )
    }
}
