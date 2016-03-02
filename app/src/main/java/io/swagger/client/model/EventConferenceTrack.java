package io.swagger.client.model;

import io.swagger.client.model.EntityBase;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventConferenceTrack extends EntityBase {
  
  @SerializedName("Name")
  private String name = null;

  
  /**
   * Name of the conference track.
   **/
  @ApiModelProperty(required = true, value = "Name of the conference track.")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventConferenceTrack {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
