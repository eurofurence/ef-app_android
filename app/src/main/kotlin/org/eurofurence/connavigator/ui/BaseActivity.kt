package org.eurofurence.connavigator.ui

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import org.eurofurence.connavigator.MainEventFragment
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.db.DBService
import roboguice.activity.RoboActionBarActivity

abstract class BaseActivity() : RoboActionBarActivity(), NavigationView.OnNavigationItemSelectedListener, MainEventFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri?) {
        println(uri)
    }

    val dbservice = DBService(this)

    protected lateinit var navDays: TextView
    protected lateinit var navTitle: TextView
    protected lateinit var navSubtitle: TextView

    /* Inserts the navigation menu and the top bar
     *
     */
    fun injectNavigation(savedBundleInstance: Bundle?) {
        // Find and configure the toolbar
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        // Find and populate the main layout manager
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        // Find navigation drawer and add this activity as a listener
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        // Resolve the header in the navigation drawer, bind the views
        val header = navigationView.getHeaderView(0)
        navDays = header.findViewById(R.id.navDays)as TextView
        navTitle = header.findViewById(R.id.navTitle)as TextView
        navSubtitle = header.findViewById(R.id.navSubtitle)as TextView
    }
}