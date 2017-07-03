package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pawegio.kandroid.longToast
import io.swagger.client.model.PrivateMessageRecord
import nl.komponents.kovenant.task
import nl.komponents.kovenant.ui.successUi
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.pref.AnalyticsPreferences
import org.eurofurence.connavigator.pref.AuthPreferences
import org.eurofurence.connavigator.tracking.Analytics
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.delegators.view
import org.eurofurence.connavigator.util.extensions.applyOnContent
import org.eurofurence.connavigator.util.extensions.applyOnRoot
import org.eurofurence.connavigator.util.extensions.recycler
import org.eurofurence.connavigator.webapi.apiService
import org.jetbrains.anko.*

/**
 * Created by requinard on 6/28/17.
 */
class FragmentViewMessages : Fragment(), ContentAPI, AnkoLogger, HasDb {
    override val db by lazy { locateDb() }
    val ui by lazy { MessagesUi() }

    var messages = emptyList<PrivateMessageRecord>()

    inner class MessageViewholder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val title: TextView by view()
        val text: TextView by view()
    }

    inner class MessageAdapter: RecyclerView.Adapter<MessageViewholder>(){
        override fun onBindViewHolder(holder: MessageViewholder, position: Int) {
            val message = messages[position]

            holder.title.text = message.subject
            holder.text.text = message.message
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MessageViewholder(
                SingleItemUi().createView(AnkoContext.Companion.create(context, parent))
        )

        override fun getItemCount() = messages.size

    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.Companion.create(container!!.context, container))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyOnRoot{ changeTitle("Private Messages") }

        info { "Filling in messages" }

        task {
            info { "Fetching messages" }
            apiService.communications.addHeader("Authorization", AuthPreferences.asBearer())
            messages = apiService.communications.apiV2CommunicationPrivateMessagesGet()
        } successUi  {
            info("Succesfully retrieved ${messages.size} messages")
            configureRecycler()
        } fail {
            warn { "Failed to retrieve messages" }
            longToast("Failed to retrieve messages!")
            Analytics.exception(it)
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
        verticalLayout {
            textView{
                id = R.id.title

                setTextAppearance(R.style.TextAppearance_AppCompat_Large)
            }

            textView{
                id = R.id.text
            }
        }
    }

}

class MessagesUi : AnkoComponent<ViewGroup> {
    lateinit var messageList: RecyclerView
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        verticalLayout {
            messageList = recycler { }
        }
    }

}