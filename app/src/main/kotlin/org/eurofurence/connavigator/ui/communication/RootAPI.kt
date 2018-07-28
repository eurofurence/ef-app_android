package org.eurofurence.connavigator.ui.communication

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import io.swagger.client.model.AnnouncementRecord
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.EventRecord
import io.swagger.client.model.KnowledgeEntryRecord
import io.swagger.client.model.PrivateMessageRecord
import org.eurofurence.connavigator.ui.ActionBarMode

/**
 * Created by Pazuzu on 12.04.2016.
 */
interface RootAPI {
    val tabs: TabLayout

    fun makeSnackbar(text: String)

    fun navigateToEvent(event: EventRecord)

    fun navigateToKnowledgeEntry(knowledgeEntry: KnowledgeEntryRecord)

    fun navigateToDealer(dealer: DealerRecord)

    fun navigateToMessage(message: PrivateMessageRecord)

    fun navigateToAnnouncement(announcementRecord: AnnouncementRecord)

    fun <T : Fragment> navigateRoot(type: Class<T>, mode: ActionBarMode = ActionBarMode.NONE)

    fun changeTitle(text: String)

    fun changeTheme(newTheme: Int)

    fun popDetails()
}