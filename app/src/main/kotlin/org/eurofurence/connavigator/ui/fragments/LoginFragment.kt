package org.eurofurence.connavigator.ui.fragments

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.pawegio.kandroid.e
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.events.LoginReceiver
import org.eurofurence.connavigator.events.LogoutReceiver
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.util.EmbeddedLocalBroadcastReceiver
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.workers.FetchPrivateMessageWorker
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.browse
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.longToast
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Created by requinard on 6/26/17.
 */
class LoginFragment : Fragment(), AnkoLogger {

    val ui by lazy { LoginUi() }

    private lateinit var loginReceiver: EmbeddedLocalBroadcastReceiver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

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
                    ui.errorText.visibility = View.VISIBLE
                    ui.layoutMain.visibility = View.VISIBLE
                    ui.layoutBusy.visibility = View.GONE
                }
            }
        }


        info { "Starting login attempt" }

        ui.submit.setOnClickListener { attemptSubmit() }

        ui.logout.setOnClickListener {
            info { "Logging user out" }

            // Invalidate cache.
            PMService.invalidate()

            longToast(getString(R.string.login_logged_out))
            context?.sendBroadcast(intentFor<LogoutReceiver>())

            findNavController().popBackStack(R.id.navHome, false)
        }

        ui.moreInformation.setOnClickListener {
            browse("https://app.eurofurence.org/redir/why-login")
        }

        savedInstanceState?.let {
            ui.regNumber.setText(it.getString("regNumber", ""))
            ui.username.setText(it.getString("username", ""))
            ui.password.setText(it.getString("password", ""))
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (ui.initialized) {
            outState.putString("regNumber", ui.regNumber.text.toString())
            outState.putString("username", ui.username.text.toString())
            outState.putString("password", ui.password.text.toString())
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

        if (ui.regNumber.text.isEmpty()) {
            ui.regNumber.error = emptyText
            return
        }

        if (ui.username.text.isEmpty()) {
            ui.username.error = emptyText
            return
        }

        if (ui.password.text.isEmpty()) {
            ui.password.error = emptyText
            return
        }

        ui.layoutMain.visibility = View.GONE
        ui.layoutBusy.visibility = View.VISIBLE

        context?.sendBroadcast(intentFor<LoginReceiver>(
                LoginReceiver.REGNUMBER to ui.regNumber.text.toString(),
                LoginReceiver.USERNAME to ui.username.text.toString(),
                LoginReceiver.PASSWORD to ui.password.text.toString()
        ))
    }
}

class LoginUi : AnkoComponent<Fragment> {
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

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                lparams(matchParent, wrapContent)

                layoutBusy = verticalLayout {
                    visibility = View.GONE
                    gravity = Gravity.CENTER
                    orientation = LinearLayout.VERTICAL

                    progressBar {

                    }.lparams(wrapContent, wrapContent)

                    textView {
                        textResource = R.string.login_authenticating
                        padding = 20
                        gravity = Gravity.CENTER
                    }.lparams(matchParent, wrapContent)

                    lparams(matchParent, matchParent)
                }

                layoutMain = verticalLayout {

                    // If not logged in
                    verticalLayout {
                        visibility = if (AuthPreferences.isLoggedIn) View.GONE else View.VISIBLE

                        lparams(matchParent, matchParent)

                        username = editText {
                            hintResource = R.string.login_username
                            inputType = InputType.TYPE_CLASS_TEXT
                        }.lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }

                        regNumber = editText {
                            hintResource = R.string.login_reg_number
                            inputType = InputType.TYPE_CLASS_NUMBER
                        }.lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }

                        password = editText {
                            hintResource = R.string.login_password
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        }.lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }

                        errorText = textView {
                            textResource = R.string.login_unsuccessful
                            visibility = View.GONE
                            textColor = ContextCompat.getColor(ctx, R.color.primary)

                        }.lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }

                        textView {
                            textResource = R.string.login_information

                        }.lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }

                        submit = button {
                            textResource = R.string.login
                            background.setColorFilter(ContextCompat.getColor(context, R.color.primaryDark), PorterDuff.Mode.SRC)
                            textColor = ContextCompat.getColor(context, R.color.textWhite)


                        }.lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }

                        moreInformation = button {
                            textResource = R.string.misc_more_information

                        }.lparams(matchParent, wrapContent) {
                            setMargins(dip(16), 0, dip(16), dip(16))
                        }
                    }

                    // If logged in
                    verticalLayout {
                        visibility = if (AuthPreferences.isLoggedIn) View.VISIBLE else View.GONE

                        padding = dip(16)

                        textView {
                            text = resources.getString(R.string.login_logged_in_as, AuthPreferences.username)
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(8)
                        }

                        textView {
                            textResource = R.string.login_benefits
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(8)
                        }

                        textView {
                            text = resources.getString(
                                    R.string.login_valid_until,
                                    DateTime(AuthPreferences.tokenValidUntil).minusDays(1).toDate().toRelative())
                        }.lparams(matchParent, wrapContent)

                        logout = button {
                            textResource = R.string.login_logout

                        }.lparams(matchParent, wrapContent) {
                            verticalMargin = dip(16)
                        }

                        textView {
                            textResource = R.string.login_privacy_notice
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault
                            typeface = Typeface.DEFAULT_BOLD
                        }.lparams(matchParent, wrapContent) {
                            bottomMargin = dip(8)
                        }

                        textView {
                            textResource = R.string.login_telemetry_disclaimer
                        }.lparams(matchParent, wrapContent)
                    }

                    lparams(matchParent, matchParent)
                }
            }
        }.also {
            initialized = true
        }
    }
}