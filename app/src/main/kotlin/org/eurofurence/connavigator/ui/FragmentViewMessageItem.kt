package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.joanzapata.iconify.widget.IconTextView
import com.pawegio.kandroid.longToast
import io.swagger.client.model.PrivateMessageRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.extensions.markAsRead
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.*
import org.jetbrains.anko.AnkoContext.Companion
import us.feras.mdv.MarkdownView

class FragmentViewMessageItem : Fragment(), AnkoLogger {
    val ui = MessageItemUi()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(Companion.create(requireContext(), container!!))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { arguments ->
            val message = Gson().fromJson(arguments.getString("message"), PrivateMessageRecord::class.java)
            ui.title.text = message.subject.capitalize()

            ui.author.text = "From: ${message.authorName}"
            ui.sent.text = "Sent: ${message.createdDateTimeUtc.toRelative()}"
            ui.received.text = "Received: ${message.receivedDateTimeUtc.toRelative()}"
            ui.read.text = "Read: ${message.readDateTimeUtc?.toRelative() ?: "Not yet"}"

            ui.content.loadMarkdown(message.message)

            if (message.readDateTimeUtc == null) {
                ui.icon.textColor = ContextCompat.getColor(requireContext(), R.color.primaryDark)
                ui.icon.text = "{fa-envelope 30sp}"
            } else {
                ui.icon.textColor = ContextCompat.getColor(requireContext(), android.R.color.tertiary_text_dark)
                ui.icon.text = "{fa-envelope-o 30sp}"
            }

            task {
                info { "Marking message ${message.id} as read" }
                message.markAsRead()
            } successUi {
                info { "Message marked as read" }
                longToast("Message has been marked as read!")
            }
        }
    }
}

class MessageItemUi : AnkoComponent<ViewGroup> {
    lateinit var icon: IconTextView
    lateinit var title: TextView
    lateinit var author: TextView
    lateinit var sent: TextView
    lateinit var received: TextView
    lateinit var read: TextView
    lateinit var content: MarkdownView

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)
            backgroundResource = R.color.backgroundGrey
            isClickable = true

            linearLayout {
                weightSum = 100F

                icon = fontAwesomeView {
                    text = "{fa-envelope 30sp}"
                    gravity = Gravity.LEFT or Gravity.TOP
                    setPadding(dip(20), dip(20), 0, 0)
                }.lparams(dip(0), matchParent, 15F)

                verticalLayout {
                    padding = dip(20)

                    title = textView("Title") {
                        compatAppearance = android.R.style.TextAppearance_Medium
                    }.lparams(matchParent, wrapContent)

                    author = textView("Subtitle") {
                        topPadding = dip(10)
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                    sent = textView("Sent") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                    received = textView("Received") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                    read = textView("Read") {
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                }.lparams(dip(0), matchParent, 85F)
            }.lparams(matchParent, wrapContent)

            linearLayout {
                content = markdownView {
                    loadMarkdown("Content")
                }
                padding = dip(20)
                backgroundResource = R.color.cardview_light_background
            }
        }
    }

}