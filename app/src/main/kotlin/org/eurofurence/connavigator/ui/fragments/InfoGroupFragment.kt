package org.eurofurence.connavigator.ui.fragments

import android.animation.LayoutTransition
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
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

    val infoItems by lazy {
        db.knowledgeEntries.items
                .filter { it.knowledgeGroupId == infoGroupId }
                .sortedBy { it.order }
    }

    val ui = InfoGroupUi()

    var subscriptions = Disposables.empty()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!arguments.contains("id")) {
            longToast("No ID was passed to info fragment!")
            this.onDestroy()
        }

        fillUi()
        subscriptions += db.subscribe {
            fillUi()
        }
    }

    private fun fillUi() {
        ui.apply {
            title.text = infoGroup.name
            mainIcon.text = infoGroup.fontAwesomeIconCharacterUnicodeAddress?.toUnicode() ?: ""
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

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }

    private fun setDropdown() {
        if (ui.recyclerLayout.visibility == View.GONE) {
            ui.recyclerLayout.visibility = View.VISIBLE
            ui.dropdownCaret.text = "{fa-chevron-up 24sp}"
        } else {
            ui.recyclerLayout.visibility = View.GONE
            ui.dropdownCaret.text = "{fa-chevron-down 24sp}"
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
    lateinit var recyclerLayout: LinearLayout


    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            groupLayout = linearLayout {
                isClickable = true
                weightSum = 20F
                backgroundResource = R.color.cardview_light_background
                layoutTransition = LayoutTransition().apply {
                    enableTransitionType(LayoutTransition.APPEARING)
                    enableTransitionType(LayoutTransition.CHANGE_APPEARING)
                    enableTransitionType(LayoutTransition.CHANGING)
                }

                verticalLayout {
                    mainIcon = fontAwesomeTextView {
                        gravity = Gravity.CENTER
                        textColor = ContextCompat.getColor(context, R.color.primary)
                        textSize = 24f
                    }.lparams(matchParent, matchParent)
                }.lparams(dip(0), matchParent) {
                    weight = 4F
                }

                verticalLayout {
                    topPadding = dip(20)
                    bottomPadding = dip(20)
                    title = textView("Title") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large
                    }
                    description = textView("Description") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                }.lparams(dip(0), wrapContent) {
                    weight = 13F
                }

                verticalLayout {
                    dropdownCaret = fontAwesomeView {
                        gravity = Gravity.CENTER
                        text = "{fa-chevron-down 24sp}"
                    }.lparams(matchParent, matchParent)
                }.lparams(dip(0), matchParent) {
                    weight = 3F
                }
            }

            recyclerLayout = verticalLayout {
                recycler = recycler {
                }.lparams(matchParent, wrapContent)
                visibility = View.GONE
            }
        }
    }
}

class SingleInfoUi : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        linearLayout {
            topPadding = dip(10)
            bottomPadding = dip(10)
            id = R.id.layout
            weightSum = 20F
            lparams(matchParent, wrapContent)

            view {
                // Sneaky view to pad stuff
            }.lparams(dip(0), wrapContent) {
                weight = 4F
            }

            textView("Name") {
                id = R.id.title
                compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                textColor = ContextCompat.getColor(context, R.color.textBlack)
            }.lparams(dip(0), wrapContent) {
                weight = 13F
            }

            fontAwesomeView {
                text = "{fa-chevron-right 24sp}"
                gravity = Gravity.CENTER
            }.lparams(dip(0), matchParent) {
                weight = 3F
            }
        }
    }
}