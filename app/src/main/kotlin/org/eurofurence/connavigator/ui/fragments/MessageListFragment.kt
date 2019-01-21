package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.joanzapata.iconify.widget.IconTextView
import com.pawegio.kandroid.longToast
import io.swagger.client.model.PrivateMessageRecord
import nl.komponents.kovenant.then
import nl.komponents.kovenant.ui.failUi
import nl.komponents.kovenant.ui.promiseOnUi
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.extensions.recycler
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by requinard on 6/28/17.
 */
class FragmentViewMessageList : Fragment(), AnkoLogger, HasDb {
    override val db by lazy { locateDb() }
    val ui by lazy { MessagesUi() }

    var messages = emptyList<PrivateMessageRecord>()

    inner class MessageViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView by view()
        val icon: IconTextView by view()
        val date: TextView by view()
        val layout: ViewGroup by view()
    }

    inner class MessageAdapter : RecyclerView.Adapter<MessageViewholder>() {
        override fun onBindViewHolder(holder: MessageViewholder, position: Int) {
            val message = messages[position]

            holder.title.text = message.subject
            holder.date.text = getString(R.string.misc_concat_newline,
                    getString(R.string.message_from_name, message.authorName),
                    getString(R.string.message_sent_date, message.createdDateTimeUtc?.toRelative() ?: getString(R.string.misc_not_yet))
            )

            if (message.readDateTimeUtc != null) {
                holder.icon.textColor = ContextCompat.getColor(context!!, android.R.color.tertiary_text_dark)
                holder.icon.text = "{fa-envelope-o 30sp}"
            } else {
                holder.icon.textColor = ContextCompat.getColor(context!!, R.color.primaryDark)
                holder.icon.text = "{fa-envelope 30sp}"
            }

            holder.layout.setOnClickListener {
                val action = FragmentViewMessageListDirections
                        .ActionFragmentViewMessageListToFragmentViewMessageItem2(message.id.toString())
                findNavController().navigate(action)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageViewholder(
                SingleItemUi().createView(AnkoContext.create(context!!.applicationContext, parent))
        )

        override fun getItemCount() = messages.size

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            UI { ui.createView(this) }.view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        info { "Filling in messages" }

        promiseOnUi {
            ui.loading.visibility = View.VISIBLE
            ui.messageList.visibility = View.GONE
        } then {
            info { "Fetching messages" }
            apiService.communications.addHeader("Authorization", AuthPreferences.asBearer())
            apiService.communications.apiCommunicationPrivateMessagesGet().sortedByDescending { it.createdDateTimeUtc }
        } success {
            info("Successfully retrieved ${it.size} messages")
            this.messages = it
        } successUi {
            configureRecycler()
            ui.loading.visibility = View.GONE
            ui.messageList.visibility = View.VISIBLE
        } failUi {
            warn { "Failed to retrieve messages" }
            longToast(getString(R.string.message_failed_to_retrieve))
        }
    }

    private fun configureRecycler() {
        ui.messageList.adapter = MessageAdapter()
        ui.messageList.layoutManager = LinearLayoutManager(activity)
        ui.messageList.itemAnimator = DefaultItemAnimator()
    }

}

class SingleItemUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayout {
            backgroundResource = R.color.cardview_light_background
            setPadding(0, dip(10), 0, dip(10))
            weightSum = 100F
            id = R.id.layout
            isClickable = true
            lparams(matchParent, wrapContent)

            fontAwesomeView {
                id = R.id.icon
                text = "{fa-envelope 30sp}"
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dip(20), 0, 0, 0)
            }.lparams(dip(0), matchParent, 15F)

            verticalLayout {
                textView {
                    id = R.id.title
                    compatAppearance = android.R.style.TextAppearance_Medium
                }

                textView {
                    id = R.id.date
                    compatAppearance = android.R.style.TextAppearance_Small
                }
            }.lparams(dip(0), wrapContent, 75F)

            fontAwesomeView {
                text = "{fa-chevron-right 30sp}"
                gravity = Gravity.CENTER
            }.lparams(dip(0), matchParent, 10F)
        }
    }

}

class MessagesUi : AnkoComponent<Fragment> {
    lateinit var loading: ProgressBar
    lateinit var messageList: RecyclerView
    override fun createView(ui: AnkoContext<Fragment>) = with(ui) {
        verticalLayout {
            lparams(matchParent, matchParent)

            loading = progressBar().lparams(matchParent, wrapContent)
            messageList = recycler { }.lparams(matchParent, matchParent) {
                topMargin = dip(10)
            }
        }
    }
}

