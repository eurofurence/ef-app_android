package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ImageFragment  {
  
  @SerializedName("Width")
  private Integer width = null;
  @SerializedName("Height")
  private Integer height = null;
  @SerializedName("SizeInBytes")
  private Long sizeInBytes = null;
  @SerializedName("MimeType")
  private String mimeType = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getWidth() {
    return width;
  }
  public void setWidth(Integer width) {
    this.width = width;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getHeight() {
    return height;
  }
  public void setHeight(Integer height) {
    this.height = height;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Long getSizeInBytes() {
    return sizeInBytes;
  }
  public void setSizeInBytes(Long sizeInBytes) {
    this.sizeInBytes = sizeInBytes;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageFragment imageFragment = (ImageFragment) o;
    return (width == null ? imageFragment.width == null : width.equals(imageFragment.width)) &&
        (height == null ? imageFragment.height == null : height.equals(imageFragment.height)) &&
        (sizeInBytes == null ? imageFragment.sizeInBytes == null : sizeInBytes.equals(imageFragment.sizeInBytes)) &&
        (mimeType == null ? imageFragment.mimeType == null : mimeType.equals(imageFragment.mimeType));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (width == null ? 0: width.hashCode());
    result = 31 * result + (height == null ? 0: height.hashCode());
    result = 31 * result + (sizeInBytes == null ? 0: sizeInBytes.hashCode());
    result = 31 * result + (mimeType == null ? 0: mimeType.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImageFragment {\n");
    
    sb.append("  width: ").append(width).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  sizeInBytes: ").append(sizeInBytes).append("\n");
    sb.append("  mimeType: ").append(mimeType).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
