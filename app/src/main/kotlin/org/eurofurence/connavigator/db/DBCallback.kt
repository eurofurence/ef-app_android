package org.eurofurence.connavigator.db

import io.swagger.client.model.*

/**
 * Callback for the [DBService].
 */
interface DBCallback {
    companion object {
        /**
         * Empty callback, does nothing
         */
        val EMPTY: DBCallback = object : DBCallback {
        }
    }

    /**
     * Called when the results of the event conference day database update were received.
     */
    fun gotEventConferenceDays(values: List<EventConferenceDay>) {
        // Default is no operation
    }

    /**
     * Called when the results of the event conference rooms database update were received.
     */
    fun gotEventConferenceRooms(values: List<EventConferenceRoom>) {
        // Default is no operation
    }

    /**
     * Called when the results of the event conference track database update were received.
     */
    fun gotEventConferenceTracks(values: List<EventConferenceTrack>) {
        // Default is no operation
    }


    /**
     * Called when the results of the events database update were received.
     */
    fun gotEvents(values: List<EventEntry>) {
        // Default is no operation
    }

    /**
     * Called when the results of the images database update were received.
     */
    fun gotImages(values: List<Image>) {
        // Default is no operation
    }

    /**
     * Called when the results of the info database update were received.
     */
    fun gotInfo(values: List<Info>) {
        // Default is no operation
    }

    /**
     * Called when the results of the images database update were received.
     */
    fun gotInfoGroups(values: List<InfoGroup>) {
        // Default is no operation
    }

    /**
     * Called when database queries are dispatched.
     */
    fun dispatched() {
        // Default is no operation
    }

    /**
     * Called when all queries are done.
     */
    fun done() {
        // Default is no operation
    }

    /**
     * Concatenates the functions
     */
    operator fun plus(right: DBCallback): DBCallback = this.let { left ->
        object : DBCallback {
            override fun gotEventConferenceDays(values: List<EventConferenceDay>) {
                left.gotEventConferenceDays(values)
                right.gotEventConferenceDays(values)
            }

            override fun gotEventConferenceRooms(values: List<EventConferenceRoom>) {
                left.gotEventConferenceRooms(values)
                right.gotEventConferenceRooms(values)
            }

            override fun gotEventConferenceTracks(values: List<EventConferenceTrack>) {
                left.gotEventConferenceTracks(values)
                right.gotEventConferenceTracks(values)
            }

            override fun gotEvents(values: List<EventEntry>) {
                left.gotEvents(values)
                right.gotEvents(values)
            }

            override fun gotImages(values: List<Image>) {
                left.gotImages(values)
                right.gotImages(values)
            }

            override fun gotInfo(values: List<Info>) {
                left.gotInfo(values)
                right.gotInfo(values)
            }

            override fun gotInfoGroups(values: List<InfoGroup>) {
                left.gotInfoGroups(values)
                right.gotInfoGroups(values)
            }

            override fun dispatched() {
                left.dispatched()
                right.dispatched()
            }

            override fun done() {
                left.done()
                right.done()
            }
        }
    }
}