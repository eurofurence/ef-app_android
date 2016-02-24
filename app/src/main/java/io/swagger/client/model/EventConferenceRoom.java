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
public class EventConferenceRoom extends EntityBase implements Serializable {
  
  private String name = null;
  private String imageEntryId = null;
  private Date lastChangeDateTimeUtc = null;
  private String imageRoomPositionPoint = null;
  private BigDecimal isDeleted = null;
  private String id = null;

  
  /**
   * Name of the conference room.
   **/
  
  @ApiModelProperty(required = true, value = "Name of the conference room.")
  @JsonProperty("Name")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  
  /**
   * Id of the (map) image that shows the room
   **/
  
  @ApiModelProperty(value = "Id of the (map) image that shows the room")
  @JsonProperty("ImageEntryId")
  public String getImageEntryId() {
    return imageEntryId;
  }
  public void setImageEntryId(String imageEntryId) {
    this.imageEntryId = imageEntryId;
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
   * Percentual coordinates on the original image indicating the entrance of the room.
   **/
  
  @ApiModelProperty(value = "Percentual coordinates on the original image indicating the entrance of the room.")
  @JsonProperty("ImageRoomPositionPoint")
  public String getImageRoomPositionPoint() {
    return imageRoomPositionPoint;
  }
  public void setImageRoomPositionPoint(String imageRoomPositionPoint) {
    this.imageRoomPositionPoint = imageRoomPositionPoint;
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

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventConferenceRoom eventConferenceRoom = (EventConferenceRoom) o;
    return Objects.equals(name, eventConferenceRoom.name) &&
        Objects.equals(imageEntryId, eventConferenceRoom.imageEntryId) &&
        Objects.equals(lastChangeDateTimeUtc, eventConferenceRoom.lastChangeDateTimeUtc) &&
        Objects.equals(imageRoomPositionPoint, eventConferenceRoom.imageRoomPositionPoint) &&
        Objects.equals(isDeleted, eventConferenceRoom.isDeleted) &&
        Objects.equals(id, eventConferenceRoom.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, imageEntryId, lastChangeDateTimeUtc, imageRoomPositionPoint, isDeleted, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventConferenceRoom {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    imageEntryId: ").append(toIndentedString(imageEntryId)).append("\n");
    sb.append("    lastChangeDateTimeUtc: ").append(toIndentedString(lastChangeDateTimeUtc)).append("\n");
    sb.append("    imageRoomPositionPoint: ").append(toIndentedString(imageRoomPositionPoint)).append("\n");
    sb.append("    isDeleted: ").append(toIndentedString(isDeleted)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

