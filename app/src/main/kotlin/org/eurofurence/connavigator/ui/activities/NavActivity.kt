package org.eurofurence.connavigator.ui.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.ResetReceiver
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.database.dispatchUpdate
import org.eurofurence.connavigator.pref.RemotePreferences
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout
import org.joda.time.DateTime
import org.joda.time.Days

class NavActivity : AppCompatActivity(), AnkoLogger {
    internal val ui = NavUi()

    val navFragment by lazy { NavHostFragment.create(R.navigation.nav_graph) }
    val navController by lazy { navFragment.findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        info { "Starting Nav Activity" }

        ui.setContentView(this)

        supportFragmentManager.beginTransaction()
                .replace(R.id.nav_graph, navFragment)
                .setPrimaryNavigationFragment(navFragment)
                .commit()


        ui.navTitle.text = RemotePreferences.eventTitle
        ui.navSubtitle.text = RemotePreferences.eventSubTitle

        // Calculate the days between, using the current time.
        val firstDay = DateTime(RemotePreferences.nextConStart)
        val days = Days.daysBetween(DateTime.now(), DateTime(firstDay)).days

        // On con vs. before con. This should be updated on day changes
        if (days <= 0)
            ui.navDays.text = getString(R.string.misc_current_day, 1 - days)
        else
            ui.navDays.text = getString(R.string.misc_days_left, days)

        info { "Inserted Nav Fragment" }
    }

    override fun onResume() {
        info { "setting up nav toolbar" }

        val appbarConfig = AppBarConfiguration(navController.graph, ui.drawer)

        ui.bar.setupWithNavController(navController, appbarConfig)

        ui.nav.setNavigationItemSelectedListener {
            ui.drawer.closeDrawers()
            onOptionsItemSelected(it)
        }

        super.onResume()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        info { "Selecting item" }
        return when (item.itemId) {
            R.id.navDevReload -> UpdateIntentService().dispatchUpdate(this, true).let { true }
            R.id.navDevClear -> alert(getString(R.string.clear_app_cache), getString(R.string.clear_database)) {
                yesButton { ResetReceiver().clearData(this@NavActivity) }
                noButton { }
            }.show().let { true }
            R.id.navWebSite -> browse("https://eurofurence.org")
            R.id.navWebTwitter -> browse("https://twitter.com/eurofurence")
            R.id.navFursuitGames -> browse("https://app.eurofurence.org/collectemall/")
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }


}

internal class NavUi : AnkoComponent<NavActivity> {
    lateinit var bar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var nav: NavigationView
    lateinit var header: View

    val navDays get() = header.findViewById<TextView>(R.id.navDays)
    val navTitle get() = header.findViewById<TextView>(R.id.navTitle)
    val navSubtitle get() = header.findViewById<TextView>(R.id.navSubTitle)

    override fun createView(ui: AnkoContext<NavActivity>) = with(ui) {
        drawerLayout {
            drawer = this
            frameLayout {
                backgroundResource = R.color.backgroundGrey
                verticalLayout {
                    bar = toolbar() {
                        backgroundResource = R.color.primaryDark
                        setTitleTextColor(ContextCompat.getColor(context, R.color.textWhite))
                        id = R.id.toolbar
                    }

                    linearLayout {
                        id = R.id.nav_graph
                    }
                }
            }.lparams(matchParent, matchParent)

            nav = navigationView {
                header = inflateHeaderView(R.layout.layout_nav_header)
                inflateMenu(R.menu.nav_drawer)
            }.lparams(wrapContent, matchParent) {
                gravity = Gravity.START
            }
        }
    }
}