package org.eurofurence.connavigator.ui.fragments

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
import io.swagger.client.model.AnnouncementRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.preferences.AppPreferences
import org.eurofurence.connavigator.ui.views.NonScrollingLinearLayout
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.filterIf
import org.eurofurence.connavigator.util.extensions.now

class AnnouncementListFragment : DisposingFragment(), HasDb, AnkoLogger {
    inner class AnnouncementDataholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
                val action =
                    HomeFragmentDirections.actionFragmentViewHomeToFragmentViewAnnouncement(
                        announcement.id.toString()
                    )
                findNavController().navigate(action)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnouncementDataholder {
            return AnnouncementDataholder(parent.context.createView{
                linearLayout {
                    layoutParams = linearLayoutParams(matchParent, wrapContent)
                    id = R.id.layout
                    weightSum = 100F

                    fontAwesomeView {
                        text = getString(R.string.fa_file_word)
                        textSize = 24f
                        layoutParams = linearLayoutParams(0, matchParent, 15f) {
                            setMargins(0, dip(5), 0, 0)
                        }
                        gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    }

                    verticalLayout {
                        layoutParams = linearLayoutParams(dip(0), wrapContent, 80F) {
                            setMargins(0, 0, 0, dip(5))
                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(0, 0, 0, 0)
                            }
                             textResource = R.string.announcement_title
                            id = R.id.announcementTitle
                            compatAppearance = android.R.style.TextAppearance_Medium
                        }

                        textView {
                            layoutParams = linearLayoutParams(matchParent, wrapContent) {
                                setMargins(0, 0, 0, dip(10))
                            }
                             textResource = R.string.announcement_content
                            id = R.id.announcementContent
                            compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                        }

                    }
                }
            })
        }

        override fun getItemCount(): Int {
            return announcements.count()
        }
    }

    override val db by lazyLocateDb()

    private val announcementAdapter by lazy { AnnoucementRecyclerDataAdapter() }

    lateinit var uiAnnouncements: RecyclerView
    lateinit var uiTitle: TextView
    lateinit var uiLayout: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        uiLayout = verticalLayout {
            layoutParams = linearLayoutParams(matchParent, wrapContent)
            backgroundResource= R.color.lightBackground

            uiTitle = textView {
                layoutParams = linearLayoutParams(matchParent, wrapContent) {
                    setMargins(0, 0, 0, 0)
                }
                 textResource = R.string.announcements
                compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                padding = dip(15)
            }

            uiAnnouncements = recycler {
                layoutParams = linearLayoutParams(matchParent, wrapContent)
                itemAnimator = DefaultItemAnimator()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        info { "Creating announcements list" }

        uiAnnouncements.apply {
            adapter = announcementAdapter
            layoutManager = NonScrollingLinearLayout(activity)
            itemAnimator = DefaultItemAnimator()
        }

        db.subscribe {
            info { "Updating items in announcement recycler" }
            uiLayout.visibility = if (getAnnouncements().count() == 0) View.GONE else View.VISIBLE
            announcementAdapter.announcements = getAnnouncements()
            announcementAdapter.notifyDataSetChanged()
        }
            .collectOnDestroyView()
    }

    private fun getAnnouncements() = db.announcements.items
        .filterIf(!AppPreferences.showOldAnnouncements) { it.validFromDateTimeUtc.time <= now().millis && it.validUntilDateTimeUtc.time > now().millis }
        .sortedBy { it.validFromDateTimeUtc }
        .toList()
}
