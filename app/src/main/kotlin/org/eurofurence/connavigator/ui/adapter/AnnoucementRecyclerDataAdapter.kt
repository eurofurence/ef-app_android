package org.eurofurence.connavigator.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.swagger.client.model.AnnouncementRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.jetbrains.anko.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Created by David on 15-5-2016.
 */
class AnnouncementDataholder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    val layout: LinearLayout by view()
    val announcementTitle: TextView by view()
    val announcementDate: TextView by view()
    val announcementContent: TextView by view()
    val announcementCaret: TextView by view()
}

class AnnoucementRecyclerDataAdapter(val announcements: List<AnnouncementRecord>) :
        RecyclerView.Adapter<AnnouncementDataholder>() {
    override fun onBindViewHolder(holder: AnnouncementDataholder, position: Int) {
        val announcement = announcements[position]

        holder.announcementTitle.text = announcement.title
        holder.announcementDate.text = Html.fromHtml("${announcement.area} <i>by</i> ${announcement.author}")
        holder.announcementContent.text = announcement.content
        holder.announcementDate.text = announcement.lastChangeDateTimeUtc.toString()

        holder.layout.setOnClickListener { showItem(holder) }


    }

    fun showItem(holder: AnnouncementDataholder) {
        if (holder.announcementContent.visibility == View.GONE) {
            holder.announcementTitle.setSingleLine(false)
            holder.announcementContent.visibility = View.VISIBLE

            holder.announcementCaret.text = "{fa-caret-up}"

            Analytics.event(Analytics.Category.ANNOUNCEMENT, Analytics.Action.OPENED, holder.announcementTitle.text.toString())
        } else {
            holder.announcementTitle.setSingleLine(true)
            holder.announcementContent.visibility = View.GONE

            holder.announcementCaret.text = "{fa-caret-down}"
        }
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
        verticalLayout {
            id = R.id.layout
            backgroundResource = R.color.cardview_light_background
            padding = dip(15)
            lparams(matchParent, wrapContent)

            linearLayout {
                weightSum = 10F

                textView {
                    id = R.id.announcementTitle
                    maxLines = 1
                    setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Medium)
                }.lparams(wrapContent, wrapContent, 9F)

                fontAwesomeView {
                    id = R.id.announcementCaret
                    textAlignment = TextView.TEXT_ALIGNMENT_VIEW_END
                    text = "{fa-caret-down}"
                }.lparams(wrapContent, wrapContent, 1F)
            }.lparams(matchParent, wrapContent)

            textView {
                id = R.id.announcementDate
                setTextAppearance(ctx, R.style.TextAppearance_AppCompat_Small)
            }.lparams(wrapContent, wrapContent)

            view {
                lparams(matchParent, dip(1)){
                    verticalMargin = dip(5)
                }
                backgroundResource = R.color.primary
            }


            textView {
                id = R.id.announcementContent
                visibility = View.GONE
                padding = dip(10)
            }.lparams(matchParent, wrapContent)
        }
    }
}