package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import org.eurofurence.connavigator.R
import org.jetbrains.anko.*

class NavActivity : AppCompatActivity() {
    internal val ui = NavUi()

    val navFragment by lazy { NavHostFragment.create(R.navigation.nav_graph) }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        ui.setContentView(this)

        supportFragmentManager.beginTransaction()
                .replace(1, navFragment)
                .setPrimaryNavigationFragment(navFragment)
                .commit()
    }
}

internal class NavUi : AnkoComponent<NavActivity> {
    override fun createView(ui: AnkoContext<NavActivity>) = with(ui) {
        verticalLayout {
            linearLayout {
                id = 1
            }
        }
    }

}