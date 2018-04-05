package org.eurofurence.connavigator.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.model.AnnouncementRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

/**
 * Created by David on 15-5-2016.
 */
class AnnouncementDataholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val layout: LinearLayout by view()
    val announcementTitle: TextView by view()
    val announcementContent: TextView by view()
}

class AnnoucementRecyclerDataAdapter(val announcements: List<AnnouncementRecord>) :
        RecyclerView.Adapter<AnnouncementDataholder>() {
    override fun onBindViewHolder(holder: AnnouncementDataholder, position: Int) {
        val announcement = announcements[position]

        holder.announcementTitle.text = announcement.title
        holder.announcementContent.text = announcement.area
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementDataholder =
            AnnouncementDataholder(AnnouncementUi().createView(
                    AnkoContext.create(parent.context.applicationContext, parent)))

    override fun getItemCount(): Int {
        return announcements.count()
    }
}

class AnnouncementUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent)
            weightSum = 10F

            fontAwesomeView {
                setPadding(0, dip(10), 0, 0)
                text = "{fa-file-text 24sp}"
                textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            }.lparams(dip(0), wrapContent, 1F)

            verticalLayout {
                textView("Announcement Title") {
                    id = R.id.announcementTitle
                    compatAppearance = android.R.style.TextAppearance_Medium
                }.lparams(matchParent, wrapContent) {
                    setMargins(0, dip(5), 0, 0)
                }

                textView("Announcement Content") {
                    id = R.id.announcementContent
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                }.lparams(matchParent, wrapContent) {
                    setMargins(0, 0, 0, dip(10))
                }

            }.lparams(dip(0), wrapContent, 9F)
        }
    }
}