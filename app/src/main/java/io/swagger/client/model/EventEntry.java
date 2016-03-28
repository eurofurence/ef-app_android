package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventEntry extends EntityBase {
  
  @SerializedName("Description")
  private String description = null;
  @SerializedName("EndTime")
  private String endTime = null;
  @SerializedName("Abstract")
  private String _abstract = null;
  @SerializedName("StartTime")
  private String startTime = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("PanelHosts")
  private String panelHosts = null;
  @SerializedName("Duration")
  private String duration = null;
  @SerializedName("SourceEventId")
  private BigDecimal sourceEventId = null;
  @SerializedName("Slug")
  private String slug = null;
  @SerializedName("ConferenceDayId")
  private UUID conferenceDayId = null;
  @SerializedName("ConferenceRoomId")
  private UUID conferenceRoomId = null;
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("ConferenceTrackId")
  private UUID conferenceTrackId = null;

  
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
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
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
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
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
  public String getPanelHosts() {
    return panelHosts;
  }
  public void setPanelHosts(String panelHosts) {
    this.panelHosts = panelHosts;
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
  public BigDecimal getSourceEventId() {
    return sourceEventId;
  }
  public void setSourceEventId(BigDecimal sourceEventId) {
    this.sourceEventId = sourceEventId;
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
  public UUID getConferenceTrackId() {
    return conferenceTrackId;
  }
  public void setConferenceTrackId(UUID conferenceTrackId) {
    this.conferenceTrackId = conferenceTrackId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventEntry {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  endTime: ").append(endTime).append("\n");
    sb.append("  _abstract: ").append(_abstract).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  panelHosts: ").append(panelHosts).append("\n");
    sb.append("  duration: ").append(duration).append("\n");
    sb.append("  sourceEventId: ").append(sourceEventId).append("\n");
    sb.append("  slug: ").append(slug).append("\n");
    sb.append("  conferenceDayId: ").append(conferenceDayId).append("\n");
    sb.append("  conferenceRoomId: ").append(conferenceRoomId).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  conferenceTrackId: ").append(conferenceTrackId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
