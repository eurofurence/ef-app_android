package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Info extends EntityBase {
  
  @SerializedName("Order")
  private Integer order = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Text")
  private String text = null;
  @SerializedName("InfoGroupEntryId")
  private UUID infoGroupEntryId = null;
  @SerializedName("ImageEntryId")
  private UUID imageEntryId = null;

  
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
   * Id of the InfoGroup this record belongs to.
   **/
  @ApiModelProperty(value = "Id of the InfoGroup this record belongs to.")
  public UUID getInfoGroupEntryId() {
    return infoGroupEntryId;
  }
  public void setInfoGroupEntryId(UUID infoGroupEntryId) {
    this.infoGroupEntryId = infoGroupEntryId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getImageEntryId() {
    return imageEntryId;
  }
  public void setImageEntryId(UUID imageEntryId) {
    this.imageEntryId = imageEntryId;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  order: ").append(order).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  infoGroupEntryId: ").append(infoGroupEntryId).append("\n");
    sb.append("  imageEntryId: ").append(imageEntryId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
