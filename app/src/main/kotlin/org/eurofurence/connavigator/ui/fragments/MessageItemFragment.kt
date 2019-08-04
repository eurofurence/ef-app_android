package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.joanzapata.iconify.widget.IconTextView
import com.pawegio.kandroid.longToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.swagger.client.model.PrivateMessageRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.extensions.markAsRead
import org.eurofurence.connavigator.util.extensions.markdownView
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.services.PMService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI
import us.feras.mdv.MarkdownView
import java.util.*

class MessageItemFragment : DisposingFragment(), AnkoLogger {
    val ui = MessageItemUi()
    val args: MessageItemFragmentArgs by navArgs()
    val messageId get() = UUID.fromString(args.messageId)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find (or fetch) message.
        task {
            PMService.find(messageId)
        } successUi {
            showMessage(it)
        }

        // On update, set from record at ID.
        PMService
                .updated
                .observeOn(AndroidSchedulers.mainThread())
                .map { it[messageId] }
                .distinct()
                .subscribe {
                    showMessage(it)
                }
                .collectOnDestroyView()
    }

    fun showMessage(message: PrivateMessageRecord?) {
        message?.let {
            ui.title.text = message.subject?.capitalize() ?: getString(R.string.message_no_subject)

            ui.author.text = getString(R.string.message_from_name, message.authorName)
            ui.sent.text = getString(R.string.message_sent_date, message.createdDateTimeUtc?.toRelative()
                    ?: getString(R.string.misc_not_yet))
            ui.received.text = getString(R.string.message_received_date, message.receivedDateTimeUtc?.toRelative()
                    ?: getString(R.string.misc_not_yet))
            ui.read.text = getString(R.string.message_read_date, message.readDateTimeUtc?.toRelative()
                    ?: getString(R.string.misc_not_yet))

            ui.content.loadMarkdown(message.message)

            if (message.readDateTimeUtc == null) {
                ui.icon.textColor = ContextCompat.getColor(context!!, R.color.primaryDark)
                ui.icon.text = "{fa-envelope 30sp}"
            } else {
                ui.icon.textColor = ContextCompat.getColor(context!!, android.R.color.tertiary_text_dark)
                ui.icon.text = "{fa-envelope-o 30sp}"
            }

            // If not read, mark as read.
            if (message.readDateTimeUtc == null) {
                task {
                    // Mark it as red.
                    info { "Marking message ${message.id} as read" }
                    message.markAsRead()
                } successUi {
                    // Display message that PM was marked as read.
                    info { "Updated PMs, displaying message marked as read." }
                    longToast(getString(R.string.message_marked_as_read))
                } failUi {
                    // Display failure to mark as read.
                    warn("Failed to update read, updating fragments depending on it.", it)
                    longToast("Something went wrong when marking the message as read.")
                }
            }
        }
    }
}

class MessageItemUi : AnkoComponent<Fragment> {
    lateinit var icon: IconTextView
    lateinit var title: TextView
    lateinit var author: TextView
    lateinit var sent: TextView
    lateinit var received: TextView
    lateinit var read: TextView
    lateinit var content: MarkdownView

    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
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

                    title = textView {
                        textResource = R.string.misc_title
                        compatAppearance = android.R.style.TextAppearance_Medium
                    }.lparams(matchParent, wrapContent)

                    author = textView {
                        textResource = R.string.misc_subtitle
                        topPadding = dip(10)
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                    sent = textView {
                        textResource = R.string.message_sent
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                    received = textView {
                        textResource = R.string.message_received
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                    read = textView {
                        textResource = R.string.message_read
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }.lparams(matchParent, wrapContent)
                }.lparams(dip(0), matchParent, 85F)
            }.lparams(matchParent, wrapContent)

            linearLayout {
                content = markdownView {
                    loadMarkdown(resources.getString(R.string.misc_content))
                }
                padding = dip(20)
                backgroundResource = R.color.lightBackground
            }
        }
    }

}