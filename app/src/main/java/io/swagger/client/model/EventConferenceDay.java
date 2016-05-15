package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EventConferenceDay extends EntityBase {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Date")
  private String date = null;

  
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
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventConferenceDay {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  date: ").append(date).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
