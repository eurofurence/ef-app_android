package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Announcement extends EntityBase {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
  @SerializedName("ValidFromDateTimeUtc")
  private Date validFromDateTimeUtc = null;
  @SerializedName("ValidUntilDateTimeUtc")
  private Date validUntilDateTimeUtc = null;
  @SerializedName("Area")
  private String area = null;
  @SerializedName("Author")
  private String author = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Content")
  private String content = null;

  
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
   **/
  @ApiModelProperty(required = true, value = "")
  public Date getValidFromDateTimeUtc() {
    return validFromDateTimeUtc;
  }
  public void setValidFromDateTimeUtc(Date validFromDateTimeUtc) {
    this.validFromDateTimeUtc = validFromDateTimeUtc;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Date getValidUntilDateTimeUtc() {
    return validUntilDateTimeUtc;
  }
  public void setValidUntilDateTimeUtc(Date validUntilDateTimeUtc) {
    this.validUntilDateTimeUtc = validUntilDateTimeUtc;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getArea() {
    return area;
  }
  public void setArea(String area) {
    this.area = area;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getAuthor() {
    return author;
  }
  public void setAuthor(String author) {
    this.author = author;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Announcement {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
    sb.append("  validFromDateTimeUtc: ").append(validFromDateTimeUtc).append("\n");
    sb.append("  validUntilDateTimeUtc: ").append(validUntilDateTimeUtc).append("\n");
    sb.append("  area: ").append(area).append("\n");
    sb.append("  author: ").append(author).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  content: ").append(content).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
