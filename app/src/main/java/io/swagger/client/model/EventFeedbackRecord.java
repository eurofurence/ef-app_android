package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventFeedbackRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("EventId")
  private UUID eventId = null;
  @SerializedName("Rating")
  private Integer rating = null;
  @SerializedName("Message")
  private String message = null;

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
  public UUID getEventId() {
    return eventId;
  }
  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getRating() {
    return rating;
  }
  public void setRating(Integer rating) {
    this.rating = rating;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventFeedbackRecord eventFeedbackRecord = (EventFeedbackRecord) o;
    return (lastChangeDateTimeUtc == null ? eventFeedbackRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(eventFeedbackRecord.lastChangeDateTimeUtc)) &&
        (id == null ? eventFeedbackRecord.id == null : id.equals(eventFeedbackRecord.id)) &&
        (eventId == null ? eventFeedbackRecord.eventId == null : eventId.equals(eventFeedbackRecord.eventId)) &&
        (rating == null ? eventFeedbackRecord.rating == null : rating.equals(eventFeedbackRecord.rating)) &&
        (message == null ? eventFeedbackRecord.message == null : message.equals(eventFeedbackRecord.message));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (eventId == null ? 0: eventId.hashCode());
    result = 31 * result + (rating == null ? 0: rating.hashCode());
    result = 31 * result + (message == null ? 0: message.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventFeedbackRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  eventId: ").append(eventId).append("\n");
    sb.append("  rating: ").append(rating).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
