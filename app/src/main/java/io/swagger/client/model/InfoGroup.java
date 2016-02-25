package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class InfoGroup extends EntityBase {
  
  @SerializedName("Order")
  private Integer order = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
  @SerializedName("Id")
  private String id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("Name")
  private String name = null;

  
  /**
   * Numeric order/position of the element (lower number = display first)
   **/
  @ApiModelProperty(value = "Numeric order/position of the element (lower number = display first)")
  public Integer getOrder() {
    return order;
  }
  public void setOrder(Integer order) {
    this.order = order;
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
   * Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000
   **/
  @ApiModelProperty(required = true, value = "Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000")
  public String getId() {
    return id;
  }
  public void setId(String id) {
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
   * Shhort description of the information group
   **/
  @ApiModelProperty(value = "Shhort description of the information group")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  
  /**
   * Name of the information group
   **/
  @ApiModelProperty(value = "Name of the information group")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfoGroup {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  order: ").append(order).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
