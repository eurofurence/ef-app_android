package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Info extends EntityBase {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
  @SerializedName("InfoGroupId")
  private UUID infoGroupId = null;
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Text")
  private String text = null;
  @SerializedName("Position")
  private Integer position = null;

  
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
   * Id of the InfoGroup this record belongs to.
   **/
  @ApiModelProperty(value = "Id of the InfoGroup this record belongs to.")
  public UUID getInfoGroupId() {
    return infoGroupId;
  }
  public void setInfoGroupId(UUID infoGroupId) {
    this.infoGroupId = infoGroupId;
  }

  
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

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
    sb.append("  infoGroupId: ").append(infoGroupId).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
