package org.eurofurence.connavigator.util.extensions

import android.content.Context
import android.util.Base64
import android.util.Log
import com.pawegio.kandroid.i
import io.swagger.client.ApiException
import io.swagger.client.model.*
import org.eurofurence.connavigator.BuildConfig
import org.eurofurence.connavigator.R
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.services.PMService
import org.eurofurence.connavigator.services.apiService
import java.util.*

fun MapRecord.findMatchingEntries(x: Float, y: Float) =
        entries
                .orEmpty()
                .filter { (x - (it.x ?: 0) power 2F) + (y - (it.y ?: 0) power 2F) < (it.tapRadius ?: 0) power 2 }

fun MapRecord.findMatchingEntry(x: Float, y: Float) =
        findMatchingEntries(x,y)
                .takeIf { it.isNotEmpty() }
                ?.reduce { acc, mapEntryRecord -> if (mapEntryRecord.distance2(x,y) < acc.distance2(x,y)) mapEntryRecord else acc }

fun MapEntryRecord.distance2(otherX: Float, otherY: Float) =
        ((x - otherX) power 2F) + ((y - otherY) power 2F)


val ImageRecord.url: String get() = "${apiService.apiPath}/Api/Images/$id/Content/with-hash:${Base64.encodeToString(contentHashSha1.toByteArray(), Base64.NO_WRAP)}"

fun PrivateMessageRecord.markAsRead() {
    if (AuthPreferences.isLoggedIn) {
        Log.i("PMR", "Marking message ${this.id} as read")

        try {
            // Set read time from API and notify.
            apiService.communications.addHeader("Authorization", AuthPreferences.asBearer())
            readDateTimeUtc = apiService.communications.apiCommunicationPrivateMessagesByMessageIdReadPost(this.id, true)
            PMService.notifyModified(this)
        } catch (e: ApiException) {
            // Actual failure, throw.
            throw e
        } catch (e: Throwable) {
            // Warn for non-API errors but do not fail.
            Log.w("PMR", "Non-API error while marking the message as read.", e)

            // Set read time and notify, but from local time as a substitute.
            readDateTimeUtc = Date()
            PMService.notifyModified(this)
        }

    } else {
        throw Exception("User is not logged in!")
    }
}

fun EventRecord.fullTitle(): String {
    val builder = StringBuilder(this.title.trim().removeSuffix("\n"))

    subTitle?.let {
        if (it.isNotEmpty()) {
            builder.append(": ")
            builder.append(it.trim().removeSuffix("\n"))
        }
    }
    return builder.toString()
}

fun EventRecord.startTimeString(): String = startDateTimeUtc.jodatime().toString("HH:mm")
fun EventRecord.endTimeString(): String = endDateTimeUtc.jodatime().toString("HH:mm")
fun EventRecord.ownerString(): String = "Hosted by $panelHosts"
fun EventRecord.shareString(ctx: Context): String = ctx.getString(R.string.event_check_out_url, this.title, createUrl("Events", this.id))

fun DealerRecord.hasUniqueDisplayName() = (this.displayName.isNotEmpty() && this.displayName != this.attendeeNickname)
fun DealerRecord.getName(): String? = if (this.hasUniqueDisplayName()) this.displayName else this.attendeeNickname
fun DealerRecord.allDaysAvailable() = listOf(this.attendsOnThursday, this.attendsOnFriday, this.attendsOnSaturday).all { it == true }
fun DealerRecord.shareString(ctx: Context) = ctx.getString(R.string.dealer_check_out_dealer_url, this.displayName, createUrl("Dealers", this.id))

fun KnowledgeEntryRecord.shareString(ctx: Context) = ctx.getString(R.string.misc_info_share, this.title, createUrl("KnowledgeEntries", this.id))

fun createUrl(type: String, id: UUID): String {
    return "https://app.eurofurence.org/${BuildConfig.CONVENTION_IDENTIFIER}/Web/$type/$id"
}