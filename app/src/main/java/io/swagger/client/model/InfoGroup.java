package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class InfoGroup extends EntityBase {
  
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("Position")
  private Integer position = null;
  @SerializedName("Name")
  private String name = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getImageId() {
    return imageId;
  }
  public void setImageId(UUID imageId) {
    this.imageId = imageId;
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
   * Numeric order/position of the element (lower number = display first)
   **/
  @ApiModelProperty(value = "Numeric order/position of the element (lower number = display first)")
  public Integer getPosition() {
    return position;
  }
  public void setPosition(Integer position) {
    this.position = position;
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
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
