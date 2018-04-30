package org.eurofurence.connavigator.ui.communication

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import io.swagger.client.model.AnnouncementRecord
import io.swagger.client.model.DealerRecord
import io.swagger.client.model.EventRecord
import io.swagger.client.model.KnowledgeEntryRecord
/**
 * Created by Pazuzu on 12.04.2016.
 */
interface RootAPI {
    val tabs: TabLayout

    fun makeSnackbar(text: String)

    fun navigateToEvent(event: EventRecord)

    fun navigateToKnowledgeEntry(knowledgeEntry: KnowledgeEntryRecord)

    fun navigateToDealer(dealer: DealerRecord)

    fun navigateToAnnouncement(announcementRecord: AnnouncementRecord)

    fun changeTitle(text: String)

    fun changeTheme(newTheme: Int)
}