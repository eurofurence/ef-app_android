package org.eurofurence.connavigator.driver

import io.swagger.client.model.*
import org.eurofurence.connavigator.util.logd
import org.eurofurence.connavigator.util.loge

/**
 * Callback for the [Driver].
 */
interface DriverCallback {
    companion object {
        /**
         * Empty callback, does nothing.
         */
        val EMPTY = object : DriverCallback {
        }

        /**
         * System output callback.
         */
        val OUTPUT = object : DriverCallback {
            override fun gotEventConferenceDays(delta: List<EventConferenceDay>) {
                logd("DCB") { "gotEventConferenceDays, delta: ${delta.size}" }
            }

            override fun gotEventConferenceRooms(delta: List<EventConferenceRoom>) {
                logd("DCB") { "gotEventConferenceRooms, delta: ${delta.size}" }
            }

            override fun gotEventConferenceTracks(delta: List<EventConferenceTrack>) {
                logd("DCB") { "gotEventConferenceTracks, delta: ${delta.size}" }
            }

            override fun gotEvents(delta: List<EventEntry>) {
                logd("DCB") { "gotEvents, delta: ${delta.size}" }
            }

            override fun gotImages(delta: List<Image>) {
                logd("DCB") { "gotImages, delta: ${delta.size}" }
            }

            override fun gotInfo(delta: List<Info>) {
                logd("DCB") { "gotImages, delta: ${delta.size}" }
            }

            override fun gotInfoGroups(delta: List<InfoGroup>) {
                logd("DCB") { "gotInfoGroups, delta: ${delta.size}" }
            }

            override fun dispatched() {
                logd("DCB") { "dispatched" }
            }

            override fun done(success: Boolean) {
                if (success)
                    logd("DCB") { "done, success: $success" }
                else
                    loge("DBC") { "done, success: $success" }
            }
        }
    }

    /**
     * Called when the results of the event conference day database update were received.
     */
    fun gotEventConferenceDays(delta: List<EventConferenceDay>) {
        // Default is no operation
    }

    /**
     * Called when the results of the event conference rooms database update were received.
     */
    fun gotEventConferenceRooms(delta: List<EventConferenceRoom>) {
        // Default is no operation
    }

    /**
     * Called when the results of the event conference track database update were received.
     */
    fun gotEventConferenceTracks(delta: List<EventConferenceTrack>) {
        // Default is no operation
    }


    /**
     * Called when the results of the events database update were received.
     */
    fun gotEvents(delta: List<EventEntry>) {
        // Default is no operation
    }

    /**
     * Called when the results of the images database update were received.
     */
    fun gotImages(delta: List<Image>) {
        // Default is no operation
    }

    /**
     * Called when the results of the info database update were received.
     */
    fun gotInfo(delta: List<Info>) {
        // Default is no operation
    }

    /**
     * Called when the results of the images database update were received.
     */
    fun gotInfoGroups(delta: List<InfoGroup>) {
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
    fun done(success: Boolean) {
        // Default is no operation
    }

    /**
     * Concatenates the functions
     */
    operator fun plus(right: DriverCallback): DriverCallback = this.let { left ->
        object : DriverCallback {
            override fun gotEventConferenceDays(delta: List<EventConferenceDay>) {
                left.gotEventConferenceDays(delta)
                right.gotEventConferenceDays(delta)
            }

            override fun gotEventConferenceRooms(delta: List<EventConferenceRoom>) {
                left.gotEventConferenceRooms(delta)
                right.gotEventConferenceRooms(delta)
            }

            override fun gotEventConferenceTracks(delta: List<EventConferenceTrack>) {
                left.gotEventConferenceTracks(delta)
                right.gotEventConferenceTracks(delta)
            }

            override fun gotEvents(delta: List<EventEntry>) {
                left.gotEvents(delta)
                right.gotEvents(delta)
            }

            override fun gotImages(delta: List<Image>) {
                left.gotImages(delta)
                right.gotImages(delta)
            }

            override fun gotInfo(delta: List<Info>) {
                left.gotInfo(delta)
                right.gotInfo(delta)
            }

            override fun gotInfoGroups(delta: List<InfoGroup>) {
                left.gotInfoGroups(delta)
                right.gotInfoGroups(delta)
            }

            override fun dispatched() {
                left.dispatched()
                right.dispatched()
            }

            override fun done(success: Boolean) {
                left.done(success)
                right.done(success)
            }
        }
    }
}