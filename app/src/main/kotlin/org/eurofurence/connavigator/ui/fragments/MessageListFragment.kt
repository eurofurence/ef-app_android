package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joanzapata.iconify.widget.IconTextView
import com.pawegio.kandroid.longToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.fontAwesomeView
import org.eurofurence.connavigator.util.extensions.recycler
import org.eurofurence.connavigator.util.extensions.toRelative
import org.eurofurence.connavigator.util.v2.compatAppearance
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.UI

/**
 * Created by requinard on 6/28/17.
 */
class FragmentViewMessageList : DisposingFragment(), AnkoLogger, HasDb {
    override val db by lazy { locateDb() }
    val ui by lazy { MessagesUi() }

    /**
     * The list of currently displayed messages.
     */
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
                    getString(R.string.message_sent_date, message.createdDateTimeUtc?.toRelative()
                            ?: getString(R.string.misc_not_yet))
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
                        .actionFragmentViewMessageListToFragmentViewMessageItem(message.id.toString())
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

        // Configure the recycler, TODO: use auto adapter.
        configureRecycler()

        // Subscribe to the actual list of PMs.
        subscribeToPMs()
    }

    /**
     * The default sorting, put unread messages at the top and sorts by creation time after.
     */
    private val standardSorting = compareBy<PrivateMessageRecord> {
        it.readDateTimeUtc != null
    }.thenByDescending {
        it.createdDateTimeUtc
    }

    /**
     * Subscribes to the PMs by piping them into the adapter (and indicating some loading time if necessary).
     */
    private fun subscribeToPMs() {
        PMService.updated
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // Next item, assert proper visibility.
                    ui.loading.visibility = View.GONE
                    ui.messageList.visibility = View.VISIBLE

                    // Assign messages, use proper sorting.
                    messages = it.values.sortedWith(standardSorting)

                    // Notify the data set of the change.
                    ui.messageList.adapter?.notifyDataSetChanged()

                }, {
                    // On error, display a long toast indicating error and set nothing visible.
                    ui.loading.visibility = View.GONE
                    ui.messageList.visibility = View.GONE

                    warn { "Failed to retrieve messages" }
                    longToast(getString(R.string.message_failed_to_retrieve))
                }, {
                    // Will never be completed.
                }, {
                    // Prepare by displaying loading only.
                    ui.loading.visibility = View.VISIBLE
                    ui.messageList.visibility = View.GONE
                })
                .collectOnDestroyView()
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
            backgroundResource = R.color.lightBackground
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

