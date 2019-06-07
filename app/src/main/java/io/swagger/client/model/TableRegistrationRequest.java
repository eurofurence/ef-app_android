package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class TableRegistrationRequest  {
  
  @SerializedName("DisplayName")
  private String displayName = null;
  @SerializedName("WebsiteUrl")
  private String websiteUrl = null;
  @SerializedName("ShortDescription")
  private String shortDescription = null;
  @SerializedName("ImageContent")
  private String imageContent = null;
  @SerializedName("Location")
  private String location = null;
  @SerializedName("TelegramHandle")
  private String telegramHandle = null;

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
  public String getImageContent() {
    return imageContent;
  }
  public void setImageContent(String imageContent) {
    this.imageContent = imageContent;
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
  public String getTelegramHandle() {
    return telegramHandle;
  }
  public void setTelegramHandle(String telegramHandle) {
    this.telegramHandle = telegramHandle;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TableRegistrationRequest tableRegistrationRequest = (TableRegistrationRequest) o;
    return (displayName == null ? tableRegistrationRequest.displayName == null : displayName.equals(tableRegistrationRequest.displayName)) &&
        (websiteUrl == null ? tableRegistrationRequest.websiteUrl == null : websiteUrl.equals(tableRegistrationRequest.websiteUrl)) &&
        (shortDescription == null ? tableRegistrationRequest.shortDescription == null : shortDescription.equals(tableRegistrationRequest.shortDescription)) &&
        (imageContent == null ? tableRegistrationRequest.imageContent == null : imageContent.equals(tableRegistrationRequest.imageContent)) &&
        (location == null ? tableRegistrationRequest.location == null : location.equals(tableRegistrationRequest.location)) &&
        (telegramHandle == null ? tableRegistrationRequest.telegramHandle == null : telegramHandle.equals(tableRegistrationRequest.telegramHandle));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (displayName == null ? 0: displayName.hashCode());
    result = 31 * result + (websiteUrl == null ? 0: websiteUrl.hashCode());
    result = 31 * result + (shortDescription == null ? 0: shortDescription.hashCode());
    result = 31 * result + (imageContent == null ? 0: imageContent.hashCode());
    result = 31 * result + (location == null ? 0: location.hashCode());
    result = 31 * result + (telegramHandle == null ? 0: telegramHandle.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class TableRegistrationRequest {\n");
    
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  websiteUrl: ").append(websiteUrl).append("\n");
    sb.append("  shortDescription: ").append(shortDescription).append("\n");
    sb.append("  imageContent: ").append(imageContent).append("\n");
    sb.append("  location: ").append(location).append("\n");
    sb.append("  telegramHandle: ").append(telegramHandle).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
