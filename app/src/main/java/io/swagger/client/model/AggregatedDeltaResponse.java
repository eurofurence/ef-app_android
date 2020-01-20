package io.swagger.client.model;

import io.swagger.client.model.DeltaResponseAnnouncementRecord;
import io.swagger.client.model.DeltaResponseDealerRecord;
import io.swagger.client.model.DeltaResponseEventConferenceDayRecord;
import io.swagger.client.model.DeltaResponseEventConferenceRoomRecord;
import io.swagger.client.model.DeltaResponseEventConferenceTrackRecord;
import io.swagger.client.model.DeltaResponseEventRecord;
import io.swagger.client.model.DeltaResponseImageRecord;
import io.swagger.client.model.DeltaResponseKnowledgeEntryRecord;
import io.swagger.client.model.DeltaResponseKnowledgeGroupRecord;
import io.swagger.client.model.DeltaResponseMapRecord;
import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class AggregatedDeltaResponse  {

  @SerializedName("ConventionIdentifier")
  private String conventionIdentifier = null;
  @SerializedName("State")
  private String state = null;
  @SerializedName("Since")
  private Date since = null;
  @SerializedName("CurrentDateTimeUtc")
  private Date currentDateTimeUtc = null;
  @SerializedName("Events")
  private DeltaResponseEventRecord events = null;
  @SerializedName("EventConferenceDays")
  private DeltaResponseEventConferenceDayRecord eventConferenceDays = null;
  @SerializedName("EventConferenceRooms")
  private DeltaResponseEventConferenceRoomRecord eventConferenceRooms = null;
  @SerializedName("EventConferenceTracks")
  private DeltaResponseEventConferenceTrackRecord eventConferenceTracks = null;
  @SerializedName("KnowledgeGroups")
  private DeltaResponseKnowledgeGroupRecord knowledgeGroups = null;
  @SerializedName("KnowledgeEntries")
  private DeltaResponseKnowledgeEntryRecord knowledgeEntries = null;
  @SerializedName("Images")
  private DeltaResponseImageRecord images = null;
  @SerializedName("Dealers")
  private DeltaResponseDealerRecord dealers = null;
  @SerializedName("Announcements")
  private DeltaResponseAnnouncementRecord announcements = null;
  @SerializedName("Maps")
  private DeltaResponseMapRecord maps = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getConventionIdentifier() {
    return conventionIdentifier;
  }
  public void setConventionIdentifier(String conventionIdentifier) {
    this.conventionIdentifier = conventionIdentifier;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getSince() {
    return since;
  }
  public void setSince(Date since) {
    this.since = since;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCurrentDateTimeUtc() {
    return currentDateTimeUtc;
  }
  public void setCurrentDateTimeUtc(Date currentDateTimeUtc) {
    this.currentDateTimeUtc = currentDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseEventRecord getEvents() {
    return events;
  }
  public void setEvents(DeltaResponseEventRecord events) {
    this.events = events;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseEventConferenceDayRecord getEventConferenceDays() {
    return eventConferenceDays;
  }
  public void setEventConferenceDays(DeltaResponseEventConferenceDayRecord eventConferenceDays) {
    this.eventConferenceDays = eventConferenceDays;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseEventConferenceRoomRecord getEventConferenceRooms() {
    return eventConferenceRooms;
  }
  public void setEventConferenceRooms(DeltaResponseEventConferenceRoomRecord eventConferenceRooms) {
    this.eventConferenceRooms = eventConferenceRooms;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseEventConferenceTrackRecord getEventConferenceTracks() {
    return eventConferenceTracks;
  }
  public void setEventConferenceTracks(DeltaResponseEventConferenceTrackRecord eventConferenceTracks) {
    this.eventConferenceTracks = eventConferenceTracks;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseKnowledgeGroupRecord getKnowledgeGroups() {
    return knowledgeGroups;
  }
  public void setKnowledgeGroups(DeltaResponseKnowledgeGroupRecord knowledgeGroups) {
    this.knowledgeGroups = knowledgeGroups;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseKnowledgeEntryRecord getKnowledgeEntries() {
    return knowledgeEntries;
  }
  public void setKnowledgeEntries(DeltaResponseKnowledgeEntryRecord knowledgeEntries) {
    this.knowledgeEntries = knowledgeEntries;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseImageRecord getImages() {
    return images;
  }
  public void setImages(DeltaResponseImageRecord images) {
    this.images = images;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseDealerRecord getDealers() {
    return dealers;
  }
  public void setDealers(DeltaResponseDealerRecord dealers) {
    this.dealers = dealers;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseAnnouncementRecord getAnnouncements() {
    return announcements;
  }
  public void setAnnouncements(DeltaResponseAnnouncementRecord announcements) {
    this.announcements = announcements;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public DeltaResponseMapRecord getMaps() {
    return maps;
  }
  public void setMaps(DeltaResponseMapRecord maps) {
    this.maps = maps;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AggregatedDeltaResponse aggregatedDeltaResponse = (AggregatedDeltaResponse) o;
    return (conventionIdentifier == null ? aggregatedDeltaResponse.conventionIdentifier == null : conventionIdentifier.equals(aggregatedDeltaResponse.conventionIdentifier)) &&
        (state == null ? aggregatedDeltaResponse.state == null : state.equals(aggregatedDeltaResponse.state)) &&
        (since == null ? aggregatedDeltaResponse.since == null : since.equals(aggregatedDeltaResponse.since)) &&
        (currentDateTimeUtc == null ? aggregatedDeltaResponse.currentDateTimeUtc == null : currentDateTimeUtc.equals(aggregatedDeltaResponse.currentDateTimeUtc)) &&
        (events == null ? aggregatedDeltaResponse.events == null : events.equals(aggregatedDeltaResponse.events)) &&
        (eventConferenceDays == null ? aggregatedDeltaResponse.eventConferenceDays == null : eventConferenceDays.equals(aggregatedDeltaResponse.eventConferenceDays)) &&
        (eventConferenceRooms == null ? aggregatedDeltaResponse.eventConferenceRooms == null : eventConferenceRooms.equals(aggregatedDeltaResponse.eventConferenceRooms)) &&
        (eventConferenceTracks == null ? aggregatedDeltaResponse.eventConferenceTracks == null : eventConferenceTracks.equals(aggregatedDeltaResponse.eventConferenceTracks)) &&
        (knowledgeGroups == null ? aggregatedDeltaResponse.knowledgeGroups == null : knowledgeGroups.equals(aggregatedDeltaResponse.knowledgeGroups)) &&
        (knowledgeEntries == null ? aggregatedDeltaResponse.knowledgeEntries == null : knowledgeEntries.equals(aggregatedDeltaResponse.knowledgeEntries)) &&
        (images == null ? aggregatedDeltaResponse.images == null : images.equals(aggregatedDeltaResponse.images)) &&
        (dealers == null ? aggregatedDeltaResponse.dealers == null : dealers.equals(aggregatedDeltaResponse.dealers)) &&
        (announcements == null ? aggregatedDeltaResponse.announcements == null : announcements.equals(aggregatedDeltaResponse.announcements)) &&
        (maps == null ? aggregatedDeltaResponse.maps == null : maps.equals(aggregatedDeltaResponse.maps));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (conventionIdentifier == null ? 0: conventionIdentifier.hashCode());
    result = 31 * result + (state == null ? 0: state.hashCode());
    result = 31 * result + (since == null ? 0: since.hashCode());
    result = 31 * result + (currentDateTimeUtc == null ? 0: currentDateTimeUtc.hashCode());
    result = 31 * result + (events == null ? 0: events.hashCode());
    result = 31 * result + (eventConferenceDays == null ? 0: eventConferenceDays.hashCode());
    result = 31 * result + (eventConferenceRooms == null ? 0: eventConferenceRooms.hashCode());
    result = 31 * result + (eventConferenceTracks == null ? 0: eventConferenceTracks.hashCode());
    result = 31 * result + (knowledgeGroups == null ? 0: knowledgeGroups.hashCode());
    result = 31 * result + (knowledgeEntries == null ? 0: knowledgeEntries.hashCode());
    result = 31 * result + (images == null ? 0: images.hashCode());
    result = 31 * result + (dealers == null ? 0: dealers.hashCode());
    result = 31 * result + (announcements == null ? 0: announcements.hashCode());
    result = 31 * result + (maps == null ? 0: maps.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AggregatedDeltaResponse {\n");

    sb.append("  conventionIdentifier: ").append(conventionIdentifier).append("\n");
    sb.append("  state: ").append(state).append("\n");
    sb.append("  since: ").append(since).append("\n");
    sb.append("  currentDateTimeUtc: ").append(currentDateTimeUtc).append("\n");
    sb.append("  events: ").append(events).append("\n");
    sb.append("  eventConferenceDays: ").append(eventConferenceDays).append("\n");
    sb.append("  eventConferenceRooms: ").append(eventConferenceRooms).append("\n");
    sb.append("  eventConferenceTracks: ").append(eventConferenceTracks).append("\n");
    sb.append("  knowledgeGroups: ").append(knowledgeGroups).append("\n");
    sb.append("  knowledgeEntries: ").append(knowledgeEntries).append("\n");
    sb.append("  images: ").append(images).append("\n");
    sb.append("  dealers: ").append(dealers).append("\n");
    sb.append("  announcements: ").append(announcements).append("\n");
    sb.append("  maps: ").append(maps).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
