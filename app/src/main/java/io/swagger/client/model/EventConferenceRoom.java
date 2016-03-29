package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;



@ApiModel(description = "")
public class EventConferenceRoom extends EntityBase {
  
  @SerializedName("Name")
  private String name = null;
  @SerializedName("ImageEntryId")
  private String imageEntryId = null;
  @SerializedName("ImageRoomPositionPoint")
  private String imageRoomPositionPoint = null;

  
  /**
   * Name of the conference room.
   **/
  @ApiModelProperty(required = true, value = "Name of the conference room.")
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
  public String getImageEntryId() {
    return imageEntryId;
  }
  public void setImageEntryId(String imageEntryId) {
    this.imageEntryId = imageEntryId;
  }

  
  /**
   * Percentual coordinates on the original image indicating the entrance of the room.
   **/
  @ApiModelProperty(value = "Percentual coordinates on the original image indicating the entrance of the room.")
  public String getImageRoomPositionPoint() {
    return imageRoomPositionPoint;
  }
  public void setImageRoomPositionPoint(String imageRoomPositionPoint) {
    this.imageRoomPositionPoint = imageRoomPositionPoint;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventConferenceRoom {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  imageEntryId: ").append(imageEntryId).append("\n");
    sb.append("  imageRoomPositionPoint: ").append(imageRoomPositionPoint).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}


