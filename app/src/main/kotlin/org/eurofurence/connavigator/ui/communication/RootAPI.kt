package org.eurofurence.connavigator.ui.communication

import android.content.SharedPreferences
import android.support.design.widget.TabLayout
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.swagger.client.model.Dealer
import io.swagger.client.model.EventEntry
import io.swagger.client.model.Info
import org.eurofurence.connavigator.database.Database
import org.eurofurence.connavigator.pref.RemoteConfig

/**
 * Created by Pazuzu on 12.04.2016.
 */
interface RootAPI {
    val database: Database

    val tabs: TabLayout

    val preferences: SharedPreferences

    val remotePreferences: RemoteConfig

    fun makeSnackbar(text: String)

    fun navigateToEvent(eventEntry: EventEntry)

    fun navigateToInfo(info: Info)

    fun navigateToDealer(dealer: Dealer)

    fun changeTitle(text: String)

    fun changeTheme(newTheme: Int)
}