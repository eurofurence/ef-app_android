package org.eurofurence.connavigator.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Browser
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavHost
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.events.ResetReceiver
import org.eurofurence.connavigator.preferences.AnalyticsPreferences
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.preferences.BackgroundPreferences
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.services.AnalyticsService
import org.eurofurence.connavigator.util.DatetimeProxy
import org.eurofurence.connavigator.util.extensions.setFAIcon
import org.eurofurence.connavigator.util.v2.plus
import org.eurofurence.connavigator.workers.DataUpdateWorker
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout
import org.joda.time.DateTime
import org.joda.time.Days

class NavActivity : AppCompatActivity(), NavHost, AnkoLogger, HasDb {
    override fun getNavController() = navFragment.findNavController()

    internal val ui = NavUi()
    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()
    val navFragment by lazy { NavHostFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        info { "Starting Nav Activity" }

        ui.setContentView(this)

        navFragment.setInitialSavedState(savedInstanceState?.getParcelable("fragment"))

        supportFragmentManager.beginTransaction()
                .replace(R.id.nav_graph, navFragment, "navFragment")
                .setPrimaryNavigationFragment(navFragment)
                .runOnCommit {
                    navController.restoreState(savedInstanceState?.getBundle("nav"))
                    navController.setGraph(R.navigation.nav_graph)
                }
                .commit()

        ui.navTitle.text = RemotePreferences.eventTitle
        ui.navSubtitle.text = RemotePreferences.eventSubTitle

        // Calculate the days between, using the current time.
        val firstDay = DateTime(RemotePreferences.nextConStart)
        val days = Days.daysBetween(DatetimeProxy.now(), DateTime(firstDay)).days

        // On con vs. before con. This should be updated on day changes
        if (days <= 0)
            ui.navDays.text = getString(R.string.misc_current_day, 1 - days)
        else
            ui.navDays.text = getString(R.string.misc_days_left, days)

        addNavDrawerIcons()

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
                    AnalyticsService.updateSettings()
                }

        subscriptions += AuthPreferences
                .updated
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateLoginMenuItem()
                }

        subscriptions += BackgroundPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    setMenuPermissions()
                }.apply {
                    // manually apply menu permissions at least once
                    setMenuPermissions()
                }

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData(DataUpdateWorker.TAG)
                .observe(this, androidx.lifecycle.Observer { workInfo ->
                    if (workInfo != null && workInfo.all { it.state == WorkInfo.State.SUCCEEDED }) {
                        db.observer.onNext(db)
                    }
                })

        info { "Inserted Nav Fragment" }
    }

    private fun setMenuPermissions() {
        if (!BackgroundPreferences.hasLoadedOnce) {
            ui.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            ui.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    private fun updateNavCountdown() {
        // Set up dates to EF
        // Manually set the first date, since the database is not updated with EF 22
        val firstDay = DateTime(RemotePreferences.nextConStart)

        // Calculate the days between, using the current time. Todo: timezones
        val days = Days.daysBetween(DatetimeProxy.now(), DateTime(firstDay)).days

        ui.navTitle.text = RemotePreferences.eventTitle
        ui.navSubtitle.text = RemotePreferences.eventSubTitle
        // On con vs. before con. This should be updated on day changes
        if (days <= 0)
            ui.navDays.text = "Day ${1 - days}"
        else
            ui.navDays.text = "Only $days days left!"
    }

    private fun updateLoginMenuItem() {
        // Find login item, assign new text.
        ui.nav.menu.findItem(R.id.navLogin)?.let {
            if (AuthPreferences.isLoggedIn)
                it.title = "Login details"
            else
                it.title = "Login"
        }
    }

    private fun addNavDrawerIcons() {
        ui.nav.inflateMenu(R.menu.nav_drawer)

        ui.nav.menu.apply {
            // Main
            this.setFAIcon(this@NavActivity, R.id.navHome, R.string.fa_home_solid)
            this.setFAIcon(this@NavActivity, R.id.navInfoList, R.string.fa_info_solid)
            this.setFAIcon(this@NavActivity, R.id.navEventList, R.string.fa_calendar)
            this.setFAIcon(this@NavActivity, R.id.navDealerList, R.string.fa_shopping_cart_solid)
            this.setFAIcon(this@NavActivity, R.id.navMapList, R.string.fa_map)

            // Personal
            this.setFAIcon(this@NavActivity, R.id.navLogin, R.string.fa_user_circle)
            this.setFAIcon(this@NavActivity, R.id.navMessages, R.string.fa_envelope)
            this.setFAIcon(this@NavActivity, R.id.navFursuitGames, R.string.fa_paw_solid)
            this.setFAIcon(this@NavActivity, R.id.navAdditionalServices, R.string.fa_book_open_solid)

            // Web
            this.setFAIcon(this@NavActivity, R.id.navWebTwitter, R.string.fa_twitter, isBrand = true)
            this.setFAIcon(this@NavActivity, R.id.navWebSite, R.string.fa_globe_solid)

            // App Management
            this.setFAIcon(this@NavActivity, R.id.navDevReload, R.string.fa_cloud_download_alt_solid)
            this.setFAIcon(this@NavActivity, R.id.navDevClear, R.string.fa_trash_solid)
            this.setFAIcon(this@NavActivity, R.id.navSettings, R.string.fa_cog_solid)
            this.setFAIcon(this@NavActivity, R.id.navAbout, R.string.fa_hands_helping_solid)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        info { "Selecting item" }

        // Exit if we're either UNINITIALIZED or when there is no data. Make an exception for the settings and update action
        if (!BackgroundPreferences.hasLoadedOnce && !listOf(R.id.navSettings, R.id.navDevReload).contains(item.itemId)
        ) {
            longToast("Please wait until we've completed our initial fetch of data")
            return true
        }

        return when (item.itemId) {
            R.id.navDevReload -> DataUpdateWorker.execute(this, true).let { true }
            R.id.navDevClear -> alert(getString(R.string.clear_app_cache), getString(R.string.clear_database)) {
                yesButton { ResetReceiver().clearData(this@NavActivity) }
                noButton { }
            }.show().let { true }
            R.id.navMessages -> navigateToMessages()
            R.id.navWebSite -> browse("https://eurofurence.org")
            R.id.navWebTwitter -> browse("https://twitter.com/eurofurence")
            R.id.navFursuitGames -> navigateToFursuitGames()
            R.id.navAdditionalServices -> navigateToAdditionalServices()
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle("nav", navController.saveState())
        outState.putParcelable("fragment", supportFragmentManager.saveFragmentInstanceState(navFragment))
        super.onSaveInstanceState(outState)
    }

    private fun navigateToMessages(): Boolean {
        if (AuthPreferences.isLoggedIn) {
            navController.navigate(R.id.navMessageList)
        } else {
            longToast("You are not logged in yet!")
            navController.navigate(R.id.navLogin)
        }
        return true
    }

    private fun navigateToFursuitGames(): Boolean {
        val url = "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/companion/#/login?embedded=false&returnPath=/collect&token=${AuthPreferences.tokenOrEmpty()}"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            putExtra(Browser.EXTRA_APPLICATION_ID, BuildConfig.CONVENTION_IDENTIFIER)
        }

        startActivity(intent)

        return true
    }

    private fun navigateToAdditionalServices(): Boolean {
        val url = "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/companion/#/login?embedded=false&returnPath=/&token=${AuthPreferences.tokenOrEmpty()}"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            putExtra(Browser.EXTRA_APPLICATION_ID, BuildConfig.CONVENTION_IDENTIFIER)
        }

        startActivity(intent)

        return true
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
                    bar = toolbar {
                        backgroundResource = R.color.primaryDark
                        setTitleTextColor(ContextCompat.getColor(context, R.color.textWhite))
                        setSubtitleTextColor(ContextCompat.getColor(context, R.color.textWhite))
                        id = R.id.toolbar
                    }

                    frameLayout() {
                        id = R.id.nav_graph
                    }.lparams(matchParent, matchParent)
                }
            }.lparams(matchParent, matchParent)

            nav = navigationView {
                header = inflateHeaderView(R.layout.layout_nav_header)
            }.lparams(wrapContent, matchParent) {
                gravity = Gravity.START
            }
        }
    }
}