package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventEntry extends EntityBase {
  
  @SerializedName("SourceEventId")
  private BigDecimal sourceEventId = null;
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Slug")
  private String slug = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("SubTitle")
  private String subTitle = null;
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
  @SerializedName("IsDeviatingFromConBook")
  private BigDecimal isDeviatingFromConBook = null;

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
  public String getSubTitle() {
    return subTitle;
  }
  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
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

  /**
   * Numeric flag that, if set to \"1\", indicates that the record is differing from the original schedule in the con book. This usually indicates that the event was rescheduled after the con book was printed.
   * minimum: 0.0
   * maximum: 1.0
   **/
  @ApiModelProperty(required = true, value = "Numeric flag that, if set to \"1\", indicates that the record is differing from the original schedule in the con book. This usually indicates that the event was rescheduled after the con book was printed.")
  public BigDecimal getIsDeviatingFromConBook() {
    return isDeviatingFromConBook;
  }
  public void setIsDeviatingFromConBook(BigDecimal isDeviatingFromConBook) {
    this.isDeviatingFromConBook = isDeviatingFromConBook;
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
    return (sourceEventId == null ? eventEntry.sourceEventId == null : sourceEventId.equals(eventEntry.sourceEventId)) &&
        (imageId == null ? eventEntry.imageId == null : imageId.equals(eventEntry.imageId)) &&
        (slug == null ? eventEntry.slug == null : slug.equals(eventEntry.slug)) &&
        (title == null ? eventEntry.title == null : title.equals(eventEntry.title)) &&
        (subTitle == null ? eventEntry.subTitle == null : subTitle.equals(eventEntry.subTitle)) &&
        (conferenceTrackId == null ? eventEntry.conferenceTrackId == null : conferenceTrackId.equals(eventEntry.conferenceTrackId)) &&
        (conferenceDayId == null ? eventEntry.conferenceDayId == null : conferenceDayId.equals(eventEntry.conferenceDayId)) &&
        (conferenceRoomId == null ? eventEntry.conferenceRoomId == null : conferenceRoomId.equals(eventEntry.conferenceRoomId)) &&
        (_abstract == null ? eventEntry._abstract == null : _abstract.equals(eventEntry._abstract)) &&
        (description == null ? eventEntry.description == null : description.equals(eventEntry.description)) &&
        (startTime == null ? eventEntry.startTime == null : startTime.equals(eventEntry.startTime)) &&
        (endTime == null ? eventEntry.endTime == null : endTime.equals(eventEntry.endTime)) &&
        (duration == null ? eventEntry.duration == null : duration.equals(eventEntry.duration)) &&
        (panelHosts == null ? eventEntry.panelHosts == null : panelHosts.equals(eventEntry.panelHosts)) &&
        (isDeviatingFromConBook == null ? eventEntry.isDeviatingFromConBook == null : isDeviatingFromConBook.equals(eventEntry.isDeviatingFromConBook));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (sourceEventId == null ? 0: sourceEventId.hashCode());
    result = 31 * result + (imageId == null ? 0: imageId.hashCode());
    result = 31 * result + (slug == null ? 0: slug.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (subTitle == null ? 0: subTitle.hashCode());
    result = 31 * result + (conferenceTrackId == null ? 0: conferenceTrackId.hashCode());
    result = 31 * result + (conferenceDayId == null ? 0: conferenceDayId.hashCode());
    result = 31 * result + (conferenceRoomId == null ? 0: conferenceRoomId.hashCode());
    result = 31 * result + (_abstract == null ? 0: _abstract.hashCode());
    result = 31 * result + (description == null ? 0: description.hashCode());
    result = 31 * result + (startTime == null ? 0: startTime.hashCode());
    result = 31 * result + (endTime == null ? 0: endTime.hashCode());
    result = 31 * result + (duration == null ? 0: duration.hashCode());
    result = 31 * result + (panelHosts == null ? 0: panelHosts.hashCode());
    result = 31 * result + (isDeviatingFromConBook == null ? 0: isDeviatingFromConBook.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventEntry {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  sourceEventId: ").append(sourceEventId).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  slug: ").append(slug).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  subTitle: ").append(subTitle).append("\n");
    sb.append("  conferenceTrackId: ").append(conferenceTrackId).append("\n");
    sb.append("  conferenceDayId: ").append(conferenceDayId).append("\n");
    sb.append("  conferenceRoomId: ").append(conferenceRoomId).append("\n");
    sb.append("  _abstract: ").append(_abstract).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  endTime: ").append(endTime).append("\n");
    sb.append("  duration: ").append(duration).append("\n");
    sb.append("  panelHosts: ").append(panelHosts).append("\n");
    sb.append("  isDeviatingFromConBook: ").append(isDeviatingFromConBook).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
