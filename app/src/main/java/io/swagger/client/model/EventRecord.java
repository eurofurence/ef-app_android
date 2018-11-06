package io.swagger.client.model;

import java.util.*;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("Slug")
  private String slug = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("SubTitle")
  private String subTitle = null;
  @SerializedName("Abstract")
  private String _abstract = null;
  @SerializedName("ConferenceDayId")
  private UUID conferenceDayId = null;
  @SerializedName("ConferenceTrackId")
  private UUID conferenceTrackId = null;
  @SerializedName("ConferenceRoomId")
  private UUID conferenceRoomId = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("Duration")
  private String duration = null;
  @SerializedName("StartTime")
  private String startTime = null;
  @SerializedName("EndTime")
  private String endTime = null;
  @SerializedName("StartDateTimeUtc")
  private Date startDateTimeUtc = null;
  @SerializedName("EndDateTimeUtc")
  private Date endDateTimeUtc = null;
  @SerializedName("PanelHosts")
  private String panelHosts = null;
  @SerializedName("IsDeviatingFromConBook")
  private Boolean isDeviatingFromConBook = null;
  @SerializedName("BannerImageId")
  private UUID bannerImageId = null;
  @SerializedName("PosterImageId")
  private UUID posterImageId = null;
  @SerializedName("Tags")
  private List<String> tags = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getSlug() {
    return slug;
  }
  public void setSlug(String slug) {
    this.slug = slug;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getSubTitle() {
    return subTitle;
  }
  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getAbstract() {
    return _abstract;
  }
  public void setAbstract(String _abstract) {
    this._abstract = _abstract;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getConferenceDayId() {
    return conferenceDayId;
  }
  public void setConferenceDayId(UUID conferenceDayId) {
    this.conferenceDayId = conferenceDayId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getConferenceTrackId() {
    return conferenceTrackId;
  }
  public void setConferenceTrackId(UUID conferenceTrackId) {
    this.conferenceTrackId = conferenceTrackId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getConferenceRoomId() {
    return conferenceRoomId;
  }
  public void setConferenceRoomId(UUID conferenceRoomId) {
    this.conferenceRoomId = conferenceRoomId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDuration() {
    return duration;
  }
  public void setDuration(String duration) {
    this.duration = duration;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getStartDateTimeUtc() {
    return startDateTimeUtc;
  }
  public void setStartDateTimeUtc(Date startDateTimeUtc) {
    this.startDateTimeUtc = startDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getEndDateTimeUtc() {
    return endDateTimeUtc;
  }
  public void setEndDateTimeUtc(Date endDateTimeUtc) {
    this.endDateTimeUtc = endDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getPanelHosts() {
    return panelHosts;
  }
  public void setPanelHosts(String panelHosts) {
    this.panelHosts = panelHosts;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getIsDeviatingFromConBook() {
    return isDeviatingFromConBook;
  }
  public void setIsDeviatingFromConBook(Boolean isDeviatingFromConBook) {
    this.isDeviatingFromConBook = isDeviatingFromConBook;
  }

  /**
   * If set, refers to a banner ([3-4]:1 aspect ratio) that can be used when little  vertical space is available (e.G. event schedule, or a header section).
   **/
  @ApiModelProperty(value = "If set, refers to a banner ([3-4]:1 aspect ratio) that can be used when little  vertical space is available (e.G. event schedule, or a header section).")
  public UUID getBannerImageId() {
    return bannerImageId;
  }
  public void setBannerImageId(UUID bannerImageId) {
    this.bannerImageId = bannerImageId;
  }

  /**
   * If set, refers to an image of any aspect ratio that should be used where enough  vertical space is available (e.G. event detail).
   **/
  @ApiModelProperty(value = "If set, refers to an image of any aspect ratio that should be used where enough  vertical space is available (e.G. event detail).")
  public UUID getPosterImageId() {
    return posterImageId;
  }
  public void setPosterImageId(UUID posterImageId) {
    this.posterImageId = posterImageId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getTags() {
    return tags;
  }
  public void setTags(List<String> tags) {
    this.tags = tags;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventRecord eventRecord = (EventRecord) o;
    return (lastChangeDateTimeUtc == null ? eventRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(eventRecord.lastChangeDateTimeUtc)) &&
        (id == null ? eventRecord.id == null : id.equals(eventRecord.id)) &&
        (slug == null ? eventRecord.slug == null : slug.equals(eventRecord.slug)) &&
        (title == null ? eventRecord.title == null : title.equals(eventRecord.title)) &&
        (subTitle == null ? eventRecord.subTitle == null : subTitle.equals(eventRecord.subTitle)) &&
        (_abstract == null ? eventRecord._abstract == null : _abstract.equals(eventRecord._abstract)) &&
        (conferenceDayId == null ? eventRecord.conferenceDayId == null : conferenceDayId.equals(eventRecord.conferenceDayId)) &&
        (conferenceTrackId == null ? eventRecord.conferenceTrackId == null : conferenceTrackId.equals(eventRecord.conferenceTrackId)) &&
        (conferenceRoomId == null ? eventRecord.conferenceRoomId == null : conferenceRoomId.equals(eventRecord.conferenceRoomId)) &&
        (description == null ? eventRecord.description == null : description.equals(eventRecord.description)) &&
        (duration == null ? eventRecord.duration == null : duration.equals(eventRecord.duration)) &&
        (startTime == null ? eventRecord.startTime == null : startTime.equals(eventRecord.startTime)) &&
        (endTime == null ? eventRecord.endTime == null : endTime.equals(eventRecord.endTime)) &&
        (startDateTimeUtc == null ? eventRecord.startDateTimeUtc == null : startDateTimeUtc.equals(eventRecord.startDateTimeUtc)) &&
        (endDateTimeUtc == null ? eventRecord.endDateTimeUtc == null : endDateTimeUtc.equals(eventRecord.endDateTimeUtc)) &&
        (panelHosts == null ? eventRecord.panelHosts == null : panelHosts.equals(eventRecord.panelHosts)) &&
        (isDeviatingFromConBook == null ? eventRecord.isDeviatingFromConBook == null : isDeviatingFromConBook.equals(eventRecord.isDeviatingFromConBook)) &&
        (bannerImageId == null ? eventRecord.bannerImageId == null : bannerImageId.equals(eventRecord.bannerImageId)) &&
        (posterImageId == null ? eventRecord.posterImageId == null : posterImageId.equals(eventRecord.posterImageId)) &&
        (tags == null ? eventRecord.tags == null : tags.equals(eventRecord.tags));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (slug == null ? 0: slug.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (subTitle == null ? 0: subTitle.hashCode());
    result = 31 * result + (_abstract == null ? 0: _abstract.hashCode());
    result = 31 * result + (conferenceDayId == null ? 0: conferenceDayId.hashCode());
    result = 31 * result + (conferenceTrackId == null ? 0: conferenceTrackId.hashCode());
    result = 31 * result + (conferenceRoomId == null ? 0: conferenceRoomId.hashCode());
    result = 31 * result + (description == null ? 0: description.hashCode());
    result = 31 * result + (duration == null ? 0: duration.hashCode());
    result = 31 * result + (startTime == null ? 0: startTime.hashCode());
    result = 31 * result + (endTime == null ? 0: endTime.hashCode());
    result = 31 * result + (startDateTimeUtc == null ? 0: startDateTimeUtc.hashCode());
    result = 31 * result + (endDateTimeUtc == null ? 0: endDateTimeUtc.hashCode());
    result = 31 * result + (panelHosts == null ? 0: panelHosts.hashCode());
    result = 31 * result + (isDeviatingFromConBook == null ? 0: isDeviatingFromConBook.hashCode());
    result = 31 * result + (bannerImageId == null ? 0: bannerImageId.hashCode());
    result = 31 * result + (posterImageId == null ? 0: posterImageId.hashCode());
    result = 31 * result + (tags == null ? 0: tags.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  slug: ").append(slug).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  subTitle: ").append(subTitle).append("\n");
    sb.append("  _abstract: ").append(_abstract).append("\n");
    sb.append("  conferenceDayId: ").append(conferenceDayId).append("\n");
    sb.append("  conferenceTrackId: ").append(conferenceTrackId).append("\n");
    sb.append("  conferenceRoomId: ").append(conferenceRoomId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  duration: ").append(duration).append("\n");
    sb.append("  startTime: ").append(startTime).append("\n");
    sb.append("  endTime: ").append(endTime).append("\n");
    sb.append("  startDateTimeUtc: ").append(startDateTimeUtc).append("\n");
    sb.append("  endDateTimeUtc: ").append(endDateTimeUtc).append("\n");
    sb.append("  panelHosts: ").append(panelHosts).append("\n");
    sb.append("  isDeviatingFromConBook: ").append(isDeviatingFromConBook).append("\n");
    sb.append("  bannerImageId: ").append(bannerImageId).append("\n");
    sb.append("  posterImageId: ").append(posterImageId).append("\n");
    sb.append("  tags: ").append(tags).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
