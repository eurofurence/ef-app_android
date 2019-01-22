package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.pawegio.kandroid.textWatcher
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.pref.BackgroundPreferences
import org.eurofurence.connavigator.ui.adapter.DayEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.FavoriteEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.RoomEventPagerAdapter
import org.eurofurence.connavigator.ui.adapter.TrackEventPagerAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.selector
import org.jetbrains.anko.support.v4.viewPager

/**
 * Created by David on 5/3/2016.
 */
class EventListFragment : Fragment(), HasDb {
    override val db by lazyLocateDb()

    val ui = EventListUi()

    private val searchFragment by lazy { EventRecyclerFragment().withArguments(daysInsteadOfGlyphs = true) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureViewpager()

        childFragmentManager.beginTransaction()
                .replace(R.id.eventSearch, searchFragment)
                .commitAllowingStateLoss()

        ui.search.textWatcher {
            afterTextChanged { text ->
                searchFragment.eventList.byTitle(text.toString())
                        .sortByDateAndTime()
                searchFragment.dataUpdated()
            }
        }
    }


    private fun configureViewpager() {
        changePagerAdapter(BackgroundPreferences.eventPagerMode)
    }

    private fun changePagerAdapter(adapter: PagerAdapter, preferredPosition: Int? = null) {
        ui.pager.adapter = adapter
        adapter.notifyDataSetChanged()

        ui.tabs.setupWithViewPager(ui.pager)

        preferredPosition?.let {
            ui.pager.setCurrentItem(it, false)
        }
    }

    private fun changePagerAdapter(mode: EventPagerMode) = when (mode) {
        EventPagerMode.ROOMS -> changePagerAdapter(RoomEventPagerAdapter(db, childFragmentManager))
        EventPagerMode.TRACKS -> changePagerAdapter(TrackEventPagerAdapter(db, childFragmentManager))
        EventPagerMode.FAVORITES -> changePagerAdapter(FavoriteEventPagerAdapter(db, childFragmentManager))
        else -> changePagerAdapter(DayEventPagerAdapter(db, childFragmentManager), DayEventPagerAdapter.indexOfToday(db))
    }.apply { BackgroundPreferences.eventPagerMode = mode }

    override fun onResume() {
        super.onResume()
        activity?.apply {
            this.findViewById<Toolbar>(R.id.toolbar).apply {
                this.menu.clear()
                this.inflateMenu(R.menu.event_list_menu)
                this.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_filter -> onFilterButtonClick()
                        R.id.action_search -> onSearchButtonClick()
                    }

                    true
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        activity?.apply {
            val toolbar = this.findViewById<Toolbar>(R.id.toolbar)

            toolbar.menu.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.apply {
            val toolbar = this.findViewById<Toolbar>(R.id.toolbar)

            toolbar.menu.clear()
        }
    }

    fun onSearchButtonClick() {
        when (ui.pager.visibility) {
            View.VISIBLE -> {
                ui.pager.visibility = View.GONE
                ui.tabs.visibility = View.GONE

                ui.searchLayout.visibility = View.VISIBLE
                ui.searchLayout.requestFocus()

                activity!!.inputMethodManager
                        .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
            else -> {
                ui.pager.visibility = View.VISIBLE
                ui.tabs.visibility = View.VISIBLE
                ui.searchLayout.visibility = View.GONE
            }
        }
    }

    fun onFilterButtonClick() {
        selector(getString(R.string.misc_filter_change_mode), listOf(getString(R.string.misc_days), getString(R.string.misc_rooms), getString(R.string.event_tracks), getString(R.string.event_schedule))) { _, position ->
            when (position) {
                1 -> changePagerAdapter(EventPagerMode.ROOMS)
                2 -> changePagerAdapter(EventPagerMode.TRACKS)
                3 -> changePagerAdapter(EventPagerMode.FAVORITES)
                else -> changePagerAdapter(EventPagerMode.DAYS)
            }
        }
    }
}

enum class EventPagerMode {
    DAYS,
    ROOMS,
    TRACKS,
    FAVORITES
}

class EventListUi : AnkoComponent<Fragment> {
    lateinit var tabs: TabLayout
    lateinit var pager: ViewPager
    lateinit var search: EditText
    lateinit var searchLayout: View
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            tabs = tabLayout {
                backgroundResource = R.color.primaryDark
                setTabTextColors(
                        ContextCompat.getColor(context, R.color.textWhite),
                        ContextCompat.getColor(context, R.color.textWhite)
                )
                tabMode = TabLayout.MODE_SCROLLABLE
            }.lparams(matchParent, wrapContent)

            pager = viewPager {
                id = 1
            }.lparams(matchParent, matchParent)

            scrollView {
                verticalLayout {
                    search = editText().lparams(matchParent, wrapContent)

                    searchLayout = linearLayout() {
                        id = R.id.eventSearch
                    }.lparams(matchParent, wrapContent)
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, matchParent)
        }
    }

}