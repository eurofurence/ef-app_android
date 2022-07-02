package org.eurofurence.connavigator.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pawegio.kandroid.longToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.dropins.*
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.ui.views.FontAwesomeType
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.toRelative

/**
 * Created by requinard on 6/28/17.
 */
class FragmentViewMessageList : DisposingFragment(), AnkoLogger, HasDb {
    override val db by lazy { locateDb() }

    lateinit var loading: ProgressBar
    lateinit var messageList: RecyclerView

    /**
     * The list of currently displayed messages.
     */
    var messages = emptyList<PrivateMessageRecord>()

    inner class MessageViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView by view()
        val icon: TextView by view() // TODO: Icon text view.
        val date: TextView by view()
        val layout: ViewGroup by view()
    }

    inner class MessageAdapter : RecyclerView.Adapter<MessageViewholder>() {
        override fun onBindViewHolder(holder: MessageViewholder, position: Int) {
            val message = messages[position]

            holder.title.text = message.subject
            holder.date.text = getString(
                R.string.misc_concat_newline,
                getString(R.string.message_from_name, message.authorName),
                getString(
                    R.string.message_sent_date, message.createdDateTimeUtc?.toRelative()
                        ?: getString(R.string.misc_not_yet)
                )
            )

            if (message.readDateTimeUtc != null) {
                holder.icon.textColorResource = android.R.color.tertiary_text_dark
                holder.icon.text = getString(R.string.fa_envelope_open)
                holder.icon.textSize = 30f

            } else {
                holder.icon.textColorResource = R.color.primaryDark
                holder.icon.text = getString(R.string.fa_envelope)
                holder.icon.textSize = 30f
            }

            holder.layout.setOnClickListener {
                val action = FragmentViewMessageListDirections
                    .actionFragmentViewMessageListToFragmentViewMessageItem(message.id.toString())
                findNavController().navigate(action)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            MessageViewholder(parent.context.createView {
                linearLayout {
                    layoutParams = viewGroupLayoutParams(matchParent, wrapContent)
                    backgroundResource = R.color.lightBackground
                    setPadding(0, dip(10), 0, dip(10))
                    weightSum = 100F
                    id = R.id.layout
                    isClickable = true

                    fontAwesomeView {
                        layoutParams = linearLayoutParams(dip(0), matchParent, 15F)
                        id = R.id.icon
                        text = getString(R.string.fa_envelope)
                        textSize = 30f
                        gravity = Gravity.CENTER_VERTICAL
                        setPadding(dip(20), 0, 0, 0)
                    }

                    verticalLayout {
                        layoutParams = linearLayoutParams(dip(0), wrapContent, 75F)
                        textView {
                            id = R.id.title
                            compatAppearance = android.R.style.TextAppearance_Medium
                        }

                        textView {
                            id = R.id.date
                            compatAppearance = android.R.style.TextAppearance_Small
                        }
                    }

                    fontAwesomeView {
                        type= FontAwesomeType.Solid
                        layoutParams = linearLayoutParams(dip(0), matchParent, 10F)
                        text = getString(R.string.fa_chevron_right_solid)
                        textSize = 30f
                        gravity = Gravity.CENTER
                    }
                }
            }
            )

        override fun getItemCount() = messages.size

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createView {
        verticalLayout {
            layoutParams = viewGroupLayoutParams(matchParent, matchParent)

            loading = progressBar {
                layoutParams = linearLayoutParams(matchParent, wrapContent)
            }

            messageList = recycler {
                layoutParams = linearLayoutParams(matchParent, matchParent) {
                    topMargin = dip(10)
                }
            }
        }
    }

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
                loading.visibility = View.GONE
                messageList.visibility = View.VISIBLE

                // Assign messages, use proper sorting.
                messages = it.values.sortedWith(standardSorting)

                // Notify the data set of the change.
                messageList.adapter?.notifyDataSetChanged()

            }, {
                // On error, display a long toast indicating error and set nothing visible.
                loading.visibility = View.GONE
                messageList.visibility = View.GONE

                warn { "Failed to retrieve messages" }
                longToast(getString(R.string.message_failed_to_retrieve))
            }, {
                // Will never be completed.
            }, {
                // Prepare by displaying loading only.
                loading.visibility = View.VISIBLE
                messageList.visibility = View.GONE
            })
            .collectOnDestroyView()
    }

    private fun configureRecycler() {
        messageList.adapter = MessageAdapter()
        messageList.layoutManager = LinearLayoutManager(activity)
        messageList.itemAnimator = DefaultItemAnimator()
    }

}
