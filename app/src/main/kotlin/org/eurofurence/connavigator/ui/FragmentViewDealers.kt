package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
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
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.editText
import org.jetbrains.anko.info
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.spinner
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

/**
 * Created by David on 15-5-2016.
 */
class FragmentViewDealers : Fragment(), ContentAPI, HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    val ui by lazy { DealersUi() }

    var effectiveDealers = emptyList<DealerRecord>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.create(container!!.context.applicationContext, container))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        applyOnRoot { changeTitle("Dealers") }

        effectiveDealers = sortDealers(dealers.items)

        info { "Rendering ${effectiveDealers.size} dealers out of ${db.dealers.items.size}" }

        ui.dealerList.adapter = DealerRecyclerAdapter(effectiveDealers, db, this)
        ui.dealerList.layoutManager = LinearLayoutManager(activity)
        ui.dealerList.itemAnimator = DefaultItemAnimator()

        ui.search.textWatcher {
            afterTextChanged { text -> search(text.toString()) }
        }
    }

    @AddTrace(name = "FragmentViewDealers:search", enabled = true)
    fun search(query: String) {
        info { "Searching dealers for $query" }

        effectiveDealers = db.dealers.items.filter { it.displayName.contains(query, true) or it.attendeeNickname.contains(query, true) }
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

                    textView("Show:") {}.lparams(dip(0), wrapContent, 20F)

                    spinner {
                        prompt = "Filter"
                        val items = listOf(
                                "All Categories",
                                "Haha there should be more here soon :3"
                        )

                        adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items)
                    }.lparams(dip(0), wrapContent, 80F)
                }

                searchLayout = linearLayout {
                    weightSum = 100F
                    visibility = View.GONE
                    textView("Search").lparams(dip(0), wrapContent, 20F)

                    search = editText { singleLine = true }.lparams(dip(0), wrapContent, 80F)
                }
            }

            dealerList = recycler {
                lparams(matchParent, matchParent)
            }
        }
    }
}