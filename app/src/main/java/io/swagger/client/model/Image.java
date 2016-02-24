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
public class Image extends EntityBase implements Serializable {
  
  private Integer fileSizeInBytes = null;
  private String url = null;
  private String mimeType = null;
  private Date lastChangeDateTimeUtc = null;
  private Integer height = null;
  private BigDecimal isDeleted = null;
  private String id = null;
  private Integer width = null;
  private String title = null;

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("FileSizeInBytes")
  public Integer getFileSizeInBytes() {
    return fileSizeInBytes;
  }
  public void setFileSizeInBytes(Integer fileSizeInBytes) {
    this.fileSizeInBytes = fileSizeInBytes;
  }

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("Url")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("MimeType")
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
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
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("Height")
  public Integer getHeight() {
    return height;
  }
  public void setHeight(Integer height) {
    this.height = height;
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
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("Width")
  public Integer getWidth() {
    return width;
  }
  public void setWidth(Integer width) {
    this.width = width;
  }

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("Title")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Image image = (Image) o;
    return Objects.equals(fileSizeInBytes, image.fileSizeInBytes) &&
        Objects.equals(url, image.url) &&
        Objects.equals(mimeType, image.mimeType) &&
        Objects.equals(lastChangeDateTimeUtc, image.lastChangeDateTimeUtc) &&
        Objects.equals(height, image.height) &&
        Objects.equals(isDeleted, image.isDeleted) &&
        Objects.equals(id, image.id) &&
        Objects.equals(width, image.width) &&
        Objects.equals(title, image.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileSizeInBytes, url, mimeType, lastChangeDateTimeUtc, height, isDeleted, id, width, title);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Image {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    fileSizeInBytes: ").append(toIndentedString(fileSizeInBytes)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    mimeType: ").append(toIndentedString(mimeType)).append("\n");
    sb.append("    lastChangeDateTimeUtc: ").append(toIndentedString(lastChangeDateTimeUtc)).append("\n");
    sb.append("    height: ").append(toIndentedString(height)).append("\n");
    sb.append("    isDeleted: ").append(toIndentedString(isDeleted)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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

