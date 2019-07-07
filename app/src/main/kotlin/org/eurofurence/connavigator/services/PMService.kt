package org.eurofurence.connavigator.services

import io.reactivex.Observable
import io.reactivex.annotations.Experimental
import io.reactivex.subjects.BehaviorSubject
import io.swagger.client.model.PrivateMessageRecord
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.eurofurence.connavigator.preferences.AuthPreferences
import org.eurofurence.connavigator.util.extensions.now
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.joda.time.*
import java.util.*

/**
 * Private message service. Calls are not indirected to background tasks, so subscription and
 * resolution must handle running in a proper context themselves.
 */
object PMService : AnkoLogger {
    /**
     * All messages from the last update, if it should be up to date, [fetch] must be called.
     */
    var all = emptyMap<UUID, PrivateMessageRecord>()
        private set

    /**
     * This subject will be notified on completed fetches, will initially be empty.
     */
    val updated: BehaviorSubject<Map<UUID, PrivateMessageRecord>> =
            BehaviorSubject.createDefault(all)

    /**
     * Observes a single key in the message items.
     */
    fun observeFind(messageId: UUID) =
            updated.flatMap { items ->
                // Get item at message ID, if present, return just it, otherwise empty.
                items[messageId]?.let {
                    Observable.just(it)
                } ?: Observable.empty()
            }

    /**
     * Invalidates the cache.
     */
    fun invalidate() {
        all = emptyMap()
        updated.onNext(all)
    }

    /**
     * Updates the data, notifies subjects.
     * @param validFor The time a fetch is considered valid for. If last fetch time is this time ago, a new fetch
     * will be issued.
     */
    fun fetch() {
        // Only update on being logged in, otherwise set empty and return.
        if (!AuthPreferences.isLoggedIn)
            return

        info { "Fetching PM data." }

        // Call API method to get all private message records and store them.
        apiService.communications
                .addHeader("Authorization", AuthPreferences.asBearer())
        val next = apiService.communications
                .apiCommunicationPrivateMessagesGet()
                .associateBy(PrivateMessageRecord::getId)

        // Notify subject of successful messages, if they are actually different.
        if (next != all) {
            all = next
            updated.onNext(all)
        }

    }

    /**
     * Stores currently executing promises to prevent unnecessary calls.
     */
    private var lastFetchPromise: Promise<Unit, Exception>? = null

    /**
     * Performs a fetch in the background as a task.
     */
    fun fetchInBackground(): Promise<Unit, Exception> {
        // Return same promise if running.
        return lastFetchPromise
                ?: task { fetch().also { lastFetchPromise = null } }.also { lastFetchPromise = it }
    }

    /**
     * Finds the message for the given ID. If not in cache, a call to [fetch] will be dispatched.
     */
    fun find(messageId: UUID): PrivateMessageRecord? {
        // Get existing message and return
        val existing = all[messageId]
        if (existing != null)
            return existing

        // Fetch and return value, return null if failed.
        return try {
            fetch()
            all[messageId]
        } catch (t: Throwable) {
            null
        }
    }

    /**
     * Notifies the underlying listener that an element's content was modified.
     *
     * Implications are not completely analyzed so use with cation.
     */
    @Experimental
    fun notifyModified(record: PrivateMessageRecord) {
        // Assert non-disconnected item.
        check(all[record.id] == record)

        // Push existing map, as changes are element based.
        updated.onNext(all)
    }
}