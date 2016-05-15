package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Announcement extends EntityBase {
  
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Announcement announcement = (Announcement) o;
    return (validFromDateTimeUtc == null ? announcement.validFromDateTimeUtc == null : validFromDateTimeUtc.equals(announcement.validFromDateTimeUtc)) &&
        (validUntilDateTimeUtc == null ? announcement.validUntilDateTimeUtc == null : validUntilDateTimeUtc.equals(announcement.validUntilDateTimeUtc)) &&
        (area == null ? announcement.area == null : area.equals(announcement.area)) &&
        (author == null ? announcement.author == null : author.equals(announcement.author)) &&
        (title == null ? announcement.title == null : title.equals(announcement.title)) &&
        (content == null ? announcement.content == null : content.equals(announcement.content));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (validFromDateTimeUtc == null ? 0: validFromDateTimeUtc.hashCode());
    result = 31 * result + (validUntilDateTimeUtc == null ? 0: validUntilDateTimeUtc.hashCode());
    result = 31 * result + (area == null ? 0: area.hashCode());
    result = 31 * result + (author == null ? 0: author.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (content == null ? 0: content.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Announcement {\n");
    sb.append("  " + super.toString()).append("\n");
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
