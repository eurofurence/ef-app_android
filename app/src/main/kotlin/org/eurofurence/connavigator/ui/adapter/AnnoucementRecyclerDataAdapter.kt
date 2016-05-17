package org.eurofurence.connavigator.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.swagger.client.model.Announcement
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.delegators.view

/**
 * Created by David on 15-5-2016.
 */
class AnnouncementDataholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val announcementTitle by view(TextView::class.java)
    val announcementContent by view(TextView::class.java)
}

class AnnoucementRecyclerDataAdapter(val announcements: List<Announcement>) : RecyclerView.Adapter<AnnouncementDataholder>() {
    override fun onBindViewHolder(holder: AnnouncementDataholder, position: Int) {
        val announcement = announcements[position]

        holder.announcementTitle.text = announcement.title
        holder.announcementContent.text = announcement.content
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