package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import io.swagger.client.model.NamedUrl;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Info extends EntityBase {
  
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
  @SerializedName("ImageIds")
  private List<String> imageIds = null;
  @SerializedName("Urls")
  private List<NamedUrl> urls = null;

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

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getImageIds() {
    return imageIds;
  }
  public void setImageIds(List<String> imageIds) {
    this.imageIds = imageIds;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<NamedUrl> getUrls() {
    return urls;
  }
  public void setUrls(List<NamedUrl> urls) {
    this.urls = urls;
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
    return (infoGroupId == null ? info.infoGroupId == null : infoGroupId.equals(info.infoGroupId)) &&
        (imageId == null ? info.imageId == null : imageId.equals(info.imageId)) &&
        (title == null ? info.title == null : title.equals(info.title)) &&
        (text == null ? info.text == null : text.equals(info.text)) &&
        (position == null ? info.position == null : position.equals(info.position)) &&
        (imageIds == null ? info.imageIds == null : imageIds.equals(info.imageIds)) &&
        (urls == null ? info.urls == null : urls.equals(info.urls));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (infoGroupId == null ? 0: infoGroupId.hashCode());
    result = 31 * result + (imageId == null ? 0: imageId.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (text == null ? 0: text.hashCode());
    result = 31 * result + (position == null ? 0: position.hashCode());
    result = 31 * result + (imageIds == null ? 0: imageIds.hashCode());
    result = 31 * result + (urls == null ? 0: urls.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Info {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  infoGroupId: ").append(infoGroupId).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  position: ").append(position).append("\n");
    sb.append("  imageIds: ").append(imageIds).append("\n");
    sb.append("  urls: ").append(urls).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
