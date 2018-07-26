package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.disposables.Disposables
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.lazyLocateDb
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.util.v2.plus
import org.jetbrains.anko.*
import org.jetbrains.anko.AnkoContext.Companion
import us.feras.mdv.MarkdownView
import java.util.*

class FragmentViewAnnouncement : Fragment(), HasDb, AnkoLogger {
    val ui = AnnouncementItemUi()
    val announcementId by lazy { UUID.fromString(arguments.getString("id")) }

    override val db by lazyLocateDb()

    var subscriptions = Disposables.empty()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(Companion.create(context, container!!))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info { "Created announcement view for $announcementId" }

        subscriptions += db.subscribe {
            val announcement = it.announcements[announcementId]!!
            info { "Updating announcement item" }
            ui.title.text = announcement.title
            ui.text.loadMarkdown(announcement.content)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscriptions.dispose()
        subscriptions = Disposables.empty()
    }
}

class AnnouncementItemUi : AnkoComponent<ViewGroup> {
    lateinit var title: TextView
    lateinit var text: MarkdownView
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            backgroundResource = R.color.cardview_light_background
            isClickable = true
            lparams(matchParent, matchParent)

            linearLayout {
                backgroundResource = R.drawable.image_fade
                title = textView {
                    compatAppearance = android.R.style.TextAppearance_DeviceDefault_Large_Inverse
                    padding = dip(50)
                }
            }
            linearLayout {
                padding = dip(20)
                text = markdownView {}
            }
        }
    }

}