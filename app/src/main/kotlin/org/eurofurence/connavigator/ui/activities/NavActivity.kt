package org.eurofurence.connavigator.ui.activities

import android.content.Intent
import android.content.pm.ActivityInfo
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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.ResetReceiver
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.UpdateIntentService
import org.eurofurence.connavigator.database.dispatchUpdate
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.extensions.booleans
import org.eurofurence.connavigator.util.extensions.localReceiver
import org.eurofurence.connavigator.util.extensions.objects
import org.eurofurence.connavigator.util.extensions.setFAIcon
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout
import org.joda.time.DateTime
import org.joda.time.Days
import java.util.*

class NavActivity : AppCompatActivity(), AnkoLogger, HasDb {
    internal val ui = NavUi()
    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()
    val navFragment by lazy { NavHostFragment.create(R.navigation.nav_graph) }
    val navController by lazy { navFragment.findNavController() }

    override fun onPause() {
        updateReceiver.unregister()
        super.onPause()
    }


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        info { "Starting Nav Activity" }

        // Stop the rotation
        if (!RemotePreferences.rotationEnabled) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

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
                    Analytics.updateSettings()
                }

        subscriptions += AuthPreferences
                .observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    updateLoginMenuItem()
                }

        updateReceiver.register()

        info { "Inserted Nav Fragment" }
    }

    private fun updateNavCountdown() {
        // Set up dates to EF
        // Manually set the first date, since the database is not updated with EF 22
        val firstDay = DateTime(RemotePreferences.nextConStart)

        // Calculate the days between, using the current time. Todo: timezones
        val days = Days.daysBetween(DateTime.now(), DateTime(firstDay)).days

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
        ui.nav.menu.findItem(R.id.loginActivity)?.let {
            if (AuthPreferences.isLoggedIn())
                it.title = "Login details"
            else
                it.title = "Login"
        }
    }

    private fun addNavDrawerIcons() {
        ui.nav.inflateMenu(R.menu.nav_drawer)

        ui.nav.menu.apply {
            // Main
            this.setFAIcon(this@NavActivity, R.id.fragmentViewHome, R.string.fa_home_solid)
            this.setFAIcon(this@NavActivity, R.id.infoListFragment, R.string.fa_info_solid)
            this.setFAIcon(this@NavActivity, R.id.eventListFragment, R.string.fa_calendar)
            this.setFAIcon(this@NavActivity, R.id.dealerListFragment, R.string.fa_shopping_cart_solid)
            this.setFAIcon(this@NavActivity, R.id.mapListFragment, R.string.fa_map)

            // Personal
            this.setFAIcon(this@NavActivity, R.id.loginActivity, R.string.fa_user_circle)
            this.setFAIcon(this@NavActivity, R.id.navMessages, R.string.fa_envelope)
            this.setFAIcon(this@NavActivity, R.id.navFursuitGames, R.string.fa_paw_solid)
            this.setFAIcon(this@NavActivity, R.id.navAdditionalServices, R.string.fa_book_open_solid)

            // Web
            this.setFAIcon(this@NavActivity, R.id.navWebTwitter, R.string.fa_twitter)
            this.setFAIcon(this@NavActivity, R.id.navWebSite, R.string.fa_globe_solid)

            // App Management
            this.setFAIcon(this@NavActivity, R.id.navDevReload, R.string.fa_cloud_download_alt_solid)
            this.setFAIcon(this@NavActivity, R.id.navDevClear, R.string.fa_trash_solid)
            this.setFAIcon(this@NavActivity, R.id.activitySettings, R.string.fa_cog_solid)
            this.setFAIcon(this@NavActivity, R.id.fragmentViewAbout, R.string.fa_hands_helping_solid)
        }
    }

    override fun onResume() {
        info { "setting up nav toolbar" }

        updateReceiver.register()

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
            R.id.navFursuitGames -> navigateToFursuitGames()
            R.id.navAdditionalServices -> navigateToAdditionalServices()
            else -> item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToFursuitGames(): Boolean {
        val url = if (AuthPreferences.isLoggedIn())
            "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/companion/#/login?embedded=true&returnPath=/collect&token=${AuthPreferences.token}"
        else
            "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/companion/#/login?embedded=true&returnPath=/"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            putExtra(Browser.EXTRA_APPLICATION_ID, BuildConfig.CONVENTION_IDENTIFIER)
        }

        startActivity(intent)

        return true
    }

    private fun navigateToAdditionalServices(): Boolean {
        val url = if (AuthPreferences.isLoggedIn())
            "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/companion/#/login?embedded=true&returnPath=/&token=${AuthPreferences.token}"
        else
            "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/companion/#/login?embedded=true&returnPath=/"

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
                    bar = toolbar() {
                        backgroundResource = R.color.primaryDark
                        setTitleTextColor(ContextCompat.getColor(context, R.color.textWhite))
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