package io.swagger.client.model;

import io.swagger.client.model.ImageFragment;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class TableRegistrationRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("CreatedDateTimeUtc")
  private Date createdDateTimeUtc = null;
  @SerializedName("OwnerUid")
  private String ownerUid = null;
  @SerializedName("DisplayName")
  private String displayName = null;
  @SerializedName("WebsiteUrl")
  private String websiteUrl = null;
  @SerializedName("ShortDescription")
  private String shortDescription = null;
  @SerializedName("TelegramHandle")
  private String telegramHandle = null;
  @SerializedName("Location")
  private String location = null;
  @SerializedName("Image")
  private ImageFragment image = null;
  public enum StateEnum {
     Pending,  Accepted,  Published,  Rejected, 
  };
  @SerializedName("State")
  private StateEnum state = null;
  @SerializedName("ImageContent")
  private String imageContent = null;

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
  @ApiModelProperty(value = "")
  public Date getCreatedDateTimeUtc() {
    return createdDateTimeUtc;
  }
  public void setCreatedDateTimeUtc(Date createdDateTimeUtc) {
    this.createdDateTimeUtc = createdDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getOwnerUid() {
    return ownerUid;
  }
  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getWebsiteUrl() {
    return websiteUrl;
  }
  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getShortDescription() {
    return shortDescription;
  }
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTelegramHandle() {
    return telegramHandle;
  }
  public void setTelegramHandle(String telegramHandle) {
    this.telegramHandle = telegramHandle;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public ImageFragment getImage() {
    return image;
  }
  public void setImage(ImageFragment image) {
    this.image = image;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public StateEnum getState() {
    return state;
  }
  public void setState(StateEnum state) {
    this.state = state;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getImageContent() {
    return imageContent;
  }
  public void setImageContent(String imageContent) {
    this.imageContent = imageContent;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TableRegistrationRecord tableRegistrationRecord = (TableRegistrationRecord) o;
    return (lastChangeDateTimeUtc == null ? tableRegistrationRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(tableRegistrationRecord.lastChangeDateTimeUtc)) &&
        (id == null ? tableRegistrationRecord.id == null : id.equals(tableRegistrationRecord.id)) &&
        (createdDateTimeUtc == null ? tableRegistrationRecord.createdDateTimeUtc == null : createdDateTimeUtc.equals(tableRegistrationRecord.createdDateTimeUtc)) &&
        (ownerUid == null ? tableRegistrationRecord.ownerUid == null : ownerUid.equals(tableRegistrationRecord.ownerUid)) &&
        (displayName == null ? tableRegistrationRecord.displayName == null : displayName.equals(tableRegistrationRecord.displayName)) &&
        (websiteUrl == null ? tableRegistrationRecord.websiteUrl == null : websiteUrl.equals(tableRegistrationRecord.websiteUrl)) &&
        (shortDescription == null ? tableRegistrationRecord.shortDescription == null : shortDescription.equals(tableRegistrationRecord.shortDescription)) &&
        (telegramHandle == null ? tableRegistrationRecord.telegramHandle == null : telegramHandle.equals(tableRegistrationRecord.telegramHandle)) &&
        (location == null ? tableRegistrationRecord.location == null : location.equals(tableRegistrationRecord.location)) &&
        (image == null ? tableRegistrationRecord.image == null : image.equals(tableRegistrationRecord.image)) &&
        (state == null ? tableRegistrationRecord.state == null : state.equals(tableRegistrationRecord.state)) &&
        (imageContent == null ? tableRegistrationRecord.imageContent == null : imageContent.equals(tableRegistrationRecord.imageContent));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (createdDateTimeUtc == null ? 0: createdDateTimeUtc.hashCode());
    result = 31 * result + (ownerUid == null ? 0: ownerUid.hashCode());
    result = 31 * result + (displayName == null ? 0: displayName.hashCode());
    result = 31 * result + (websiteUrl == null ? 0: websiteUrl.hashCode());
    result = 31 * result + (shortDescription == null ? 0: shortDescription.hashCode());
    result = 31 * result + (telegramHandle == null ? 0: telegramHandle.hashCode());
    result = 31 * result + (location == null ? 0: location.hashCode());
    result = 31 * result + (image == null ? 0: image.hashCode());
    result = 31 * result + (state == null ? 0: state.hashCode());
    result = 31 * result + (imageContent == null ? 0: imageContent.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TableRegistrationRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  createdDateTimeUtc: ").append(createdDateTimeUtc).append("\n");
    sb.append("  ownerUid: ").append(ownerUid).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  websiteUrl: ").append(websiteUrl).append("\n");
    sb.append("  shortDescription: ").append(shortDescription).append("\n");
    sb.append("  telegramHandle: ").append(telegramHandle).append("\n");
    sb.append("  location: ").append(location).append("\n");
    sb.append("  image: ").append(image).append("\n");
    sb.append("  state: ").append(state).append("\n");
    sb.append("  imageContent: ").append(imageContent).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
