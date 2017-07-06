package org.eurofurence.connavigator.ui

import android.os.Build
import android.os.Bundle
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
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.joda.time.DateTime

/**
 * Created by requinard on 6/26/17.
 */
class LoginActivity : AppCompatActivity(), AnkoLogger {
    val ui by lazy { LoginUi() }

    val loginReceiver = localReceiver(LoginReceiver.LOGIN_RESULT) {
        val success = it.booleans["success"]

        info { "Received broadast from LoginReceiver" }

        runOnUiThread {
            if (success) {
                info { "Login was success! Closing activity" }
                longToast("Logged in as ${AuthPreferences.username}")
                finish()
            } else {
                info { "Login failed! Showing error message" }
                longToast("Failed to log you in!")
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
            longToast("Logging you out, goodbye!")
            AuthPreferences.logout()
            finish()
        }

        loginReceiver.register()
    }

    /**
     * Validates the fields from the UI
     */
    private fun attemptSubmit() {
        val emptyText = "This field is not supposed to be empty!"

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
    lateinit var layoutMain: LinearLayout
    lateinit var layoutBusy: LinearLayout

    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        linearLayout {
            lparams(matchParent, matchParent)

            layoutBusy = verticalLayout {
                visibility = View.GONE
                gravity = Gravity.CENTER
                orientation = LinearLayout.VERTICAL

                progressBar {
                    lparams(wrapContent, wrapContent)
                }

                textView("Authenticating...") {
                    padding = 20
                    gravity = Gravity.CENTER

                    lparams(matchParent, wrapContent)
                }

                lparams(matchParent, matchParent)
            }

            layoutMain = verticalLayout {

                // If not logged in
                verticalLayout {
                    visibility = if (AuthPreferences.isLoggedIn()) View.GONE else View.VISIBLE

                toolbar {
                    title = "Login"
                    lparams(matchParent, wrapContent)
                    backgroundResource = R.color.primary
                    setTitleTextAppearance(ctx, android.R.style.TextAppearance_Medium_Inverse)
                }

                    lparams(matchParent, matchParent)

                    regNumber = editText {
                        hint = "Registration number"
                        inputType = InputType.TYPE_CLASS_NUMBER

                        lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }
                    }

                    username = editText {
                        hint = "Username"
                        lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }
                    }

                    password = editText {
                        hint = "Password"
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }
                    }

                    errorText = textView("Your login was unsuccessful, are you sure you entered the correct data?") {
                        visibility = View.GONE
                        textColor = ctx.getColor(R.color.primary)
                        lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }
                    }

                    textView("Your login credentials for the app are the same as for the Eurofurence Registration System.\n\nIf you do not remember or have access to them, you can ask for a PIN code to sign in at the ConOps or Security office.") {
                        lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }
                    }

                    submit = button {
                        text = "Login"
                        lparams(matchParent, wrapContent) {
                            margin = dip(16)
                        }
                    }
                }

                // If logged in
                verticalLayout {
                    visibility = if (AuthPreferences.isLoggedIn()) View.VISIBLE else View.GONE

                    textView("Username: ${AuthPreferences.username}")
                    textView("Login is valid until: ${DateTime(AuthPreferences.tokenValidUntil).toLocalDateTime()}")

                    logout = button("Logout")
                }
                lparams(matchParent, matchParent)
            }

        }
    }

}