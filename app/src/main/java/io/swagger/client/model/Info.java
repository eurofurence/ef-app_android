package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Info extends EntityBase {
  
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Text")
  private String text = null;
  @SerializedName("Position")
  private Integer position = null;
  @SerializedName("InfoGroupId")
  private UUID infoGroupId = null;

  
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
   * Title of the message blob.
   **/
  @ApiModelProperty(value = "Title of the message blob.")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   * Content of the message blob, may contain markup (tbd).
   **/
  @ApiModelProperty(value = "Content of the message blob, may contain markup (tbd).")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
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
   * Id of the InfoGroup this record belongs to.
   **/
  @ApiModelProperty(value = "Id of the InfoGroup this record belongs to.")
  public UUID getInfoGroupId() {
    return infoGroupId;
  }
  public void setInfoGroupId(UUID infoGroupId) {
    this.infoGroupId = infoGroupId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  infoGroupId: ").append(infoGroupId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
