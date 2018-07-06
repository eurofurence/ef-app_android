package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.swagger.client.model.AnnouncementRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.pref.AppPreferences
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.filterIf
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.extensions.now
import org.eurofurence.connavigator.util.extensions.recycler
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoContext.Companion
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.info
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.padding
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

class AnnouncementListFragment : Fragment(), HasDb, AnkoLogger {
    inner class AnnouncementDataholder(itemView: View?) : RecyclerView.ViewHolder(itemView), ContentAPI {
        val layout: LinearLayout by view()
        val announcementTitle: TextView by view()
        val announcementContent: TextView by view()
    }

    inner class AnnoucementRecyclerDataAdapter :
            RecyclerView.Adapter<AnnouncementDataholder>(), AnkoLogger {

        var announcements = emptyList<AnnouncementRecord>()

        override fun onBindViewHolder(holder: AnnouncementDataholder, position: Int) {
            val announcement = announcements[position]

            info { "updating announcement $position" }

            holder.announcementTitle.text = announcement.title
            holder.announcementContent.text = announcement.area
            holder.layout.setOnClickListener {
                applyOnRoot {
                    navigateToAnnouncement(announcement)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementDataholder {
            return AnnouncementDataholder(AnnouncementUi().createView(
                    AnkoContext.create(parent.context.applicationContext, parent)))
        }

        override fun getItemCount(): Int {
            return announcements.count()
        }
    }

    override val db by lazyLocateDb()
    val ui = AnnouncementsUi()
    val announcementAdapter by lazy {
        AnnoucementRecyclerDataAdapter().apply {
            announcements = db.announcements.items.toList()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(Companion.create(requireContext(), container!!))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        info { "Creating announcements list" }

        ui.announcements.adapter = announcementAdapter

        Observable.just(db.announcements.items)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    info { "Updating items in announcement recycler" }
                    ui.layout.visibility = if (getAnnouncements().count() == 0) View.GONE else View.VISIBLE
                    announcementAdapter.announcements = getAnnouncements()
                }
    }

    fun getAnnouncements() = db.announcements.items
            .filterIf(AppPreferences.showOldAnnouncements) { it.validFromDateTimeUtc.time <= now().millis && it.validUntilDateTimeUtc.time > now().millis }
            .toList()
}

class AnnouncementsUi : AnkoComponent<ViewGroup> {
    lateinit var announcements: RecyclerView
    lateinit var title: TextView
    lateinit var layout: ViewGroup

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            layout = this
            padding = dip(15)
            backgroundResource = R.color.cardview_light_background
            lparams(matchParent, wrapContent) {
                setMargins(0, dip(10), 0, 0)
            }

            title = textView("Announcements") {
                compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
            }.lparams(matchParent, wrapContent) {
                setMargins(0, 0, 0, dip(10))
            }

            announcements = recycler {
                layoutManager = NonScrollingLinearLayout(context)
                itemAnimator = DefaultItemAnimator()
                lparams(matchParent, wrapContent)
            }
        }
    }
}

class AnnouncementUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            lparams(matchParent, wrapContent)
            id = R.id.layout
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