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
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.FontAwesomeIcons
import io.swagger.client.model.AnnouncementRecord
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.EventRecord
import io.swagger.client.model.KnowledgeEntryRecord
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.ResetReceiver
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.database.updateComplete
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.communication.RootAPI
import org.eurofurence.connavigator.util.delegators.header
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnContent
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.logd
import org.eurofurence.connavigator.util.extensions.logv
import org.eurofurence.connavigator.util.extensions.objects
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.info
import org.jetbrains.anko.longToast
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.withArguments
import org.jetbrains.anko.yesButton
import org.joda.time.DateTime
import org.joda.time.Days
import java.util.Date
import java.util.UUID

class ActivityRoot : AppCompatActivity(), RootAPI, SharedPreferences.OnSharedPreferenceChangeListener, HasDb, AnkoLogger {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        logd { "Updating content data after preference change" }

        if (BuildConfig.DEBUG) {
            Analytics.event(Analytics.Category.SETTINGS, Analytics.Action.CHANGED, key)
        }

        applyOnContent { dataUpdated() }
    }

    override fun changeTitle(text: String) {
        supportActionBar?.title = text
        Analytics.screen(this, text)
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

    // See if we're on the home screen. Used to check the back button
    var onHome = true

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

    /**
     * Use a bound value instead of the specification.
     */
    val updateCompleteMsg by updateComplete


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

        // Show the home screen
        setupContent()

        // Show our browsing intent
        handleBrowsingIntent()

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
                    val eventValue = events[UUID.fromString(uuid)]
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
                    val knowledgeEntryValue = knowledgeEntries[UUID.fromString(uuid)]
                    if (knowledgeEntryValue != null) {
                        Analytics.event(Analytics.Category.INFO, Analytics.Action.INCOMING, knowledgeEntryValue.title)
                        navigateToKnowledgeEntry(knowledgeEntryValue)
                        return true
                    } else {
                        makeSnackbar("I'm sorry, but we didn't find any info!")
                    }
                }

            // Handle dealer links
                intent.dataString.contains("/dealer/") -> {
                    val uuid = intent.data.lastPathSegment
                    val dealerValue = dealers[UUID.fromString(uuid)]
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
            navigateRoot(FragmentViewHome::class.java, ActionBarMode.HOME)

    override fun onResume() {
        super.onResume()
        updateReceiver.register()
        updateCompleteMsg.listen {
            println(it)
        }
    }

    override fun onPause() {
        updateCompleteMsg.unlistenAll()
        updateReceiver.unregister()
        super.onPause()
    }

    override fun onBackPressed() {
        info { "Items on the backstack${fragmentManager.backStackEntryCount}" }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            super.onBackPressed()
        } else if (onHome == false) {
            navigateRoot(FragmentViewHome::class.java, ActionBarMode.HOME)
        } else {
            alert("Are you sure you want to close the app? You'll still receive messages", "Close the app") {
                yesButton { super.onBackPressed() }
                noButton { /* pass */ }
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu options
        menuInflater.inflate(R.menu.action_bar, menu)

        // Set up icons
        menu.apply {
            fun icon(icon: FontAwesomeIcons) = IconDrawable(this@ActivityRoot, icon)
                    .colorRes(R.color.textWhite)
                    .actionBarSize()

            findItem(R.id.action_filter).icon = icon(FontAwesomeIcons.fa_filter)
            findItem(R.id.action_search).icon = icon(FontAwesomeIcons.fa_search)
            findItem(R.id.action_settings).icon = icon(FontAwesomeIcons.fa_cogs)
        }
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
            R.id.action_filter -> applyOnContent { onFilterButtonClick() }.let { true }
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

    override fun <T : Fragment> navigateRoot(type: Class<T>, mode: ActionBarMode) {
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
        if (listOf(ActionBarMode.TABS, ActionBarMode.SEARCHTABS, ActionBarMode.SEARCHTABSFILTER).contains(mode)) {
            tabs.visibility = View.VISIBLE
        } else {
            tabs.visibility = View.GONE
        }

        onHome = mode == ActionBarMode.HOME

        // Show the search button
        menu?.findItem(R.id.action_search)?.isVisible = listOf(ActionBarMode.SEARCH, ActionBarMode.SEARCHTABS, ActionBarMode.SEARCHMAP, ActionBarMode.SEARCHTABSFILTER).contains(mode)

        // Show map button
        menu?.findItem(R.id.action_map)?.isVisible = (mode == ActionBarMode.MAP || mode == ActionBarMode.SEARCHMAP)

        menu?.findItem(R.id.action_filter)?.isVisible = mode == ActionBarMode.SEARCHTABSFILTER
    }

    private fun setupNav() {
        // Add handler for navigation selection
        navView.setNavigationItemSelectedListener {
            //Handle the ID
            when (it.itemId) {
                R.id.navHome -> navigateRoot(FragmentViewHome::class.java, ActionBarMode.HOME)
                R.id.navEvents -> navigateRoot(FragmentViewEvents::class.java, ActionBarMode.SEARCHTABSFILTER)
                R.id.navInfo -> navigateRoot(FragmentViewInfoGroups::class.java)
                R.id.navMaps -> navigateRoot(FragmentViewMaps::class.java, ActionBarMode.TABS)
                R.id.navDealersDen -> navigateRoot(FragmentViewDealers::class.java, ActionBarMode.SEARCH)
                R.id.navAbout -> navigateRoot(FragmentViewAbout::class.java)
                R.id.navLogin -> startActivity<LoginActivity>()
                R.id.navMessages -> navigateIfLoggedIn(FragmentViewMessageList::class.java)
                R.id.navFursuitGames -> {
                    if (RemotePreferences.nativeFursuitGames) {
                        navigateIfLoggedIn(FragmentViewFursuits::class.java, ActionBarMode.TABS)
                    } else {
                        val url = if (AuthPreferences.isLoggedIn()) {
                            "https://app.eurofurence.org/collectemall/#token-${AuthPreferences.token}"
                        } else {
                            "https://app.eurofurence.org/collectemall/#token-empty"
                        }

                        browse(url)
                    }
                }
                R.id.navWebSite -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.eurofurence.org/")))
                R.id.navWebTwitter -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/eurofurence")))
                R.id.navDevReload -> UpdateIntentService.dispatchUpdate(this)
                R.id.navDevSettings -> handleSettings()
                R.id.navDevClear -> {
                    alert("Empty app cache. You WILL need an internet connection to restart", "Clear database") {
                        yesButton { ResetReceiver.fire(this@ActivityRoot) }
                        noButton { longToast("Not clearing DB") }
                    }.show()
                }
            }

            // Close drawer and return the result
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        // Set up dates to EF
        // Manually set the first date, since the database is not updated with EF 22
        val firstDay = DateTime(RemotePreferences.nextConStart)

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

    private fun <T : Fragment> navigateIfLoggedIn(fragment: Class<T>, mode: ActionBarMode = ActionBarMode.NONE): Boolean {
        if (AuthPreferences.isLoggedIn()) {
            navigateRoot(fragment, mode)
            return true
        } else {
            longToast("You need to login before you can see private messages!")
            return false
        }
    }


    override fun navigateToEvent(event: EventRecord) {
        navigateToSubFragment(FragmentViewEvent.onEvent(event))
    }

    override fun navigateToKnowledgeEntry(knowledgeEntry: KnowledgeEntryRecord) {
        navigateToSubFragment(FragmentViewInfo.onKnowledgeEntry(knowledgeEntry))
    }


    override fun navigateToDealer(dealer: DealerRecord) {
        val fragment = FragmentViewDealer().withArguments(
                "id" to dealer.id.toString()
        )

        navigateToSubFragment(fragment)
    }

    override fun navigateToMessage(message: PrivateMessageRecord) {
        val fragment = FragmentViewMessageItem().withArguments(
                "message" to Gson().toJson(message)
        )

        navigateToSubFragment(fragment)
    }

    override fun navigateToAnnouncement(announcementRecord: AnnouncementRecord) {
        val fragment = FragmentViewAnnouncement().withArguments(
                "id" to announcementRecord.id.toString()
        )

        navigateToSubFragment(fragment)
    }

    private fun navigateToSubFragment(fragment: Fragment) =
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.in_slide_and_fade, R.anim.out_slide_and_fade, R.anim.in_slide_and_fade, R.anim.out_slide_and_fade)
                    .add(R.id.content, fragment, "content")
                    .addToBackStack(null)
                    .commit()

    override val db by lazyLocateDb()

    private fun handleSettings() {
        logv { "Starting settings activity" }
        intent = Intent(this, ActivitySettings::class.java)
        startActivity(intent)
    }

    override fun changeTheme(newTheme: Int) {
        setTheme(newTheme)
    }
}