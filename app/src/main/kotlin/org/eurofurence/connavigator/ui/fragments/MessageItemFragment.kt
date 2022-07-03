package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.pawegio.kandroid.longToast
import io.noties.markwon.Markwon
import io.reactivex.android.schedulers.AndroidSchedulers
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.util.extensions.markAsRead
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.services.PMService
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.dropins.fa.Fa

import java.util.*

class MessageItemFragment : DisposingFragment(), AnkoLogger {
    val args: MessageItemFragmentArgs by navArgs()
    val messageId get() = UUID.fromString(args.messageId)

    lateinit var icon: TextView
    lateinit var title: TextView
    lateinit var author: TextView
    lateinit var sent: TextView
    lateinit var received: TextView
    lateinit var read: TextView
    lateinit var content: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        verticalLayout {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)

            backgroundResource = R.color.backgroundGrey
            isClickable = true

            linearLayout {
                layoutParams = linearLayoutParams(matchParent, wrapContent)
                weightSum = 100F

                icon = fontAwesomeView {
                    layoutParams = linearLayoutParams(dip(0), matchParent, 15F)
                    text = Fa.fa_envelope
                    textSize = 30f
                    gravity = Gravity.LEFT or Gravity.TOP
                    setPadding(dip(20), dip(20), 0, 0)
                }

                verticalLayout {
                    layoutParams = linearLayoutParams(dip(0), matchParent, 85F)
                    padding = dip(20)

                    title = textView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                        textResource = R.string.misc_title
                        compatAppearance = android.R.style.TextAppearance_Medium
                    }

                    author = textView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                        textResource = R.string.misc_subtitle
                        setPadding(0, dip(10), 0, 0)
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                    sent = textView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                        textResource = R.string.message_sent
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                    received = textView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                        textResource = R.string.message_received
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                    read = textView {
                        layoutParams = linearLayoutParams(matchParent, wrapContent)
                        textResource = R.string.message_read
                        compatAppearance = android.R.style.TextAppearance_DeviceDefault_Small
                    }
                }
            }

            linearLayout {
                content = markdownView {
                    text = Markwon
                        .create(context)
                        .toMarkdown(resources.getString(R.string.misc_content))
                }
                padding = dip(20)
                backgroundResource = R.color.lightBackground
            }
        }
    }

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
            title.text = message.subject?.capitalize() ?: getString(R.string.message_no_subject)

            author.text = getString(R.string.message_from_name, message.authorName)
            sent.text = getString(
                R.string.message_sent_date, message.createdDateTimeUtc?.toRelative()
                    ?: getString(R.string.misc_not_yet)
            )
            received.text = getString(
                R.string.message_received_date, message.receivedDateTimeUtc?.toRelative()
                    ?: getString(R.string.misc_not_yet)
            )
            read.text = getString(
                R.string.message_read_date, message.readDateTimeUtc?.toRelative()
                    ?: getString(R.string.misc_not_yet)
            )

            content.text = Markwon
                .create(requireContext())
                .toMarkdown(message.message)

            if (message.readDateTimeUtc == null) {
                icon.text = Fa.fa_envelope
                icon.textSize = 30f
            } else {

                icon.text = Fa.fa_envelope_o
                icon.textSize = 30f
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
