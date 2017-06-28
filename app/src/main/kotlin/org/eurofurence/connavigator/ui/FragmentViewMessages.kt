package org.eurofurence.connavigator.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.nitrico.lastadapter.LastAdapter
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.database.HasDb
import org.eurofurence.connavigator.database.locateDb
import org.eurofurence.connavigator.ui.communication.ContentAPI
import org.eurofurence.connavigator.util.extensions.recycler
import org.jetbrains.anko.*

/**
 * Created by requinard on 6/28/17.
 */
class FragmentViewMessages : Fragment(), ContentAPI, AnkoLogger, HasDb {
    override val db by lazy { locateDb() }
    val ui by lazy { MessagesUi() }

    val messages = listOf<PrivateMessageRecord>(PrivateMessageRecord().apply {
        message = "Hello world"
        authorName = "Requinard"
    })

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            ui.createView(AnkoContext.Companion.create(container!!.context, container))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        info { "Filling in messages" }
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