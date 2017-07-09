package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.model.KnowledgeEntryRecord
import io.swagger.client.model.KnowledgeGroupRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.net.imageService
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.*
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.v2.get
import org.jetbrains.anko.*

class FragmentViewInfoGroups : Fragment(), ContentAPI, HasDb {
    override val db by lazyLocateDb()

    companion object {
        fun <T : Any, U : Any> weave(parents: List<T>, children: Map<T, List<U>>): List<Choice<T, U>> =
                parents.flatMap {
                    val head = listOf(left<T, U>(it))
                    val tail = (children[it] ?: emptyList()).map { right<T, U>(it) }
                    head + tail
                }
    }

    // Event view holder finds and memorizes the views in an event card
    inner class InfoGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView by view()
        val description: TextView by view()
        val image: ImageView by view()
    }

    // Event view holder finds and memorizes the views in an event card
    inner class InfoGroupItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView by view()
        val layout: LinearLayout by view()
    }

    inner class DataAdapter : RecyclerView.Adapter<ViewHolder>() {
        override fun getItemViewType(pos: Int) =
                if (effectiveInterleaved[pos].isLeft) 1 else 0

        override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder =
                when (type) {
                    0 -> InfoGroupItemViewHolder(
                            GroupItemUi().createView(AnkoContext.create(context.applicationContext, parent)))
                    1 -> InfoGroupViewHolder(
                            GroupUi().createView(AnkoContext.create(context.applicationContext, parent)))
                    else -> throw IllegalStateException()

                }

        override fun getItemCount() =
                effectiveInterleaved.size


        override fun onBindViewHolder(rawHolder: ViewHolder, pos: Int) {
            effectiveInterleaved[pos] onLeft { group ->
                // Cast holder
                val holder = rawHolder as InfoGroupViewHolder

                // Set data
                holder.title.text = group.name
                holder.description.text = group.description

            } onRight { entry ->
                // Cast holder
                val holder = rawHolder as InfoGroupItemViewHolder

                // Set data
                holder.title.text = entry.title

                // Handle clicks
                holder.itemView.setOnClickListener {
                    applyOnRoot { navigateToKnowledgeEntry(entry) }
                    vibrator.short()
                }
                holder.itemView.setOnLongClickListener {
                    startActivity(SharingUtility.share(Formatter.shareInfo(entry))).let { true }
                    vibrator.long().let { true }
                }
            }
        }
    }


    // View
    val infoGroups: RecyclerView by view()

    val vibrator by lazy { TouchVibrator(context) }

    // Store of currently displayed info groups and items
    var effectiveInterleaved = emptyList<Choice<KnowledgeGroupRecord, KnowledgeEntryRecord>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fview_info_groups, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // Initialize the info groups
        dataInit()

        applyOnRoot { changeTitle("Info Listings") }

        Analytics.screen(activity, "Info Listings")

        // Default setup for recycler layout and animation
        infoGroups.layoutManager = LinearLayoutManager(context)
        infoGroups.itemAnimator = DefaultItemAnimator()
        infoGroups.adapter = DataAdapter()
    }

    override fun dataUpdated() {
        dataInit()

        infoGroups.adapter.notifyDataSetChanged()
    }

    private fun dataInit() {
        // Calibrate groups and elements
        val groups = knowledgeGroups.asc { it.order }
        val elements = knowledgeEntries.items
                .groupBy { it[toGroup] ?: error("Entry without group mapping.") }
                .mapValues { it.value.sortedBy { it.order } }

        // Weave them
        effectiveInterleaved = weave(groups, elements)
    }
}

class GroupUi : AnkoComponent<ViewGroup> {
    lateinit var image: ImageView
    lateinit var title: TextView
    lateinit var description: TextView
    lateinit var layout: LinearLayout

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, wrapContent)
            backgroundResource = android.R.color.background_light
            id = R.id.layout

            image = imageView {
                lparams(matchParent, dip(200))
                visibility = View.GONE
                scaleType = ImageView.ScaleType.FIT_CENTER
                id = R.id.image
            }

            title = themedTextView(R.style.AppTheme_Header) {
                setTextAppearance(ctx, android.R.style.TextAppearance_Large_Inverse)
                id = R.id.title
            }

            description = textView {
                setTextAppearance(ctx, android.R.style.TextAppearance_Small)
                padding = dip(10)
                id = R.id.description
            }
        }
    }
}

class GroupItemUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            padding = dip(16)
            id = R.id.layout

            textView {
                setTextAppearance(ctx, android.R.style.TextAppearance_Large)
                lparams(matchParent, wrapContent)
                id = R.id.title
            }
        }
    }

}