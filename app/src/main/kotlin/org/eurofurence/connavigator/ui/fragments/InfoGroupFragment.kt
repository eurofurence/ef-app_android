package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.longToast
import java.util.*

/**
 * Renders an info group element and displays it's individual items
 */
class InfoGroupFragment : Fragment(), HasDb, ContentAPI {
    inner class InfoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout by view("layout")
        val name: TextView by view("title")
    }

    inner class DataAdapter : RecyclerView.Adapter<InfoItemViewHolder>() {
        override fun getItemCount() = infoItems.count()

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                InfoItemViewHolder(UI { SingleInfoUi().createView(this) }.view)

        override fun onBindViewHolder(holder: InfoItemViewHolder, position: Int) {
            val item = infoItems[position]

            holder.name.text = item.title
            holder.layout.setOnClickListener {
                applyOnRoot { navigateToKnowledgeEntry(item) }
            }
        }
    }

    override val db by lazyLocateDb()

    val infoGroupId get() = UUID.fromString(arguments.getString("id"))
    val infoGroup by lazy { db.knowledgeGroups[infoGroupId]!! }
    val infoItems by lazy { db.knowledgeEntries.items.filter { it.knowledgeGroupId == infoGroupId } }

    val ui = InfoGroupUi()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!arguments.contains("id")) {
            longToast("No ID was passed to info fragment!")
            this.onDestroy()
        }

        db.subscribe {
            ui.apply {
                title.text = infoGroup.name
                mainIcon.text = infoGroup.fontAwesomeIconCharacterUnicodeAddress.toLong(radix = 16).toChar().toString()
                description.text = infoGroup.description
                groupLayout.setOnClickListener {
                    setDropdown()
                }
                recycler.apply {
                    adapter = DataAdapter()
                    layoutManager = NonScrollingLinearLayout(context)
                    itemAnimator = DefaultItemAnimator()
                }
            }
        }
    }

    private fun setDropdown() {
        if(ui.recycler.visibility == View.GONE) {
            ui.recycler.visibility = View.VISIBLE
            ui.dropdownCaret.text = "{fa-caret-up 24sp}"
        } else {
            ui.recycler.visibility = View.GONE
            ui.dropdownCaret.text = "{fa-caret-down 24sp}"
        }
    }
}

class InfoGroupUi : AnkoComponent<Fragment> {
    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var recycler: RecyclerView
    lateinit var groupLayout: LinearLayout
    lateinit var dropdownCaret: IconTextView
    lateinit var mainIcon: TextView


    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            groupLayout = linearLayout {
                isClickable = true
                weightSum = 10F
                backgroundResource = R.color.cardview_light_background
                padding = dip(20)

                verticalLayout {
                    mainIcon = fontAwesomeTextView {
                        gravity = Gravity.CENTER
                        textColor = ContextCompat.getColor(context, R.color.primary)
                        textSize = 24f
                    }.lparams(matchParent, matchParent)
                }.lparams(dip(0), matchParent) {
                    weight = 2F
                }

                verticalLayout {
                    title = textView("Title") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large
                    }
                    description = textView("Description") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                }.lparams(dip(0), wrapContent) {
                    weight = 7F
                }

                verticalLayout {
                    dropdownCaret = fontAwesomeView {
                        gravity = Gravity.CENTER
                        text = "{fa-caret-down 24sp}"
                    }.lparams(matchParent, matchParent)
                }.lparams(dip(0), matchParent) {
                    weight = 1F
                }
            }

            recycler = recycler {
                visibility = View.GONE
            }.lparams(matchParent, matchParent)
        }
    }
}

class SingleInfoUi : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        linearLayout {
            padding = dip(10)
            id = R.id.layout
            weightSum = 10F

            view {
                // Sneaky view to pad stuff
            }.lparams(dip(0), wrapContent) {
                weight = 2F
            }

            textView("Name") {
                id = R.id.title
                compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                textColor = ContextCompat.getColor(context, R.color.textBlack)
            }.lparams(dip(0), wrapContent) {
                weight = 7F
            }

            fontAwesomeView {
                text = "{fa-caret-right 24sp}"
            }.lparams(dip(0), wrapContent){
                weight = 1F
            }
        }
    }
}