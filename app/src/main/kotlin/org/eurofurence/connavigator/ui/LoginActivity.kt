package org.eurofurence.connavigator.ui

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.Button
import android.widget.TextView
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.tracking.Analytics
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar

/**
 * Created by requinard on 6/26/17.
 */
class LoginActivity : AppCompatActivity(), AnkoLogger {
    val ui by lazy { LoginUi() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Analytics.screen(this, "Login")
        info { "Starting login attempt" }
        ui.setContentView(this)

        ui.submit.setOnClickListener { attemptSubmit() }
    }

    /**
     * Validates the fields from the UI
     */
    private fun attemptSubmit(){
        val emptyText = "This field is not supposed to be empty!"

        if(ui.regNumber.text.isEmpty()){
            ui.regNumber.error = emptyText
            return
        }

        if(ui.username.text.isEmpty()){
            ui.username.error = emptyText
            return
        }

        if(ui.password.text.isEmpty()){
            ui.password.error = emptyText
            return
        }
    }
}

class LoginUi : AnkoComponent<LoginActivity> {
    lateinit var username: TextView
    lateinit var regNumber: TextView
    lateinit var password: TextView
    lateinit var submit: Button

    override fun createView(ui: AnkoContext<LoginActivity>) = with(ui) {
        verticalLayout {
            toolbar {
                title = "Login"
                lparams(matchParent, wrapContent)
                backgroundResource = R.color.primary
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setTitleTextColor(ctx.getColor(R.color.textWhite))
                }
            }

            lparams(matchParent, matchParent)

            regNumber = editText {
                hint = "Registration number"
                inputType = InputType.TYPE_CLASS_NUMBER
                lparams(matchParent, wrapContent)
            }

            username = editText {
                hint = "Username"
                lparams(matchParent, wrapContent)
            }

            password = editText {
                hint = "Password"
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                lparams(matchParent, wrapContent)
            }

            submit = button {
                text = "Submit"
                lparams(matchParent, wrapContent)
            }
        }
    }

}