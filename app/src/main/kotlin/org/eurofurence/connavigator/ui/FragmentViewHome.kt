package org.eurofurence.connavigator.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.github.lzyzsd.circleprogress.ArcProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.fragments.AnnouncementListFragment
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.fragments.UserStatusFragment
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.arcProgress
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.nestedScrollView
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, AnkoLogger, NavRepresented {
    val ui by lazy { HomeUi() }

    override val drawerItemId: Int
        get() = R.id.navHome

    val database by lazy { locateDb() }
    var subscriptions = Disposables.empty()
    val now by lazy { DateTime.now() }

    val upcoming by lazy { EventRecyclerFragment().withArguments(EventList(database).isUpcoming().sortByStartTime(), "Upcoming events", false) }
    val current by lazy { EventRecyclerFragment().withArguments(EventList(database).isCurrent().sortByStartTime(), "Running events", false) }
    val favorited by lazy { EventRecyclerFragment().withArguments(EventList(database).isFavorited().sortByDateAndTime(), "Favorited events", false, true) }
    val announcement by lazy { AnnouncementListFragment() }
    val userStatus by lazy { UserStatusFragment() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        applyOnRoot { changeTitle("Home") }

        configureEventRecyclers()

        subscriptions += RemotePreferences.observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { configureProgressBar() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    private fun configureEventRecyclers() {
        info { "Configuring event recyclers" }

        childFragmentManager.beginTransaction()
                .replace(R.id.home_current, current)
                .replace(R.id.home_upcoming, upcoming)
                .replace(R.id.home_favorited, favorited)
                .replace(R.id.home_announcement, announcement)
                .replace(R.id.home_user_status, userStatus)
                .commitAllowingStateLoss()
    }

    private fun configureProgressBar() {
        info { "configuring progress bar" }
        val lastConDay = DateTime(RemotePreferences.lastConEnd)
        val nextConDay = DateTime(RemotePreferences.nextConStart)

        val totalDaysBetween = Days.daysBetween(lastConDay, nextConDay)
        val totalDaysToNextCon = Days.daysBetween(now, nextConDay)

        info { "Days between cons : ${totalDaysBetween.days}" }
        info { "Days to next con: ${totalDaysToNextCon.days}" }

        ui.countdownArc.max = totalDaysBetween.days
        ui.countdownArc.progress = totalDaysToNextCon.days

        if (totalDaysToNextCon.days <= 0) {
            info { "Hiding countdown to next con" }
            ui.countdownLayout.visibility = View.GONE
        }
    }
}

class HomeUi : AnkoComponent<Fragment> {
    lateinit var countdownArc: ArcProgress
    lateinit var countdownLayout: LinearLayout

    lateinit var upcomingFragment: ViewGroup
    lateinit var currentFragment: ViewGroup
    lateinit var favoritesFragment: ViewGroup
    lateinit var announcementFragment: ViewGroup
    lateinit var loginWidget: ViewGroup

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        nestedScrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

                lparams(matchParent, matchParent)

                imageView(R.drawable.banner_2018) {
                    adjustViewBounds = true
                    setBackgroundColor(Color.WHITE)
                    ViewCompat.setElevation(this, 15f)
                }.lparams(matchParent, wrapContent) {
                    setMargins(0, 0, 0, 0)
                }

                loginWidget = linearLayout {
                    id = R.id.home_user_status
                    lparams(matchParent, wrapContent)
                }.lparams(matchParent, wrapContent) {
                    setMargins(0, dip(10), 0, 0)
                }

                countdownLayout = linearLayout {
                    countdownArc = arcProgress {
                        lparams(displayMetrics.widthPixels / 2, displayMetrics.widthPixels / 2)
                        gravity = Gravity.CENTER
                        strokeWidth = 25F
                        suffixText = "Days"
                        bottomText = "Until next EF"
                        bottomTextSize = dip(16F).toFloat()
                        suffixTextSize = dip(20F).toFloat()

                        finishedStrokeColor = ContextCompat.getColor(ctx, R.color.accentLight)
                        unfinishedStrokeColor = ContextCompat.getColor(ctx, R.color.primary)
                        textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                    }
                    padding = dip(20)
                }

                announcementFragment = linearLayout {
                    id = R.id.home_announcement
                }.lparams(matchParent, wrapContent) {
                    setMargins(0, dip(10), 0, 0)
                }

                upcomingFragment = linearLayout {
                    id = R.id.home_current
                }.lparams(matchParent, wrapContent)

                currentFragment = linearLayout {
                    id = R.id.home_upcoming
                }.lparams(matchParent, wrapContent)

                favoritesFragment = linearLayout {
                    id = R.id.home_favorited
                }.lparams(matchParent, wrapContent)
            }
        }
    }
}
