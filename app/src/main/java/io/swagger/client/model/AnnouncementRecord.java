package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class AnnouncementRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
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
  @SerializedName("ImageId")
  private UUID imageId = null;

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

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getImageId() {
    return imageId;
  }
  public void setImageId(UUID imageId) {
    this.imageId = imageId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnnouncementRecord announcementRecord = (AnnouncementRecord) o;
    return (lastChangeDateTimeUtc == null ? announcementRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(announcementRecord.lastChangeDateTimeUtc)) &&
        (id == null ? announcementRecord.id == null : id.equals(announcementRecord.id)) &&
        (validFromDateTimeUtc == null ? announcementRecord.validFromDateTimeUtc == null : validFromDateTimeUtc.equals(announcementRecord.validFromDateTimeUtc)) &&
        (validUntilDateTimeUtc == null ? announcementRecord.validUntilDateTimeUtc == null : validUntilDateTimeUtc.equals(announcementRecord.validUntilDateTimeUtc)) &&
        (area == null ? announcementRecord.area == null : area.equals(announcementRecord.area)) &&
        (author == null ? announcementRecord.author == null : author.equals(announcementRecord.author)) &&
        (title == null ? announcementRecord.title == null : title.equals(announcementRecord.title)) &&
        (content == null ? announcementRecord.content == null : content.equals(announcementRecord.content)) &&
        (imageId == null ? announcementRecord.imageId == null : imageId.equals(announcementRecord.imageId));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (validFromDateTimeUtc == null ? 0: validFromDateTimeUtc.hashCode());
    result = 31 * result + (validUntilDateTimeUtc == null ? 0: validUntilDateTimeUtc.hashCode());
    result = 31 * result + (area == null ? 0: area.hashCode());
    result = 31 * result + (author == null ? 0: author.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (content == null ? 0: content.hashCode());
    result = 31 * result + (imageId == null ? 0: imageId.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnnouncementRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  validFromDateTimeUtc: ").append(validFromDateTimeUtc).append("\n");
    sb.append("  validUntilDateTimeUtc: ").append(validUntilDateTimeUtc).append("\n");
    sb.append("  area: ").append(area).append("\n");
    sb.append("  author: ").append(author).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  content: ").append(content).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
