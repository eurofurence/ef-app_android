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
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.adapter.DealerRecyclerAdapter
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot

/**
 * Created by David on 15-5-2016.
 */
class FragmentViewDealers : Fragment(), TextWatcher, ContentAPI, HasDb {
    override val db by lazyLocateDb()

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // pass
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val query = dealersSearch.text

        effectiveDealers = dealers.items
                .filter {
                    if (it.displayName != "")
                        it.displayName.contains(query, true)
                    else
                        it.attendeeNickname.contains(query, true)
                }

        dealersRecycler.adapter = DealerRecyclerAdapter(sortDealers(effectiveDealers), db, this)

        dealersRecycler.adapter.notifyDataSetChanged()
    }

    val dealersRecycler: RecyclerView by view()
    val dealersSearch: EditText by view()
    val dealersSearchLayout: LinearLayout by view()

    var effectiveDealers = emptyList<DealerRecord>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_dealers, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        Analytics.screen(activity, "Dealers Listing")

        applyOnRoot { changeTitle("Dealers Den") }
        dealersSearch.setSingleLine()
        effectiveDealers = sortDealers(dealers.items)

        dealersRecycler.adapter = DealerRecyclerAdapter(effectiveDealers, db, this)
        dealersRecycler.layoutManager = LinearLayoutManager(activity)
        dealersRecycler.itemAnimator = DefaultItemAnimator()

        dealersSearch.addTextChangedListener(this)
    }

    fun sortDealers(dealers: Iterable<DealerRecord>): List<DealerRecord> =
            dealers.sortedBy { (if (it.displayName != "") it.displayName else it.attendeeNickname).toLowerCase() }

    override fun onSearchButtonClick() {
        if (dealersSearchLayout.visibility == View.GONE) {
            dealersSearchLayout.visibility = View.VISIBLE
        } else {
            dealersSearchLayout.visibility = View.GONE
        }
    }

    override fun dataUpdated() {
        dealersRecycler.adapter = DealerRecyclerAdapter(sortDealers(dealers.items), db, this)
    }
}