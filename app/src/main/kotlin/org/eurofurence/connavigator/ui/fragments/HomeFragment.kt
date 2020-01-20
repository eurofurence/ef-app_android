package org.eurofurence.connavigator.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.github.lzyzsd.circleprogress.ArcProgress
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.notifications.cancelFromRelated
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.ui.filters.*
import org.eurofurence.connavigator.util.DatetimeProxy
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
class HomeFragment : DisposingFragment(), AnkoLogger, HasDb {
    override val db by lazyLocateDb()

    val ui by lazy { HomeUi() }

    val database by lazy { locateDb() }
    val now: DateTime get() = DatetimeProxy.now()

    private val upcoming by lazy {
        EventRecyclerFragment()
                .withArguments(FilterIsUpcoming() then OrderTime(), getString(R.string.event_upcoming), false)
    }

    private val current by lazy {
        EventRecyclerFragment()
                .withArguments(FilterIsCurrent() then OrderTime(), getString(R.string.event_running), false)
    }

    private val favorited by lazy {
        EventRecyclerFragment()
                .withArguments(FilterIsFavorited() then OrderDayAndTime(), getString(R.string.event_favorited), false, true)
    }

    private val announcement by lazy { AnnouncementListFragment() }
    private val userStatus by lazy { UserStatusFragment() }
    private val loadingWidget by lazy { LoadingIndicatorFragment() }
    private val appStatus by lazy { AppStatusFragment() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        AuthPreferences.validate()

        configureEventRecyclers()

        RemotePreferences.observer
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { configureProgressBar() }
                .collectOnDestroyView()

        db.subscribe {
            context!!.notificationManager.apply {
                // Cancel all event notifications.
                for (e in events.items)
                    cancelFromRelated(e.id)

                // Cancel all announcement notifications.
                for (a in announcements.items)
                    cancelFromRelated(a.id)
            }
        }
        .collectOnDestroyView()
    }

    @SuppressLint("ResourceType")
    private fun configureEventRecyclers() {
        info { "Configuring event recyclers" }

        childFragmentManager.beginTransaction()
                .replace(R.id.loadingWidget, loadingWidget)
                .replace(R.id.home_current, current)
                .replace(R.id.home_upcoming, upcoming)
                .replace(R.id.home_favorited, favorited)
                .replace(R.id.home_announcement, announcement)
                .replace(R.id.home_user_status, userStatus)
                .replace(R.id.appStatusWidget, appStatus)
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

    private lateinit var upcomingFragment: ViewGroup
    private lateinit var currentFragment: ViewGroup
    private lateinit var favoritesFragment: ViewGroup
    private lateinit var announcementFragment: ViewGroup
    private lateinit var loginWidget: ViewGroup
    private lateinit var appStatusWidget: ViewGroup
    private lateinit var loadingWidget: FrameLayout

    @SuppressLint("ResourceType")
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        nestedScrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS

                lparams(matchParent, matchParent)

                imageView(R.drawable.banner_2019) {
                    adjustViewBounds = true
                    setBackgroundColor(Color.WHITE)
                    ViewCompat.setElevation(this, 15f)
                }.lparams(matchParent, wrapContent) {
                    setMargins(0, 0, 0, 0)
                }

                appStatusWidget = frameLayout {
                    id = R.id.appStatusWidget
                }

                loadingWidget = frameLayout {
                    id = R.id.loadingWidget
                }

                currentFragment = linearLayout {
                    id = R.id.home_upcoming
                }.lparams(matchParent, wrapContent)

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
                        suffixText = resources.getString(R.string.misc_days)
                        bottomText = resources.getString(R.string.misc_until_next_ef)
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
