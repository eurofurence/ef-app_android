package io.swagger.client.model;

import io.swagger.client.model.EntityBase;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class InfoGroup extends EntityBase {
  
  @SerializedName("Order")
  private Integer order = null;
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
    sb.append("  description: ").append(description).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
