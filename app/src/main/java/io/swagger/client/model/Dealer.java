package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Dealer extends EntityBase {
  
  @SerializedName("RegistrationNumber")
  private Integer registrationNumber = null;
  @SerializedName("AttendeeNickname")
  private String attendeeNickname = null;
  @SerializedName("DisplayName")
  private String displayName = null;
  @SerializedName("ShortDescription")
  private String shortDescription = null;
  @SerializedName("AboutTheArtistText")
  private String aboutTheArtistText = null;
  @SerializedName("AboutTheArtText")
  private String aboutTheArtText = null;
  @SerializedName("WebsiteUri")
  private String websiteUri = null;
  @SerializedName("ArtPreviewCaption")
  private String artPreviewCaption = null;
  @SerializedName("ArtistThumbnailImageId")
  private UUID artistThumbnailImageId = null;
  @SerializedName("ArtistImageId")
  private UUID artistImageId = null;
  @SerializedName("ArtPreviewImageId")
  private UUID artPreviewImageId = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getRegistrationNumber() {
    return registrationNumber;
  }
  public void setRegistrationNumber(Integer registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getAttendeeNickname() {
    return attendeeNickname;
  }
  public void setAttendeeNickname(String attendeeNickname) {
    this.attendeeNickname = attendeeNickname;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getShortDescription() {
    return shortDescription;
  }
  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getAboutTheArtistText() {
    return aboutTheArtistText;
  }
  public void setAboutTheArtistText(String aboutTheArtistText) {
    this.aboutTheArtistText = aboutTheArtistText;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getAboutTheArtText() {
    return aboutTheArtText;
  }
  public void setAboutTheArtText(String aboutTheArtText) {
    this.aboutTheArtText = aboutTheArtText;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getWebsiteUri() {
    return websiteUri;
  }
  public void setWebsiteUri(String websiteUri) {
    this.websiteUri = websiteUri;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getArtPreviewCaption() {
    return artPreviewCaption;
  }
  public void setArtPreviewCaption(String artPreviewCaption) {
    this.artPreviewCaption = artPreviewCaption;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getArtistThumbnailImageId() {
    return artistThumbnailImageId;
  }
  public void setArtistThumbnailImageId(UUID artistThumbnailImageId) {
    this.artistThumbnailImageId = artistThumbnailImageId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getArtistImageId() {
    return artistImageId;
  }
  public void setArtistImageId(UUID artistImageId) {
    this.artistImageId = artistImageId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getArtPreviewImageId() {
    return artPreviewImageId;
  }
  public void setArtPreviewImageId(UUID artPreviewImageId) {
    this.artPreviewImageId = artPreviewImageId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Dealer dealer = (Dealer) o;
    return (registrationNumber == null ? dealer.registrationNumber == null : registrationNumber.equals(dealer.registrationNumber)) &&
        (attendeeNickname == null ? dealer.attendeeNickname == null : attendeeNickname.equals(dealer.attendeeNickname)) &&
        (displayName == null ? dealer.displayName == null : displayName.equals(dealer.displayName)) &&
        (shortDescription == null ? dealer.shortDescription == null : shortDescription.equals(dealer.shortDescription)) &&
        (aboutTheArtistText == null ? dealer.aboutTheArtistText == null : aboutTheArtistText.equals(dealer.aboutTheArtistText)) &&
        (aboutTheArtText == null ? dealer.aboutTheArtText == null : aboutTheArtText.equals(dealer.aboutTheArtText)) &&
        (websiteUri == null ? dealer.websiteUri == null : websiteUri.equals(dealer.websiteUri)) &&
        (artPreviewCaption == null ? dealer.artPreviewCaption == null : artPreviewCaption.equals(dealer.artPreviewCaption)) &&
        (artistThumbnailImageId == null ? dealer.artistThumbnailImageId == null : artistThumbnailImageId.equals(dealer.artistThumbnailImageId)) &&
        (artistImageId == null ? dealer.artistImageId == null : artistImageId.equals(dealer.artistImageId)) &&
        (artPreviewImageId == null ? dealer.artPreviewImageId == null : artPreviewImageId.equals(dealer.artPreviewImageId));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (registrationNumber == null ? 0: registrationNumber.hashCode());
    result = 31 * result + (attendeeNickname == null ? 0: attendeeNickname.hashCode());
    result = 31 * result + (displayName == null ? 0: displayName.hashCode());
    result = 31 * result + (shortDescription == null ? 0: shortDescription.hashCode());
    result = 31 * result + (aboutTheArtistText == null ? 0: aboutTheArtistText.hashCode());
    result = 31 * result + (aboutTheArtText == null ? 0: aboutTheArtText.hashCode());
    result = 31 * result + (websiteUri == null ? 0: websiteUri.hashCode());
    result = 31 * result + (artPreviewCaption == null ? 0: artPreviewCaption.hashCode());
    result = 31 * result + (artistThumbnailImageId == null ? 0: artistThumbnailImageId.hashCode());
    result = 31 * result + (artistImageId == null ? 0: artistImageId.hashCode());
    result = 31 * result + (artPreviewImageId == null ? 0: artPreviewImageId.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dealer {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  registrationNumber: ").append(registrationNumber).append("\n");
    sb.append("  attendeeNickname: ").append(attendeeNickname).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  shortDescription: ").append(shortDescription).append("\n");
    sb.append("  aboutTheArtistText: ").append(aboutTheArtistText).append("\n");
    sb.append("  aboutTheArtText: ").append(aboutTheArtText).append("\n");
    sb.append("  websiteUri: ").append(websiteUri).append("\n");
    sb.append("  artPreviewCaption: ").append(artPreviewCaption).append("\n");
    sb.append("  artistThumbnailImageId: ").append(artistThumbnailImageId).append("\n");
    sb.append("  artistImageId: ").append(artistImageId).append("\n");
    sb.append("  artPreviewImageId: ").append(artPreviewImageId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
