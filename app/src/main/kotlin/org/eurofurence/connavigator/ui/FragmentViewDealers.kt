package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import io.swagger.client.model.Dealer
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.DealerRecyclerAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.letRoot

/**
 * Created by David on 15-5-2016.
 */
class FragmentViewDealers : Fragment(), TextWatcher, ContentAPI {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // pass
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val query = dealersSearch.text

        effectiveDealers = database.dealerDb.items.filter { if (it.displayName != "") it.displayName.contains(query, true) else it.attendeeNickname.contains(query, true) }

        dealersRecycler.adapter = DealerRecyclerAdapter(sortDealers(effectiveDealers), database, this)

        dealersRecycler.adapter.notifyDataSetChanged()
    }

    val database: Database get() = letRoot { it.database }!!
    val dealersRecycler: RecyclerView by view()
    val dealersSearch: EditText by view()
    val dealersSearchLayout: LinearLayout by view()

    var effectiveDealers = emptyList<Dealer>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_dealers, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Dealers Listing")

        applyOnRoot { changeTitle("Dealers Den") }
        dealersSearch.setSingleLine()
        effectiveDealers = sortDealers(database.dealerDb.items)

        dealersRecycler.adapter = DealerRecyclerAdapter(effectiveDealers, database, this)
        dealersRecycler.layoutManager = LinearLayoutManager(activity)
        dealersRecycler.itemAnimator = DefaultItemAnimator()

        dealersSearch.addTextChangedListener(this)
    }

    fun sortDealers(dealers: Iterable<Dealer>): List<Dealer> = dealers.sortedBy { (if (it.displayName != "") it.displayName else it.attendeeNickname).toLowerCase() }

    override fun onSearchButtonClick() {
        if (dealersSearchLayout.visibility == View.GONE) {
            dealersSearchLayout.visibility = View.VISIBLE
        } else {
            dealersSearchLayout.visibility = View.GONE
        }
    }

    override fun dataUpdated() {
        dealersRecycler.adapter = DealerRecyclerAdapter(sortDealers(database.dealerDb.items), database, this)
    }
}