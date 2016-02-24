package io.swagger.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;

import java.io.Serializable;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-02-24T14:49:45.074+01:00")
public class Info extends EntityBase implements Serializable {
  
  private String text = null;
  private String imageEntryId = null;
  private Date lastChangeDateTimeUtc = null;
  private Integer order = null;
  private BigDecimal isDeleted = null;
  private String id = null;
  private String title = null;
  private String infoGroupEntryId = null;

  
  /**
   * Content of the message blob, may contain markup (tbd).
   **/
  
  @ApiModelProperty(value = "Content of the message blob, may contain markup (tbd).")
  @JsonProperty("Text")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("ImageEntryId")
  public String getImageEntryId() {
    return imageEntryId;
  }
  public void setImageEntryId(String imageEntryId) {
    this.imageEntryId = imageEntryId;
  }

  
  /**
   * Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)
   **/
  
  @ApiModelProperty(required = true, value = "Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)")
  @JsonProperty("LastChangeDateTimeUtc")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  
  /**
   * Numeric order/position of the element (lower number = display first)
   **/
  
  @ApiModelProperty(value = "Numeric order/position of the element (lower number = display first)")
  @JsonProperty("Order")
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
  @JsonProperty("IsDeleted")
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
  @JsonProperty("Id")
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  
  /**
   * Title of the message blob.
   **/
  
  @ApiModelProperty(value = "Title of the message blob.")
  @JsonProperty("Title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   * Id of the InfoGroup this record belongs to.
   **/
  
  @ApiModelProperty(value = "Id of the InfoGroup this record belongs to.")
  @JsonProperty("InfoGroupEntryId")
  public String getInfoGroupEntryId() {
    return infoGroupEntryId;
  }
  public void setInfoGroupEntryId(String infoGroupEntryId) {
    this.infoGroupEntryId = infoGroupEntryId;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Info info = (Info) o;
    return Objects.equals(text, info.text) &&
        Objects.equals(imageEntryId, info.imageEntryId) &&
        Objects.equals(lastChangeDateTimeUtc, info.lastChangeDateTimeUtc) &&
        Objects.equals(order, info.order) &&
        Objects.equals(isDeleted, info.isDeleted) &&
        Objects.equals(id, info.id) &&
        Objects.equals(title, info.title) &&
        Objects.equals(infoGroupEntryId, info.infoGroupEntryId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text, imageEntryId, lastChangeDateTimeUtc, order, isDeleted, id, title, infoGroupEntryId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    imageEntryId: ").append(toIndentedString(imageEntryId)).append("\n");
    sb.append("    lastChangeDateTimeUtc: ").append(toIndentedString(lastChangeDateTimeUtc)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
    sb.append("    isDeleted: ").append(toIndentedString(isDeleted)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    infoGroupEntryId: ").append(toIndentedString(infoGroupEntryId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

