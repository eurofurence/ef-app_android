package org.eurofurence.connavigator.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import io.swagger.client.model.Dealer
import io.swagger.client.model.EventEntry
import io.swagger.client.model.Info
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI
import org.eurofurence.connavigator.util.delegators.header
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

class ActivityRoot : AppCompatActivity(), RootAPI, SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        logd { "Updating content data after preference change" }

        if (BuildConfig.DEBUG) {
            Analytics.event(Analytics.Category.SETTINGS, Analytics.Action.CHANGED, key)
        }

        applyOnContent { dataUpdated() }
    }

    override fun changeTitle(text: String) {
        supportActionBar!!.title = text
    }

    // Menu
    var menu: Menu? = null

    // Views
    val toolbar: Toolbar by view()
    override val tabs: TabLayout by view()
    val drawer: DrawerLayout by view()
    val fab: FloatingActionButton by view()

    // Views in navigation view
    val navView: NavigationView by view()
    val navDays: TextView by header({ navView })
    val navTitle: TextView by header({ navView })
    val navSubtitle: TextView by header({ navView })

    // Content API aggregator
    var content: Set<ContentAPI> = setOf()

    /**
     * Listens to update responses, since the event recycler holds database related data
     */
    val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        // Get intent extras
        val success = it.booleans["success"]
        val time = it.objects["time", Date::class.java]


        if (!success) {
            // Make a snackbar for the result
            makeSnackbar("Database reload ${if (success) "successful" else "failed"}, version $time")

            // Update content data if fragments implement content API
            applyOnContent {
                logv { "Updated the data and dispatching to $this" }
                dataUpdated()
            }
        }
    }

    override fun makeSnackbar(text: String) {
        Snackbar.make(findViewById(R.id.content)!!, text, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Stop the rotation
        if (RemotePreferences.rotationEnabled == false) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        // Assign the layout
        setContentView(R.layout.activity_root)

        setupBar()
        setupBarNavLink()
        setupNav()
        setupFab()

        if (!handleBrowsingIntent()) {
            setupContent()
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
    }

    /**
     * Reacts to the intent ACTION_VIEW
     * Returns true if we have managed to navigate, false if not
     */
    private fun handleBrowsingIntent(): Boolean {
        if (intent.action == Intent.ACTION_VIEW) {

            logd { intent.dataString }
            when {
            // Handle event links
                intent.dataString.contains("/event/") -> {
                    val uuid = intent.data.lastPathSegment
                    val eventValue = database.eventEntryDb[UUID.fromString(uuid)]
                    if (eventValue != null) {
                        Analytics.event(Analytics.Category.EVENT, Analytics.Action.INCOMING, eventValue.title)
                        navigateToEvent(eventValue)
                        return true
                    } else {
                        makeSnackbar("I'm sorry, we didn't find any event!")
                    }
                }

            // Handle info links
                intent.dataString.contains("/info/") -> {
                    val uuid = intent.data.lastPathSegment
                    val infoValue = database.infoDb[UUID.fromString(uuid)]
                    if (infoValue != null) {
                        Analytics.event(Analytics.Category.INFO, Analytics.Action.INCOMING, infoValue.title)
                        navigateToInfo(infoValue)
                        return true
                    } else {
                        makeSnackbar("I'm sorry, but we didn't find any info!")
                    }
                }

            // Handle dealer links
                intent.dataString.contains("/dealer/") -> {
                    val uuid = intent.data.lastPathSegment
                    val dealerValue = database.dealerDb[UUID.fromString(uuid)]
                    if (dealerValue != null) {
                        Analytics.event(Analytics.Category.DEALER, Analytics.Action.INCOMING, dealerValue.attendeeNickname)
                        navigateToDealer(dealerValue)
                        return true
                    } else {
                        makeSnackbar("I'm sorry, but we didn't find any dealer!")
                    }
                }
            }
        }
        return false
    }

    private fun setupContent() =
            navigateRoot(FragmentViewHome::class.java)

    override fun onResume() {
        super.onResume()
        updateReceiver.register()
    }

    override fun onPause() {
        updateReceiver.unregister()
        super.onPause()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options
        menuInflater.inflate(R.menu.action_bar, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Propagate the ID based selection to functions
        return when (item.itemId) {
            R.id.action_settings -> handleSettings().let { true }
            R.id.action_bug_report -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/forms/9qI2iFBwAa"))).let { true }
            R.id.action_feedback_report -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://goo.gl/forms/66Q61KsU0G"))).let { true }
            R.id.action_search -> applyOnContent { onSearchButtonClick() }.let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupBar() {
        // Set action bar for support
        setSupportActionBar(toolbar)
    }

    private fun setupBarNavLink() {
        // Connect drawer and toolbar
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun <T : Fragment> navigateRoot(type: Class<T>, mode: ActionBarMode = ActionBarMode.NONE) {
        setActionBarMode(mode)

        // If not already there, navigate with fragment transaction
        if (!type.isInstance(content)) {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .replace(R.id.content, type.newInstance(), "content")
                    .commitAllowingStateLoss()
        }

    }

    private fun setActionBarMode(mode: ActionBarMode) {
        if (listOf(ActionBarMode.TABS, ActionBarMode.SEARCHTABS).contains(mode)) {
            tabs.visibility = View.VISIBLE
        } else {
            tabs.visibility = View.GONE
        }

        // Show the search button
        menu?.findItem(R.id.action_search)?.isVisible = listOf(ActionBarMode.SEARCH, ActionBarMode.SEARCHTABS, ActionBarMode.SEARCHMAP).contains(mode)

        // Show map button
        menu?.findItem(R.id.action_map)?.isVisible = (mode == ActionBarMode.MAP || mode == ActionBarMode.SEARCHMAP)
    }

    private fun setupNav() {
        // Add handler for navigation selection
        navView.setNavigationItemSelectedListener {
            //Handle the ID
            when (it.itemId) {
                R.id.navHome -> navigateRoot(FragmentViewHome::class.java)
                R.id.navEvents -> navigateRoot(FragmentViewEvents::class.java, ActionBarMode.SEARCHTABS)
                R.id.navInfo -> navigateRoot(FragmentViewInfoGroups::class.java)
                R.id.navMaps -> navigateRoot(FragmentViewMaps::class.java, ActionBarMode.TABS)
                R.id.navDealersDen -> navigateRoot(FragmentViewDealers::class.java, ActionBarMode.SEARCH)
                R.id.navAbout -> navigateRoot(FragmentViewAbout::class.java)
                R.id.navWebSite -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eurofurence.org/")))
                R.id.navWebTwitter -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/eurofurence")))
                R.id.navDevReload -> UpdateIntentService.dispatchUpdate(this)
                R.id.navDevSettings -> handleSettings()
                R.id.navDevClear -> {
                    AlertDialog.Builder(ContextThemeWrapper(this, R.style.appcompatDialog))
                            .setTitle("Clearing Database")
                            .setMessage("This will get rid of all cached items you have stored locally. You will need an internet connection to restart!")
                            .setPositiveButton("Clear", { dialogInterface, i -> database.clear(); imageService.clear(); System.exit(0) })
                            .setNegativeButton("Cancel", { dialogInterface, i -> })
                            .show()
                }
            }

            // Close drawer and return the result
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        // Set up dates to EF
        // Manually set the first date, since the database is not updated with EF 22
        val firstDay = DateTime(2017, 8, 16, 0, 0)

        // Calculate the days between, using the current time. Todo: timezones
        val days = Days.daysBetween(DateTime.now(), DateTime(firstDay)).days

        if (!RemotePreferences.mapsEnabled) {
            navView.menu.findItem(R.id.navMap).isVisible = false
        }

        // On con vs. before con. This should be updated on day changes
        if (days <= 0)
            navDays.text = "Day ${1 - days}"
        else
            navDays.text = "Only $days days left!"
    }

    override fun navigateToEvent(eventEntry: EventEntry) {
        navigateToSubFragment(FragmentViewEvent(eventEntry))
    }

    override fun navigateToInfo(info: Info) {
        navigateToSubFragment(FragmentViewInfo(info))
    }


    override fun navigateToDealer(dealer: Dealer) {
        navigateToSubFragment(FragmentViewDealer(dealer))
    }

    private fun navigateToSubFragment(fragment: Fragment) =
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.in_slide_and_fade, R.anim.out_slide_and_fade, R.anim.in_slide_and_fade, R.anim.out_slide_and_fade)
                    .add(R.id.content, fragment, "content")
                    .addToBackStack(null)
                    .commit()

    /**
     * Database is lazily initialized and then provided as part of the root API
     */
    override val database by lazy { Database(this) }

    private fun setupFab() {
        fab.setOnClickListener { view ->
            // Notify the update to the user
            Snackbar.make(findViewById(R.id.content)!!, "Updating the database", Snackbar.LENGTH_LONG).show()

            // Start the update service
            UpdateIntentService.dispatchUpdate(this@ActivityRoot)
        }

        //if (!BuildConfig.DEBUG)
        fab.visibility = View.INVISIBLE
    }

    private fun handleSettings() {
        logv { "Starting settings activity" }
        intent = Intent(this, ActivitySettings::class.java)
        startActivity(intent)
    }

    override fun changeTheme(newTheme: Int) {
        setTheme(newTheme)
    }
}