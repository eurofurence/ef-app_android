package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventConferenceDay extends EntityBase {
  
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Date")
  private String date = null;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventConferenceDay eventConferenceDay = (EventConferenceDay) o;
    return (name == null ? eventConferenceDay.name == null : name.equals(eventConferenceDay.name)) &&
        (date == null ? eventConferenceDay.date == null : date.equals(eventConferenceDay.date));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (date == null ? 0: date.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventConferenceDay {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
