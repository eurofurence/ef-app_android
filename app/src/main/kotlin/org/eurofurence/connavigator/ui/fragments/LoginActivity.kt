package org.eurofurence.connavigator.ui.fragments

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.LoginReceiver
import org.eurofurence.connavigator.broadcast.LogoutReceiver
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.appcompat.v7.toolbar
import org.joda.time.DateTime

/**
 * Created by requinard on 6/26/17.
 */
class LoginActivity : AppCompatActivity(), AnkoLogger {
    val ui by lazy { LoginUi() }

    private val loginReceiver = localReceiver(LoginReceiver.LOGIN_RESULT) {
        val success = it.booleans["success"]

        info { "Received broadcast from LoginReceiver" }

        runOnUiThread {
            if (success) {
                info { "Login was success! Closing activity" }
                longToast(getString(R.string.login_logged_in_as, AuthPreferences.username))
                finish()
            } else {
                info { "Login failed! Showing error message" }
                longToast(getString(R.string.login_failed))
                ui.errorText.visibility = View.VISIBLE
                ui.layoutMain.visibility = View.VISIBLE
                ui.layoutBusy.visibility = View.GONE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Analytics.screen(this, "Login")
        info { "Starting login attempt" }
        ui.setContentView(this)

        ui.submit.setOnClickListener { attemptSubmit() }

        ui.logout.setOnClickListener {
            info { "Logging user out" }
            longToast(getString(R.string.login_logged_out))
            sendBroadcast(intentFor<LogoutReceiver>())
            finish()
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

        sendBroadcast(intentFor<LoginReceiver>(
                LoginReceiver.REGNUMBER to ui.regNumber.text.toString(),
                LoginReceiver.USERNAME to ui.username.text.toString(),
                LoginReceiver.PASSWORD to ui.password.text.toString()
        ))
    }
}

class LoginUi : AnkoComponent<LoginActivity> {
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

    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                lparams(matchParent, wrapContent)

                layoutBusy = verticalLayout {
                    visibility = View.GONE
                    gravity = Gravity.CENTER
                    orientation = LinearLayout.VERTICAL

                    progressBar {
                        lparams(wrapContent, wrapContent)
                    }

                    textView {
                        textResource = R.string.login_authenticating
                        padding = 20
                        gravity = Gravity.CENTER

                        lparams(matchParent, wrapContent)
                    }

                    lparams(matchParent, matchParent)
                }

                layoutMain = verticalLayout {

                    toolbar {
                        titleResource = R.string.login
                        lparams(matchParent, wrapContent)
                        backgroundResource = R.color.primary
                        setTitleTextAppearance(ctx, android.R.style.TextAppearance_Medium_Inverse)
                    }

                    // If not logged in
                    verticalLayout {
                        visibility = if (AuthPreferences.isLoggedIn()) View.GONE else View.VISIBLE

                        lparams(matchParent, matchParent)

                        username = editText {
                            hintResource = R.string.login_username
                            inputType = InputType.TYPE_CLASS_TEXT
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        regNumber = editText {
                            hintResource = R.string.login_reg_number
                            inputType = InputType.TYPE_CLASS_NUMBER

                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        password = editText {
                            hintResource = R.string.login_password
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        errorText = textView {
                            textResource = R.string.login_unsuccessful
                            visibility = View.GONE
                            textColor = ContextCompat.getColor(ctx, R.color.primary)
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        textView {
                            textResource = R.string.login_information
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        submit = button {
                            textResource = R.string.login
                            background.setColorFilter(ContextCompat.getColor(context, R.color.primaryDark), PorterDuff.Mode.SRC)
                            textColor = ContextCompat.getColor(context, R.color.textWhite)

                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        moreInformation = button {
                            textResource = R.string.misc_more_information
                            lparams(matchParent, wrapContent) {
                                setMargins(dip(16), 0, dip(16), dip(16))
                            }
                        }
                    }

                    // If logged in
                    verticalLayout {
                        visibility = if (AuthPreferences.isLoggedIn()) View.VISIBLE else View.GONE

                        textView {
                            text = resources.getString(R.string.login_logged_in_as, AuthPreferences.username)
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }
                        textView {
                            text = resources.getString(R.string.login_valid_until, DateTime(AuthPreferences.tokenValidUntil).toLocalDateTime())
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }

                        logout = button {
                            textResource = R.string.login_logout
                            lparams(matchParent, wrapContent) {
                                margin = dip(16)
                            }
                        }
                    }

                    lparams(matchParent, matchParent)
                }
            }
        }.also {
            initialized = true
        }
    }
}