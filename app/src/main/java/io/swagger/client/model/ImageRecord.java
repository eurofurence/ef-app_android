package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ImageRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("InternalReference")
  private String internalReference = null;
  @SerializedName("Width")
  private Integer width = null;
  @SerializedName("Height")
  private Integer height = null;
  @SerializedName("SizeInBytes")
  private Long sizeInBytes = null;
  @SerializedName("MimeType")
  private String mimeType = null;
  @SerializedName("ContentHashSha1")
  private String contentHashSha1 = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getInternalReference() {
    return internalReference;
  }
  public void setInternalReference(String internalReference) {
    this.internalReference = internalReference;
  }

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

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getContentHashSha1() {
    return contentHashSha1;
  }
  public void setContentHashSha1(String contentHashSha1) {
    this.contentHashSha1 = contentHashSha1;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageRecord imageRecord = (ImageRecord) o;
    return (lastChangeDateTimeUtc == null ? imageRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(imageRecord.lastChangeDateTimeUtc)) &&
        (id == null ? imageRecord.id == null : id.equals(imageRecord.id)) &&
        (internalReference == null ? imageRecord.internalReference == null : internalReference.equals(imageRecord.internalReference)) &&
        (width == null ? imageRecord.width == null : width.equals(imageRecord.width)) &&
        (height == null ? imageRecord.height == null : height.equals(imageRecord.height)) &&
        (sizeInBytes == null ? imageRecord.sizeInBytes == null : sizeInBytes.equals(imageRecord.sizeInBytes)) &&
        (mimeType == null ? imageRecord.mimeType == null : mimeType.equals(imageRecord.mimeType)) &&
        (contentHashSha1 == null ? imageRecord.contentHashSha1 == null : contentHashSha1.equals(imageRecord.contentHashSha1));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (internalReference == null ? 0: internalReference.hashCode());
    result = 31 * result + (width == null ? 0: width.hashCode());
    result = 31 * result + (height == null ? 0: height.hashCode());
    result = 31 * result + (sizeInBytes == null ? 0: sizeInBytes.hashCode());
    result = 31 * result + (mimeType == null ? 0: mimeType.hashCode());
    result = 31 * result + (contentHashSha1 == null ? 0: contentHashSha1.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImageRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  internalReference: ").append(internalReference).append("\n");
    sb.append("  width: ").append(width).append("\n");
    sb.append("  height: ").append(height).append("\n");
    sb.append("  sizeInBytes: ").append(sizeInBytes).append("\n");
    sb.append("  mimeType: ").append(mimeType).append("\n");
    sb.append("  contentHashSha1: ").append(contentHashSha1).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
