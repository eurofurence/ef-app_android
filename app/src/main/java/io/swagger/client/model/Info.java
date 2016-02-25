package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Info extends EntityBase {
  
  @SerializedName("Order")
  private Integer order = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
  @SerializedName("InfoGroupEntryId")
  private String infoGroupEntryId = null;
  @SerializedName("ImageEntryId")
  private String imageEntryId = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Text")
  private String text = null;
  @SerializedName("Id")
  private String id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;

  
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
   * Id of the InfoGroup this record belongs to.
   **/
  @ApiModelProperty(value = "Id of the InfoGroup this record belongs to.")
  public String getInfoGroupEntryId() {
    return infoGroupEntryId;
  }
  public void setInfoGroupEntryId(String infoGroupEntryId) {
    this.infoGroupEntryId = infoGroupEntryId;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getImageEntryId() {
    return imageEntryId;
  }
  public void setImageEntryId(String imageEntryId) {
    this.imageEntryId = imageEntryId;
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

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  order: ").append(order).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
    sb.append("  infoGroupEntryId: ").append(infoGroupEntryId).append("\n");
    sb.append("  imageEntryId: ").append(imageEntryId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
