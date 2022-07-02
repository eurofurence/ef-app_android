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
import com.github.lzyzsd.circleprogress.ArcProgress
import com.pawegio.kandroid.notificationManager
import io.reactivex.android.schedulers.AndroidSchedulers
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.notifications.cancelFromRelated
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.preferences.RemotePreferences
import org.eurofurence.connavigator.ui.filters.*
import org.eurofurence.connavigator.util.DatetimeProxy
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class HomeFragment : DisposingFragment(), AnkoLogger, HasDb {
    override val db by lazyLocateDb()

    lateinit var uiCountdownArc: ArcProgress
    lateinit var uiCountdownLayout: LinearLayout

    lateinit var uiUpcomingFragment: ViewGroup
    lateinit var uiCurrentFragment: ViewGroup
    lateinit var uiFavoritesFragment: ViewGroup
    lateinit var uiAnnouncementFragment: ViewGroup
    lateinit var uiLoginWidget: ViewGroup
    lateinit var uiAppStatusWidget: ViewGroup
    lateinit var uiLoadingWidget: FrameLayout

    val database by lazy { locateDb() }
    val now: DateTime get() = DatetimeProxy.now()

    private val upcoming by lazy {
        EventRecyclerFragment()
            .withArguments(
                FilterIsUpcoming() then OrderTime(),
                getString(R.string.event_upcoming),
                false
            )
    }

    private val current by lazy {
        EventRecyclerFragment()
            .withArguments(
                FilterIsCurrent() then OrderTime(),
                getString(R.string.event_running),
                false
            )
    }

    private val favorited by lazy {
        EventRecyclerFragment()
            .withArguments(
                FilterIsFavorited() then OrderDayAndTime(),
                getString(R.string.event_favorited),
                false,
                true
            )
    }

    private val announcement by lazy { AnnouncementListFragment() }
    private val userStatus by lazy { UserStatusFragment() }
    private val loadingWidget by lazy { LoadingIndicatorFragment() }
    private val appStatus by lazy { AppStatusFragment() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        nestedScrollView {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)
            verticalLayout {
                descendantFocusability = ViewGroup.FOCUS_BLOCK_DESCENDANTS
                layoutParams = viewGroupLayoutParams(matchParent, matchParent)

                imageView(R.drawable.banner_2022) {
                    layoutParams = linearLayoutParams(matchParent, wrapContent) {
                        setMargins(0, 0, 0, 0)
                    }
                    adjustViewBounds = true
                    setBackgroundColor(Color.WHITE)
                    ViewCompat.setElevation(this, 15f)
                }

                uiAppStatusWidget = frameLayout {
                    id = R.id.appStatusWidget
                }

                uiLoadingWidget = frameLayout {
                    id = R.id.loadingWidget
                }

                uiCurrentFragment = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    id = R.id.home_upcoming
                }

                uiLoginWidget = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent) {
                        setMargins(0, dip(10), 0, 0)
                    }
                    id = R.id.home_user_status
                }

                uiCountdownLayout = linearLayout {
                    uiCountdownArc = arcProgress {
                        layoutParams = linearLayoutParams(
                            resources.displayMetrics.widthPixels / 2,
                            resources.displayMetrics.widthPixels
                        )

                        gravity = Gravity.CENTER
                        strokeWidth = 25F
                        suffixText = resources.getString(R.string.misc_days)
                        bottomText = resources.getString(R.string.misc_until_next_ef)
                        bottomTextSize = dip(16F)
                        suffixTextSize = dip(20F)

                        finishedStrokeColor = ContextCompat.getColor(context, R.color.accentLight)
                        unfinishedStrokeColor = ContextCompat.getColor(context, R.color.primary)
                        textColor = ContextCompat.getColor(context, R.color.textBlack)
                    }
                    padding = dip(20)
                }

                uiAnnouncementFragment = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent) {
                        setMargins(0, dip(10), 0, 0)
                    }
                    id = R.id.home_announcement
                }

                uiUpcomingFragment = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    id = R.id.home_current
                }

                uiCurrentFragment = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    id = R.id.home_upcoming
                }

                uiFavoritesFragment = linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    id = R.id.home_favorited
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        AuthPreferences.validate()

        configureEventRecyclers()

        RemotePreferences.observer
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { configureProgressBar() }
            .collectOnDestroyView()

        db.subscribe {
            context?.notificationManager?.apply {
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

        uiCountdownArc.max = totalDaysBetween.days
        uiCountdownArc.progress = totalDaysToNextCon.days

        if (totalDaysToNextCon.days <= 0) {
            info { "Hiding countdown to next con" }
            uiCountdownLayout.visibility = View.GONE
        }
    }
}
