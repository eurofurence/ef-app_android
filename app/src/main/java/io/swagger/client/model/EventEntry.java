package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventEntry extends EntityBase {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
  @SerializedName("SourceEventId")
  private BigDecimal sourceEventId = null;
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Slug")
  private String slug = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("ConferenceTrackId")
  private UUID conferenceTrackId = null;
  @SerializedName("ConferenceDayId")
  private UUID conferenceDayId = null;
  @SerializedName("ConferenceRoomId")
  private UUID conferenceRoomId = null;
  @SerializedName("Abstract")
  private String _abstract = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("StartTime")
  private String startTime = null;
  @SerializedName("EndTime")
  private String endTime = null;
  @SerializedName("Duration")
  private String duration = null;
  @SerializedName("PanelHosts")
  private String panelHosts = null;

  
  /**
   * Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000
   **/
  @ApiModelProperty(required = true, value = "Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000")
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

  
  /**
   * Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)
   **/
  @ApiModelProperty(required = true, value = "Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  
  /**
   * Numeric flag that, if set to \"1\", indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval.
   * minimum: 0.0
   * maximum: 1.0
   **/
  @ApiModelProperty(required = true, value = "Numeric flag that, if set to \"1\", indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval.")
  public BigDecimal getIsDeleted() {
    return isDeleted;
  }
  public void setIsDeleted(BigDecimal isDeleted) {
    this.isDeleted = isDeleted;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public BigDecimal getSourceEventId() {
    return sourceEventId;
  }
  public void setSourceEventId(BigDecimal sourceEventId) {
    this.sourceEventId = sourceEventId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getImageId() {
    return imageId;
  }
  public void setImageId(UUID imageId) {
    this.imageId = imageId;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getSlug() {
    return slug;
  }
  public void setSlug(String slug) {
    this.slug = slug;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getConferenceTrackId() {
    return conferenceTrackId;
  }
  public void setConferenceTrackId(UUID conferenceTrackId) {
    this.conferenceTrackId = conferenceTrackId;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getConferenceDayId() {
    return conferenceDayId;
  }
  public void setConferenceDayId(UUID conferenceDayId) {
    this.conferenceDayId = conferenceDayId;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getConferenceRoomId() {
    return conferenceRoomId;
  }
  public void setConferenceRoomId(UUID conferenceRoomId) {
    this.conferenceRoomId = conferenceRoomId;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getAbstract() {
    return _abstract;
  }
  public void setAbstract(String _abstract) {
    this._abstract = _abstract;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getDuration() {
    return duration;
  }
  public void setDuration(String duration) {
    this.duration = duration;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getPanelHosts() {
    return panelHosts;
  }
  public void setPanelHosts(String panelHosts) {
    this.panelHosts = panelHosts;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventEntry {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
    sb.append("  sourceEventId: ").append(sourceEventId).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  slug: ").append(slug).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  conferenceTrackId: ").append(conferenceTrackId).append("\n");
    sb.append("  conferenceDayId: ").append(conferenceDayId).append("\n");
    sb.append("  conferenceRoomId: ").append(conferenceRoomId).append("\n");
    sb.append("  _abstract: ").append(_abstract).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  endTime: ").append(endTime).append("\n");
    sb.append("  duration: ").append(duration).append("\n");
    sb.append("  panelHosts: ").append(panelHosts).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
