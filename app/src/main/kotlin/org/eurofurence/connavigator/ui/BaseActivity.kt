package org.eurofurence.connavigator.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import org.eurofurence.connavigator.MainEventFragment
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.delegators.viewInHeader

abstract class BaseActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, MainEventFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri?) {
        println(uri)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        // Handle navigation view item clicks here.
        val id = item?.itemId

        if (id == R.id.nav_events) {
            startActivity(Intent(this, LaunchScreenActivity::class.java))
        } else if (id == R.id.nav_info) {
            startActivity(Intent(this, InfoListActivity::class.java))
        } else if (id == R.id.nav_dev_clear) {
            // Clear the database
            Database(this).clear()

            // Notify user and the recycler
            eventRecycler.adapter.notifyDataSetChanged()
            Snackbar.make(findViewById(R.id.fab), "Database cleared", Snackbar.LENGTH_SHORT).show()
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    val eventRecycler  by view(RecyclerView::class.java)
    val navView by view(NavigationView::class.java)
    val navDays by viewInHeader(TextView::class.java, { navView })
    val navTitle by viewInHeader(TextView::class.java, { navView })
    val navSubtitle by viewInHeader(TextView::class.java, { navView })

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