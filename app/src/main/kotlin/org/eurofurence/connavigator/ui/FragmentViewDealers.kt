package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.recycler
import org.jetbrains.anko.*

/**
 * Created by David on 15-5-2016.
 */
class FragmentViewDealers : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    val ui by lazy { DealersUi() }
    var effectiveDealers = emptyList<DealerRecord>()

    var searchText = ""
    var searchCategory = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.create(container!!.context.applicationContext, container))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        applyOnRoot { changeTitle("Dealers") }

        effectiveDealers = sortDealers(dealers.items)

        info { "Rendering ${effectiveDealers.size} dealers out of ${db.dealers.items.size}" }

        ui.dealerList.adapter = DealerRecyclerAdapter(effectiveDealers, db, this)
        ui.dealerList.layoutManager = LinearLayoutManager(activity)
        ui.dealerList.itemAnimator = DefaultItemAnimator()

        val distinctCategories = dealers.items
                .map{ it.categories }
                .fold( emptyList<String>(), { a, b -> a.plus(b).distinct() } )
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

    @AddTrace(name = "FragmentViewDealers:search", enabled = true)
    fun updateFilter() {
        info { "Filtering dealers for text=$searchText, category=$searchCategory" }

        effectiveDealers = db.dealers.items.toList()

        if (!searchText.isNullOrEmpty())
            effectiveDealers = effectiveDealers.filter { it.displayName.contains(searchText, true) or it.attendeeNickname.contains(searchText, true) }

        if (!searchCategory.isNullOrEmpty())
            effectiveDealers = effectiveDealers.filter { it.categories.contains(searchCategory) }

        ui.dealerList.adapter = DealerRecyclerAdapter(sortDealers(effectiveDealers), db, this)
        ui.dealerList.adapter.notifyDataSetChanged()
    }

    fun sortDealers(dealers: Iterable<DealerRecord>): List<DealerRecord> =
            dealers.sortedBy { (if (it.displayName != "") it.displayName else it.attendeeNickname).toLowerCase() }

    override fun onSearchButtonClick() {
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

    override fun dataUpdated() {
        ui.dealerList.adapter = DealerRecyclerAdapter(sortDealers(dealers.items), db, this)
    }
}

class DealersUi : AnkoComponent<ViewGroup> {
    lateinit var dealerList: RecyclerView
    lateinit var search: EditText
    lateinit var searchLayout: LinearLayout
    lateinit var categorySpinner: Spinner

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey

            verticalLayout {
                // Search widgets
                padding = dip(10)
                linearLayout {
                    // Filter types
                    weightSum = 100F

                    textView("Show:") {
                        leftPadding = dip(5)
                    }.lparams(dip(0), wrapContent, 20F)

                    categorySpinner = spinner {
                        prompt = "Filter"
                    }.lparams(dip(0), wrapContent, 80F)
                }

                searchLayout = linearLayout {
                    weightSum = 100F
                    visibility = View.GONE
                    textView("Find: ")  {
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