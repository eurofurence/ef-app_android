package org.eurofurence.connavigator.ui.fragments

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.ui.views.FontAwesomeTextView
import org.eurofurence.connavigator.ui.views.FontAwesomeType
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.*
import java.util.*

/**
 * Renders an info group element and displays it's individual items
 */
class InfoGroupFragment : DisposingFragment(), HasDb, AnkoLogger {
    inner class InfoItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout by view("layout")
        val name: TextView by view("title")
    }

    inner class DataAdapter : RecyclerView.Adapter<InfoItemViewHolder>() {
        override fun getItemCount() = infoItems.count()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            InfoItemViewHolder(parent.context.createView {
                linearLayout {
                    setPadding(0, dip(10), 0, dip(10))
                    id = R.id.layout
                    weightSum = 20F
                    layoutParams = viewGroupLayoutParams(matchParent, wrapContent)

                    view {
                        layoutParams = linearLayoutParams(0, wrapContent, 4f)
                        // Sneaky view to pad stuff
                    }

                    textView {
                        layoutParams = linearLayoutParams(0, wrapContent, 13f)
                        textResource = R.string.misc_name
                        id = R.id.title
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Medium
                        textColorResource = R.color.textBlack
                    }

                    fontAwesomeView {
                        type= FontAwesomeType.Solid
                        layoutParams = linearLayoutParams(0, matchParent, 3f)
                        text = getString(R.string.fa_chevron_right_solid)
                        textSize = 24f
                        gravity = Gravity.CENTER
                    }
                }
            })

        override fun onBindViewHolder(holder: InfoItemViewHolder, position: Int) {
            val item = infoItems[position]

            holder.name.text = item.title
            holder.layout.setOnClickListener {
                val action = InfoListFragmentDirections.actionInfoListFragmentToInfoItemFragment(
                    item.id.toString(),
                    BuildConfig.CONVENTION_IDENTIFIER
                )
                findNavController().navigate(action)
            }

            holder.layout.setOnLongClickListener {
                context?.let {
                    it.share(item.shareString(it), "Share Info")
                } != null
            }
        }
    }

    override val db by lazyLocateDb()

    lateinit var uiTitle: TextView
    lateinit var uiDescription: TextView
    lateinit var uiRecycler: RecyclerView
    lateinit var uiGroupLayout: LinearLayout
    lateinit var uiDropdownCaret: FontAwesomeTextView // TODO: FA View.
    lateinit var uiMainIcon: TextView
    lateinit var uiRecyclerLayout: LinearLayout

    private val infoGroupId: UUID? get() = UUID.fromString(arguments!!.getString("id"))
    private val infoGroup by lazy { db.knowledgeGroups[infoGroupId] }

    val infoItems by lazy {
        db.knowledgeEntries.items
            .filter { it.knowledgeGroupId == infoGroupId }
            .sortedBy { it.order }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        verticalLayout {
            uiGroupLayout = linearLayout {
                isClickable = true
                weightSum = 20F
                backgroundResource = R.color.lightBackground
                layoutTransition = LayoutTransition().apply {
                    enableTransitionType(LayoutTransition.APPEARING)
                    enableTransitionType(LayoutTransition.CHANGE_APPEARING)
                    enableTransitionType(LayoutTransition.CHANGING)
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(0, matchParent, 4f)
                    uiMainIcon = fontAwesomeView {
                        layoutParams = linearLayoutParams(matchParent, matchParent)
                        gravity = Gravity.CENTER
                        textColorResource = R.color.primary
                        textSize = 24f
                    }
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(dip(0), wrapContent, 13f)
                    setPadding(0, dip(15), 0, dip(15))
                    uiTitle = textView {
                        textResource = R.string.misc_title
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large
                    }
                    uiDescription = textView {
                        textResource = R.string.misc_description
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(dip(0), matchParent, 3f)
                    uiDropdownCaret = fontAwesomeView {
                        type=FontAwesomeType.Solid
                        layoutParams = linearLayoutParams(matchParent, matchParent)
                        gravity = Gravity.CENTER
                        text = getString(R.string.fa_chevron_down_solid)
                        textSize = 24f
                    }
                }
            }

            uiRecyclerLayout = verticalLayout {
                uiRecycler = recycler {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                }
                visibility = View.GONE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillUi()

        db.subscribe {
            fillUi()
        }.collectOnDestroyView()
    }

    private fun fillUi() {
        infoGroup?.let {
            uiTitle.text = it.name
            uiMainIcon.text = it.fontAwesomeIconCharacterUnicodeAddress?.toUnicode() ?: ""
            uiDescription.text = it.description
            uiGroupLayout.setOnClickListener {
                setDropdown()
            }
            uiRecycler.apply {
                adapter = DataAdapter()
                layoutManager = NonScrollingLinearLayout(activity)
                itemAnimator = DefaultItemAnimator()
            }
        } ?: run {
            warn { "Info group initialized on non-existent ID." }
            uiGroupLayout.visibility = View.GONE
        }
    }

    private fun setDropdown() {
        if (uiRecyclerLayout.visibility == View.GONE) {
            uiRecyclerLayout.visibility = View.VISIBLE
            uiDropdownCaret.text = getString(R.string.fa_chevron_up_solid)
            uiDropdownCaret.textSize = 24f
        } else {
            uiRecyclerLayout.visibility = View.GONE
            uiDropdownCaret.text = getString(R.string.fa_chevron_down_solid)
            uiDropdownCaret.textSize = 24f
        }
    }
}
