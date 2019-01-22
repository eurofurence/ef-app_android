@file:Suppress("MemberVisibilityCanBePrivate")

package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.perf.metrics.AddTrace
import com.pawegio.kandroid.textWatcher
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.adapter.DealerRecyclerAdapter
import org.eurofurence.connavigator.util.extensions.recycler
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by David on 15-5-2016.
 */
class DealerListFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    val ui by lazy { DealersUi() }
    private var effectiveDealers = emptyList<DealerRecord>()

    var searchText = ""
    var searchCategory = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        effectiveDealers = sortDealers(dealers.items)

        info { "Rendering ${effectiveDealers.size} dealers out of ${db.dealers.items.size}" }

        ui.dealerList.adapter = DealerRecyclerAdapter(effectiveDealers, db, this)
        ui.dealerList.layoutManager = LinearLayoutManager(activity)
        ui.dealerList.itemAnimator = DefaultItemAnimator()

        val distinctCategories = dealers.items
                .map { it.categories ?: emptyList() }
                .fold(emptyList<String>()) { a, b -> a.plus(b).distinct() }
                .sorted()

        ui.categorySpinner.adapter =
                ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item,
                        listOf("All Categories").plus(distinctCategories))

        ui.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                searchCategory = if (position == 0) {
                    ""
                } else {
                    parent.getItemAtPosition(position) as String
                }
                updateFilter()
            }
        }

        ui.search.textWatcher {
            afterTextChanged { text -> searchText = text.toString(); updateFilter() }
        }
    }

    override fun onResume() {
        super.onResume()

        activity?.apply {
            this.findViewById<Toolbar>(R.id.toolbar).apply {
                this.menu.clear()
                this.inflateMenu(R.menu.dealer_list_menu)
                this.setOnMenuItemClickListener {
                    when (it.itemId) {
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
            this.findViewById<Toolbar>(R.id.toolbar).menu.clear()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity?.apply {
            this.findViewById<Toolbar>(R.id.toolbar).menu.clear()
        }
    }

    @AddTrace(name = "DealerListFragment:search", enabled = true)
    fun updateFilter() {
        info { "Filtering dealers for text=$searchText, category=$searchCategory" }

        effectiveDealers = db.dealers.items.toList()

        if (!searchText.isEmpty())
            effectiveDealers = effectiveDealers.filter { it.displayName.contains(searchText, true) or it.attendeeNickname.contains(searchText, true) }

        if (!searchCategory.isEmpty())
            effectiveDealers = effectiveDealers.filter {
                it.categories?.contains(searchCategory) ?: false
            }

        ui.dealerList.adapter = DealerRecyclerAdapter(sortDealers(effectiveDealers), db, this)
        ui.dealerList.adapter.notifyDataSetChanged()
    }

    private fun sortDealers(dealers: Iterable<DealerRecord>): List<DealerRecord> =
            dealers.sortedBy { (if (it.displayName != "") it.displayName else it.attendeeNickname).toLowerCase() }

    fun onSearchButtonClick() {
        if (ui.searchLayout.visibility == View.GONE) {
            info { "Showing search bar" }
            ui.searchLayout.visibility = View.VISIBLE
        } else {
            info { "Hiding search bar" }
            ui.searchLayout.visibility = View.GONE
            searchText = ""
            updateFilter()
        }
    }

    fun dataUpdated() {
        ui.dealerList.adapter = DealerRecyclerAdapter(sortDealers(dealers.items), db, this)
    }
}

class DealersUi : AnkoComponent<Fragment> {
    lateinit var dealerList: RecyclerView
    lateinit var search: EditText
    lateinit var searchLayout: LinearLayout
    lateinit var categorySpinner: Spinner

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            verticalLayout {
                // Search widgets
                padding = dip(10)
                linearLayout {
                    // Filter types
                    weightSum = 100F

                    textView {
                        textResource = R.string.misc_show
                        leftPadding = dip(5)
                    }.lparams(dip(0), wrapContent, 20F)

                    categorySpinner = spinner {
                        prompt = resources.getString(R.string.misc_filter)
                    }.lparams(dip(0), wrapContent, 80F)
                }

                searchLayout = linearLayout {
                    weightSum = 100F
                    visibility = View.GONE
                    textView {
                        textResource = R.string.misc_find
                        leftPadding = dip(5)
                    }.lparams(dip(0), wrapContent, 20F)

                    search = editText { singleLine = true }.lparams(dip(0), wrapContent, 80F)
                }
            }

            dealerList = recycler {
                lparams(matchParent, matchParent)
            }
        }
    }
}