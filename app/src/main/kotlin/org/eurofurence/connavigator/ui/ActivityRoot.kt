package org.eurofurence.connavigator.ui

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.swagger.client.model.*
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.R.id
import org.eurofurence.connavigator.broadcast.ResetReceiver
import org.eurofurence.connavigator.database.*
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.BackgroundPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.RootAPI
import org.eurofurence.connavigator.util.delegators.header
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnContent
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.objects
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.withArguments
import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

class ActivityRoot : AppCompatActivity(), RootAPI, SharedPreferences.OnSharedPreferenceChangeListener, HasDb, AnkoLogger {
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        debug { "Updating content data after preference change" }

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

    private var currentMode: ActionBarMode = ActionBarMode.HOME

    // Views in navigation view
    val navView: NavigationView by view()
    val navDays: TextView by header({ navView })
    val navTitle: TextView by header({ navView })
    val navSubtitle: TextView by header({ navView })


    // See if we're on the home screen. Used to check the back button
    private val onHome get() = supportFragmentManager.findFragmentByTag("content") is FragmentViewHome

    var subscriptions = Disposables.empty()

    /**
     * Listens to update responses, since the event recycler holds database related data
     */
    private val updateReceiver = localReceiver(UpdateIntentService.UPDATE_COMPLETE) {
        // Get intent extras
        val success = it.booleans["success"]
        val time = it.objects["time", Date::class.java]

        info { "Received UPDATE_COMPLETE notification. Success: $success; Time: $time" }

        if (success) {
            db.observer.onNext(db)
        }
    }

    /**
     * Use a bound value instead of the specification.
     */
    private val updateCompleteMsg by updateComplete


    override fun makeSnackbar(text: String) {
        Snackbar.make(findViewById(R.id.content)!!, text, Snackbar.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Stop the rotation
        if (!RemotePreferences.rotationEnabled) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        // Assign the layout
        setContentView(R.layout.activity_root)

        setupBar()
        setupBarNavLink()
        setupNav()

        subscriptions += RemotePreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateNavCountdown()
                }

        subscriptions += AnalyticsPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Analytics.updateSettings()
                }

        // Show the home screen
        if (savedInstanceState == null || !savedInstanceState.getBoolean("hasContent"))
            setupContent()

        // Show our browsing intent
        handleBrowsingIntent()

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)

        // Restore fragments from saved instance state.
        savedInstanceState?.also {
            setActionBarMode(ActionBarMode.valueOf(it.getString("currentMode")))

            // Get flags of fragment presence in bundle and existing fragments.
            val contentRestorable = it.getBoolean("hasContent")
            val contentPresent = supportFragmentManager.findFragmentByTag("content")
            val contentSubRestorable = it.getBoolean("hasContentSub")
            val contentSubPresent = supportFragmentManager.findFragmentByTag("contentSub")

            // Content item is the existing fragment or the one that is restorable from saved state.
            val content = contentPresent ?: if (contentRestorable)
                supportFragmentManager.getFragment(savedInstanceState, "content")
            else
                null

            // Details item is the existing fragment or the one that is restorable from saved state.
            val contentSub = contentSubPresent ?: if (contentSubRestorable)
                supportFragmentManager.getFragment(savedInstanceState, "contentSub")
            else
                null

            // If content is to be restored, replace the content view with the one given.
            if (content != null) {
                // Assert a clean back stack, we are re-adding anyways.
                supportFragmentManager.popBackStackImmediate("contentSubAdded", FragmentManager.POP_BACK_STACK_INCLUSIVE)

                // Insert the main content view (can already be the one present.
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.content, content, "content")
                        .commitAllowingStateLoss()

                // Set nav drawer item if nav represented content fragment.
                (content as? NavRepresented)?.let {
                    navView.setCheckedItem(it.drawerItemId)
                }


                // If details present, add those.
                if (contentSub != null)
                    supportFragmentManager
                            .beginTransaction()
                            .addToBackStack("contentSubAdded")
                            .add(R.id.content, contentSub, "contentSub")
                            .commitAllowingStateLoss()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    /**
     * Reacts to the intent ACTION_VIEW
     * Returns true if we have managed to navigate, false if not
     */
    private fun handleBrowsingIntent(): Boolean {
        if (intent.action == Intent.ACTION_VIEW) {

            debug { intent.dataString }
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

        AuthPreferences.validate()

        if (!RemotePreferences.autoUpdateDisabled)
            dispatchUpdate(this)
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
        } else if (!onHome) {
            navigateRoot(FragmentViewHome::class.java, ActionBarMode.HOME)
        } else if (BackgroundPreferences.closeAppImmediately) {
            super.onBackPressed()
        } else {
            alert("Are you sure you want to close the app?\n\n(You'll still receive notifications for announcements and personal messages when the app is closed.)", "Close Application?") {
                customView {
                    verticalLayout {
                        checkBox("Remember this choice") {
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                            textColor = ContextCompat.getColor(context, R.color.textBlack)
                            setOnCheckedChangeListener { _, value -> BackgroundPreferences.closeAppImmediately = value }
                        }.lparams(matchParent, wrapContent) {
                            margin = dip(20)
                        }
                    }
                }
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

        // Restore mode
        setActionBarMode(currentMode)
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

        // Find existing content fragment.
        val content = supportFragmentManager.findFragmentByTag("content")

        // If not already there, navigate with fragment transaction
        if (!type.isInstance(content)) {
            // Pop existing details.
            popDetails()

            val inst = type.newInstance()

            // Set nav drawer item if nav represented content fragment.
            (inst as? NavRepresented)?.let {
                navView.setCheckedItem(it.drawerItemId)
            }

            // Add content to content view.
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_fade_in, R.anim.abc_fade_out)
                    .replace(R.id.content, inst, "content")
                    .commitAllowingStateLoss()
        }

    }

    private fun setActionBarMode(mode: ActionBarMode) {
        if (listOf(ActionBarMode.TABS, ActionBarMode.SEARCHTABS, ActionBarMode.SEARCHTABSFILTER).contains(mode)) {
            tabs.visibility = View.VISIBLE
        } else {
            tabs.visibility = View.GONE
        }

        // Show the search button
        menu?.findItem(R.id.action_search)?.isVisible = listOf(ActionBarMode.SEARCH, ActionBarMode.SEARCHTABS, ActionBarMode.SEARCHMAP, ActionBarMode.SEARCHTABSFILTER).contains(mode)

        // Show map button
        menu?.findItem(R.id.action_map)?.isVisible = (mode == ActionBarMode.MAP || mode == ActionBarMode.SEARCHMAP)

        menu?.findItem(R.id.action_filter)?.isVisible = mode == ActionBarMode.SEARCHTABSFILTER

        currentMode = mode

        info { "Action bar mode is now $mode" }
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
                R.id.navDevReload -> dispatchUpdate(this, showToastOnCompletion = true)
                R.id.navDevSettings -> handleSettings()
                R.id.navDevClear -> {
                    alert("Empty app cache. You WILL need an internet connection to restart", "Clear database") {
                        yesButton { ResetReceiver().clearData(this@ActivityRoot) }
                        noButton { longToast("Not clearing DB") }
                    }.show()
                }
            }


            // Close drawer and return the result
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun updateNavCountdown() {
        // Set up dates to EF
        // Manually set the first date, since the database is not updated with EF 22
        val firstDay = DateTime(RemotePreferences.nextConStart)

        // Calculate the days between, using the current time. Todo: timezones
        val days = Days.daysBetween(DateTime.now(), DateTime(firstDay)).days

        if (!RemotePreferences.mapsEnabled) {
            navView.menu.findItem(id.navMap).isVisible = false
        }

        navTitle.text = RemotePreferences.eventTitle
        navSubtitle.text = RemotePreferences.eventSubTitle
        // On con vs. before con. This should be updated on day changes
        if (days <= 0)
            navDays.text = "Day ${1 - days}"
        else
            navDays.text = "Only $days days left!"
    }

    private fun <T : Fragment> navigateIfLoggedIn(fragment: Class<T>, mode: ActionBarMode = ActionBarMode.NONE): Boolean {
        return if (AuthPreferences.isLoggedIn()) {
            navigateRoot(fragment, mode)
            true
        } else {
            longToast("You need to login before you can see private messages!")
            false
        }
    }

    private var stateSaved = false

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("currentMode", currentMode.toString())

        supportFragmentManager.findFragmentByTag("content")?.let { content ->
            outState.putBoolean("hasContent", true)
            supportFragmentManager.putFragment(outState, "content", content)
        }

        supportFragmentManager.findFragmentByTag("contentSub")?.let { contentSub ->
            outState.putBoolean("hasContentSub", true)
            supportFragmentManager.putFragment(outState, "contentSub", contentSub)
        }

        stateSaved = true
    }

    override fun navigateToEvent(event: EventRecord) {
        navigateToSubFragment(FragmentViewEvent().withArguments(
                "id" to event.id.toString()
        ))
    }

    override fun navigateToKnowledgeEntry(knowledgeEntry: KnowledgeEntryRecord) {
        navigateToSubFragment(FragmentViewInfo().withArguments(knowledgeEntry))
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

    private fun navigateToSubFragment(fragment: Fragment) {
        // Pop existing details.
        popDetails()

        // Add content to content view.
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.in_slide_and_fade, R.anim.out_slide_and_fade, R.anim.in_slide_and_fade, R.anim.out_slide_and_fade)
                .addToBackStack("contentSubAdded")
                .add(R.id.content, fragment, "contentSub")
                .commit()
    }

    override fun popDetails() {
        // Pop the backstack including the details fragment, unless state is already saved.
        supportFragmentManager
                .takeUnless { stateSaved }
                ?.popBackStack("contentSubAdded", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    override val db by lazyLocateDb()

    private fun handleSettings() {
        info { "Starting settings activity" }
        intent = Intent(this, ActivitySettings::class.java)
        startActivity(intent)
    }

    override fun changeTheme(newTheme: Int) {
        setTheme(newTheme)
    }
}