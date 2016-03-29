package org.eurofurence.connavigator.ui

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.TextView
import org.eurofurence.connavigator.MainEventFragment
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.db.DBService
import org.eurofurence.connavigator.util.android.headerView
import org.eurofurence.connavigator.util.android.view

abstract class BaseActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainEventFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri?) {
        println(uri)
    }

    val dbService = DBService(this)

    val eventRecycler: RecyclerView by view(RecyclerView::class.java)
    val navView: NavigationView by view(NavigationView::class.java)
    val navDays: TextView by headerView(TextView::class.java, { navView })
    val navTitle: TextView by headerView(TextView::class.java, { navView })
    val navSubtitle: TextView by headerView(TextView::class.java, { navView })

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
        navView.setNavigationItemSelectedListener(this)
    }
}