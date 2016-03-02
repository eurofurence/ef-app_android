package io.swagger.client.model;

import io.swagger.client.model.EntityBase;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Image extends EntityBase {
  
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Height")
  private Integer height = null;
  @SerializedName("FileSizeInBytes")
  private Integer fileSizeInBytes = null;
  @SerializedName("Width")
  private Integer width = null;
  @SerializedName("Url")
  private String url = null;
  @SerializedName("MimeType")
  private String mimeType = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getHeight() {
    return height;
  }
  public void setHeight(Integer height) {
    this.height = height;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getFileSizeInBytes() {
    return fileSizeInBytes;
  }
  public void setFileSizeInBytes(Integer fileSizeInBytes) {
    this.fileSizeInBytes = fileSizeInBytes;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getWidth() {
    return width;
  }
  public void setWidth(Integer width) {
    this.width = width;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Image {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  fileSizeInBytes: ").append(fileSizeInBytes).append("\n");
    sb.append("  width: ").append(width).append("\n");
    sb.append("  url: ").append(url).append("\n");
    sb.append("  mimeType: ").append(mimeType).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
