package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import org.eurofurence.connavigator.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout

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
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }


}

internal class NavUi : AnkoComponent<NavActivity> {
    lateinit var bar: Toolbar
    lateinit var drawer: DrawerLayout
    lateinit var nav: NavigationView
    override fun createView(ui: AnkoContext<NavActivity>) = with(ui) {
        drawerLayout {
            drawer = this
            frameLayout {
                backgroundResource = R.color.backgroundGrey
                verticalLayout {
                    bar = toolbar() {
                        backgroundResource = R.color.primaryDark
                        setTitleTextColor(ContextCompat.getColor(context, R.color.textWhite))
                    }

                    linearLayout {
                        id = R.id.nav_graph
                    }
                }
            }.lparams(matchParent, matchParent)

            nav = navigationView {
                inflateHeaderView(R.layout.layout_nav_header)
                inflateMenu(R.menu.nav_drawer)
            }.lparams(wrapContent, matchParent) {
                gravity = Gravity.START
            }
        }
    }

}