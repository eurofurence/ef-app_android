@file:Suppress("MemberVisibilityCanBePrivate")

package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.perf.metrics.AddTrace
import com.pawegio.kandroid.runDelayed
import com.pawegio.kandroid.textWatcher
import io.swagger.client.model.DealerRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.ui.adapter.DealerRecyclerAdapter
import org.eurofurence.connavigator.util.extensions.setFAIcon


/**
 * Created by David on 15-5-2016.
 */
class DealerListFragment : Fragment(), HasDb, AnkoLogger {
    override val db by lazyLocateDb()

    lateinit var dealerList: RecyclerView
    lateinit var search: EditText
    lateinit var searchLayout: LinearLayout
    lateinit var categorySpinner: Spinner


    val layoutManager get() = dealerList?.layoutManager
    private var effectiveDealers = emptyList<DealerRecord>()

    var searchText = ""
    var searchCategory = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        createView {
            verticalLayout {
                layoutParams = viewGroupLayoutParams(matchParent, matchParent)
                backgroundResource = R.color.backgroundGrey

                verticalLayout {
                    // Search widgets
                    padding = dip(10)
                    linearLayout {
                        // Filter types
                        weightSum = 100F

                        textView {
                            layoutParams = linearLayoutParams(dip(0), wrapContent, 20F)
                            textResource = R.string.misc_show
                            setPadding(dip(5), 0, 0, 0)
                        }

                        categorySpinner = spinner {
                            layoutParams = linearLayoutParams(dip(0), wrapContent, 80F)
                            prompt = resources.getString(R.string.misc_filter)
                        }
                    }

                    searchLayout = linearLayout {
                        weightSum = 100F
                        visibility = View.GONE
                        textView {
                            layoutParams = linearLayoutParams(dip(0), wrapContent, 20F)
                            textResource = R.string.misc_find
                            setPadding(dip(5), 0, 0, 0)
                        }

                        search = editText {
                            layoutParams = linearLayoutParams(dip(0), wrapContent, 80F)
                            singleLine = true
                        }
                    }
                }

                dealerList = recycler {
                    layoutParams = linearLayoutParams(matchParent, matchParent)
                    setPadding(0, dip(10), 0, dip(10))
                    clipToPadding = false
                    backgroundResource = R.color.lightBackground
                }
            }
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        effectiveDealers = sortDealers(dealers.items)

        info { "Rendering ${effectiveDealers.size} dealers out of ${db.dealers.items.size}" }

        dealerList?.adapter = DealerRecyclerAdapter(effectiveDealers, db, this)
        dealerList?.layoutManager = LinearLayoutManager(activity)
        dealerList?.itemAnimator = DefaultItemAnimator()

        val distinctCategories = dealers.items
            .map { it.categories ?: emptyList() }
            .fold(emptyList<String>()) { a, b -> a.plus(b).distinct() }
            .sorted()

        categorySpinner.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            listOf("All Categories").plus(distinctCategories)
        )

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                searchCategory = if (position == 0) {
                    ""
                } else {
                    parent.getItemAtPosition(position) as String
                }
                updateFilter()
            }
        }

        search.textWatcher {
            afterTextChanged { text -> searchText = text.toString(); updateFilter() }
        }

    }

    override fun onResume() {
        super.onResume()

        activity?.apply {
            this.findViewById<Toolbar>(R.id.toolbar).apply {
                this.menu.clear()
                this.inflateMenu(R.menu.dealer_list_menu)
                this.context?.let {
                    this.menu.setFAIcon(
                        it,
                        R.id.action_search,
                        R.string.fa_search_solid,
                        white = true
                    )
                }
                this.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_search -> onSearchButtonClick()
                    }
                    true
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        layoutManager?.also { lm ->
            outState.putParcelable("lm_key", lm.onSaveInstanceState())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        layoutManager?.also { lm ->
            runDelayed(200) {
                savedInstanceState
                    ?.getParcelable<Parcelable>("lm_key")
                    ?.let(lm::onRestoreInstanceState)
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
            effectiveDealers = effectiveDealers.filter {
                it.displayName.contains(
                    searchText,
                    true
                ) or it.attendeeNickname.contains(searchText, true)
            }

        if (!searchCategory.isEmpty())
            effectiveDealers = effectiveDealers.filter {
                it.categories?.contains(searchCategory) ?: false
            }

        dealerList?.adapter = DealerRecyclerAdapter(sortDealers(effectiveDealers), db, this).also {
            it.notifyDataSetChanged()
        }
    }

    private fun sortDealers(dealers: Iterable<DealerRecord>): List<DealerRecord> =
        dealers.sortedBy { (if (it.displayName != "") it.displayName else it.attendeeNickname).toLowerCase() }

    fun onSearchButtonClick() {
        if (searchLayout.visibility == View.GONE) {
            info { "Showing search bar" }
            searchLayout.visibility = View.VISIBLE
            search.requestFocus()
        } else {
            info { "Hiding search bar" }
            searchLayout.visibility = View.GONE
            searchText = ""
            updateFilter()
        }
    }
}