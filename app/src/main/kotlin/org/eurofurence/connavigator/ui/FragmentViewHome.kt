package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.github.lzyzsd.circleprogress.ArcProgress
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.broadcast.DataChanged
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.pref.RemotePreferences
import org.eurofurence.connavigator.ui.adapter.AnnoucementRecyclerDataAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.filters.EventList
import org.eurofurence.connavigator.ui.fragments.EventRecyclerFragment
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*
import org.joda.time.DateTime
import org.joda.time.Days

/**
 * Created by David on 5/14/2016.
 */
class FragmentViewHome : Fragment(), ContentAPI, AnkoLogger {
    lateinit var ui: HomeUi

    val database by lazy { locateDb() }
    val now by lazy { DateTime.now() }

    val announcements by lazy {
        database.announcements.items.filterIf(
                !AppPreferences.showOldAnnouncements,
                { it.validFromDateTimeUtc.time <= now.millis && it.validUntilDateTimeUtc.time > now.millis }
        )
    }

    val dataChanged by lazy {
        context.localReceiver(DataChanged.DATACHANGED) {
            ui.updateLogin()
        }
    }


    val upcoming by lazy { EventRecyclerFragment(EventList(database).isUpcoming().sortByStartTime(), "Upcoming events", false) }
    val current by lazy { EventRecyclerFragment(EventList(database).isCurrent().sortByStartTime(), "Current events", false) }
    val favorited by lazy { EventRecyclerFragment(EventList(database).isFavorited().sortByDate().sortByStartTime(), "Your favorited events", false) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        container!!

        info { "Initializing home view" }
        ui = HomeUi()

        return ui.createView(AnkoContext.create(container.context.applicationContext, container))
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        applyOnRoot { changeTitle("Home") }

        configureProgressBar()
        configureAnnouncements()
        configureEventRecyclers()

        dataChanged.register()
    }

    private fun configureEventRecyclers() {
        info { "Configuring event recyclers" }

        fragmentManager.beginTransaction()
                .replace(5000, upcoming)
                .replace(5001, current)
                .replace(5002, favorited)
                .commitAllowingStateLoss()
    }

    private fun configureAnnouncements() {
        info { "Configuring announcement recycler" }
        info { "There are ${announcements.count()} announcements" }
        ui.announcementsRecycler.adapter = AnnoucementRecyclerDataAdapter(
                announcements.sortedByDescending { it.lastChangeDateTimeUtc }
                        .toList()
        )
        ui.announcementsRecycler.layoutManager = NonScrollingLinearLayout(activity)
        ui.announcementsRecycler.itemAnimator = DefaultItemAnimator()

        if (announcements.count() == 0) {
            ui.announcementsTitle.text = "There's no announcements right now :)"
            ui.announcementsRecycler.visibility = View.GONE
        }
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

class HomeUi : AnkoComponent<ViewGroup> {
    lateinit var countdownArc: ArcProgress
    lateinit var announcementsTitle: TextView
    lateinit var announcementsRecycler: RecyclerView
    lateinit var greeting: TextView
    lateinit var countdownLayout: LinearLayout

    lateinit var upcomingFragment: ViewGroup
    lateinit var currentFragment: ViewGroup
    lateinit var favoritesFragment: ViewGroup

    /**
     * Update the login part of the ui
     */
    fun updateLogin() {
        greeting.text = "{fa-user} Hello ${AuthPreferences.username}"
        greeting.visibility = if (AuthPreferences.isLoggedIn()) View.VISIBLE else View.GONE
    }

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        scrollView {
            lparams(matchParent, matchParent)
            verticalLayout {
                lparams(matchParent, matchParent)

                greeting = fontAwesomeView {
                    visibility = if (AuthPreferences.isLoggedIn()) View.VISIBLE else View.GONE
                    text = "{fa-user} Hello ${AuthPreferences.username}"
                    compatAppearance = R.style.TextAppearance_AppCompat_Medium
                    padding = dip(15)
                }

                if (AuthPreferences.isLoggedIn()) {
                    fontAwesomeView {
                        horizontalPadding = dip(15)

                        task {
                            val api = apiService.communications

                            api.addHeader("Authorization", AuthPreferences.asBearer())

                            api.apiV2CommunicationPrivateMessagesGet()
                        } successUi { messages ->
                            val unreadMessages = messages.filter { it.readDateTimeUtc == null }
                            if (unreadMessages.isNotEmpty()) {
                                this.text = "{fa-envelope} You have ${unreadMessages.size} messages"
                            }
                        }
                    }
                }

                countdownLayout = linearLayout {
                    countdownArc = arcProgress {
                        lparams(matchParent, displayMetrics.widthPixels - dip(2 * 20))
                        strokeWidth = 25F
                        suffixText = "Days"
                        bottomText = "Until next EF"
                        bottomTextSize = dip(20F).toFloat()
                        suffixTextSize = dip(20F).toFloat()

                        finishedStrokeColor = ContextCompat.getColor(ctx, R.color.accentLight)
                        unfinishedStrokeColor = ContextCompat.getColor(ctx, R.color.primary)
                        textColor = ContextCompat.getColor(ctx, R.color.textBlack)
                    }
                    padding = dip(20)
                }


                announcementsTitle = textView("Latest announcements") {
                    setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Large)
                    padding = dip(15)
                }.lparams(matchParent, wrapContent)

                announcementsRecycler = recycler {
                    lparams(matchParent, wrapContent)
                }

                upcomingFragment = linearLayout {
                    id = 5000
                    lparams(matchParent, wrapContent)
                }

                currentFragment = linearLayout {
                    id = 5001
                    lparams(matchParent, wrapContent)
                }

                favoritesFragment = linearLayout {
                    id = 5002
                    lparams(matchParent, wrapContent)
                }
            }
        }
    }
}
