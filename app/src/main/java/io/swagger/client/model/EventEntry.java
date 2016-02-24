package io.swagger.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;

import java.io.Serializable;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-02-24T14:49:45.074+01:00")
public class EventEntry extends EntityBase implements Serializable {
  
  private String conferenceTrackId = null;
  private String description = null;
  private String panelHosts = null;
  private String slug = null;
  private String title = null;
  private String conferenceRoomId = null;
  private String conferenceDayId = null;
  private Date lastChangeDateTimeUtc = null;
  private String duration = null;
  private BigDecimal isDeleted = null;
  private String endTime = null;
  private String id = null;
  private BigDecimal sourceEventId = null;
  private String startTime = null;
  private String _abstract = null;

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("ConferenceTrackId")
  public String getConferenceTrackId() {
    return conferenceTrackId;
  }
  public void setConferenceTrackId(String conferenceTrackId) {
    this.conferenceTrackId = conferenceTrackId;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("Description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("PanelHosts")
  public String getPanelHosts() {
    return panelHosts;
  }
  public void setPanelHosts(String panelHosts) {
    this.panelHosts = panelHosts;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("Slug")
  public String getSlug() {
    return slug;
  }
  public void setSlug(String slug) {
    this.slug = slug;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("Title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("ConferenceRoomId")
  public String getConferenceRoomId() {
    return conferenceRoomId;
  }
  public void setConferenceRoomId(String conferenceRoomId) {
    this.conferenceRoomId = conferenceRoomId;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("ConferenceDayId")
  public String getConferenceDayId() {
    return conferenceDayId;
  }
  public void setConferenceDayId(String conferenceDayId) {
    this.conferenceDayId = conferenceDayId;
  }

  
  /**
   * Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)
   **/
  
  @ApiModelProperty(required = true, value = "Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)")
  @JsonProperty("LastChangeDateTimeUtc")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("Duration")
  public String getDuration() {
    return duration;
  }
  public void setDuration(String duration) {
    this.duration = duration;
  }

  
  /**
   * Numeric flag that, if set to \"1\", indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval.
   * minimum: 0.0
   * maximum: 1.0
   **/
  
  @ApiModelProperty(required = true, value = "Numeric flag that, if set to \"1\", indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval.")
  @JsonProperty("IsDeleted")
  public BigDecimal getIsDeleted() {
    return isDeleted;
  }
  public void setIsDeleted(BigDecimal isDeleted) {
    this.isDeleted = isDeleted;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("EndTime")
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  
  /**
   * Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000
   **/
  
  @ApiModelProperty(required = true, value = "Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000")
  @JsonProperty("Id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("SourceEventId")
  public BigDecimal getSourceEventId() {
    return sourceEventId;
  }
  public void setSourceEventId(BigDecimal sourceEventId) {
    this.sourceEventId = sourceEventId;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("StartTime")
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  
  /**
   **/
  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("Abstract")
  public String getAbstract() {
    return _abstract;
  }
  public void setAbstract(String _abstract) {
    this._abstract = _abstract;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventEntry eventEntry = (EventEntry) o;
    return Objects.equals(conferenceTrackId, eventEntry.conferenceTrackId) &&
        Objects.equals(description, eventEntry.description) &&
        Objects.equals(panelHosts, eventEntry.panelHosts) &&
        Objects.equals(slug, eventEntry.slug) &&
        Objects.equals(title, eventEntry.title) &&
        Objects.equals(conferenceRoomId, eventEntry.conferenceRoomId) &&
        Objects.equals(conferenceDayId, eventEntry.conferenceDayId) &&
        Objects.equals(lastChangeDateTimeUtc, eventEntry.lastChangeDateTimeUtc) &&
        Objects.equals(duration, eventEntry.duration) &&
        Objects.equals(isDeleted, eventEntry.isDeleted) &&
        Objects.equals(endTime, eventEntry.endTime) &&
        Objects.equals(id, eventEntry.id) &&
        Objects.equals(sourceEventId, eventEntry.sourceEventId) &&
        Objects.equals(startTime, eventEntry.startTime) &&
        Objects.equals(_abstract, eventEntry._abstract);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conferenceTrackId, description, panelHosts, slug, title, conferenceRoomId, conferenceDayId, lastChangeDateTimeUtc, duration, isDeleted, endTime, id, sourceEventId, startTime, _abstract);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventEntry {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    conferenceTrackId: ").append(toIndentedString(conferenceTrackId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    panelHosts: ").append(toIndentedString(panelHosts)).append("\n");
    sb.append("    slug: ").append(toIndentedString(slug)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    conferenceRoomId: ").append(toIndentedString(conferenceRoomId)).append("\n");
    sb.append("    conferenceDayId: ").append(toIndentedString(conferenceDayId)).append("\n");
    sb.append("    lastChangeDateTimeUtc: ").append(toIndentedString(lastChangeDateTimeUtc)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    isDeleted: ").append(toIndentedString(isDeleted)).append("\n");
    sb.append("    endTime: ").append(toIndentedString(endTime)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sourceEventId: ").append(toIndentedString(sourceEventId)).append("\n");
    sb.append("    startTime: ").append(toIndentedString(startTime)).append("\n");
    sb.append("    _abstract: ").append(toIndentedString(_abstract)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

