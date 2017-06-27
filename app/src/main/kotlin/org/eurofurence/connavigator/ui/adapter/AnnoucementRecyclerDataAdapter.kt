package org.eurofurence.connavigator.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.swagger.client.model.AnnouncementRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.delegators.view
import org.joda.time.format.DateTimeFormat

/**
 * Created by David on 15-5-2016.
 */
class AnnouncementDataholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val announcementTitle: TextView by view()
    val announcementDate: TextView by view()
    val announcementContent: TextView by view()
    val announcementCaret: ImageView by view()
}

class AnnoucementRecyclerDataAdapter(val announcements: List<AnnouncementRecord>) :
        RecyclerView.Adapter<AnnouncementDataholder>() {
    override fun onBindViewHolder(holder: AnnouncementDataholder, position: Int) {
        val announcement = announcements[position]

        val dateTimeFormatter = DateTimeFormat.forPattern("MMMM dd yyyy 'at' HH:mm")

        holder.announcementTitle.text = announcement.title
        holder.announcementDate.text = Html.fromHtml("${announcement.area} <i>by</i> ${announcement.author}")
        holder.announcementContent.text = announcement.content

        holder.announcementTitle.setOnClickListener { showItem(holder) }
        holder.announcementDate.setOnClickListener { showItem(holder) }
        holder.announcementCaret.setOnClickListener { showItem(holder) }


    }

    fun showItem(holder: AnnouncementDataholder) {
        if (holder.announcementContent.visibility == View.GONE) {
            holder.announcementTitle.setSingleLine(false)
            holder.announcementContent.visibility = View.VISIBLE

            holder.announcementCaret.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_collapse))

            Analytics.event(Analytics.Category.ANNOUNCEMENT, Analytics.Action.OPENED, holder.announcementTitle.text.toString())
        } else {
            holder.announcementTitle.setSingleLine(true)
            holder.announcementContent.visibility = View.GONE

            holder.announcementCaret.setImageDrawable(ContextCompat.getDrawable(holder.itemView.context, R.drawable.icon_expand))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementDataholder =
            AnnouncementDataholder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.fragment_announcement, parent, false)
            )

    override fun getItemCount(): Int {
        return announcements.count()
    }
}