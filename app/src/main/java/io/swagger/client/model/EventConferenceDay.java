package io.swagger.client.model;

import io.swagger.client.model.EntityBase;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventConferenceDay extends EntityBase {
  
  @SerializedName("Date")
  private String date = null;
  @SerializedName("Name")
  private String name = null;

  
  /**
   * Conference day in date format (YYYY-MM-DD)
   **/
  @ApiModelProperty(required = true, value = "Conference day in date format (YYYY-MM-DD)")
  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }

  
  /**
   * Name of the conference day, e,g, \"Sat - Con Day 4\".
   **/
  @ApiModelProperty(required = true, value = "Name of the conference day, e,g, \"Sat - Con Day 4\".")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventConferenceDay {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
