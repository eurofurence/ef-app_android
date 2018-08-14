@file:Suppress("unused")

package org.eurofurence.connavigator.util.v2

import io.swagger.client.model.*
import java.util.*

/**
 * An abstract form of the delta case classes.
 */
class AbstractDelta<T>(
        val id: (T) -> UUID,
        val last: Date,
        val deltaStart: Date,
        val clearBeforeInsert: Boolean,
        val changed: List<T>,
        val deleted: List<UUID>)

fun DeltaResponseAnnouncementRecord.convert() =
        AbstractDelta(AnnouncementRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseDealerRecord.convert() =
        AbstractDelta(DealerRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseEventConferenceDayRecord.convert() =
        AbstractDelta(EventConferenceDayRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseEventConferenceRoomRecord.convert() =
        AbstractDelta(EventConferenceRoomRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseEventConferenceTrackRecord.convert() =
        AbstractDelta(EventConferenceTrackRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseEventRecord.convert() =
        AbstractDelta(EventRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseImageRecord.convert() =
        AbstractDelta(ImageRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseKnowledgeEntryRecord.convert() =
        AbstractDelta(KnowledgeEntryRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseKnowledgeGroupRecord.convert() =
        AbstractDelta(KnowledgeGroupRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)

fun DeltaResponseMapRecord.convert() =
        AbstractDelta(MapRecord::getId,
                storageLastChangeDateTimeUtc,
                storageDeltaStartChangeDateTimeUtc,
                removeAllBeforeInsert,
                changedEntities,
                deletedEntities)