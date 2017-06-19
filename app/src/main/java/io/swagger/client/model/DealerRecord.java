package io.swagger.client.model;

import io.swagger.client.model.LinkFragment;
import java.util.*;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class DealerRecord  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("RegistrationNumber")
  private Integer registrationNumber = null;
  @SerializedName("AttendeeNickname")
  private String attendeeNickname = null;
  @SerializedName("DisplayName")
  private String displayName = null;
  @SerializedName("Merchandise")
  private String merchandise = null;
  @SerializedName("ShortDescription")
  private String shortDescription = null;
  @SerializedName("AboutTheArtistText")
  private String aboutTheArtistText = null;
  @SerializedName("AboutTheArtText")
  private String aboutTheArtText = null;
  @SerializedName("Links")
  private List<LinkFragment> links = null;
  @SerializedName("TwitterHandle")
  private String twitterHandle = null;
  @SerializedName("TelegramHandle")
  private String telegramHandle = null;
  @SerializedName("AttendsOnThursday")
  private Boolean attendsOnThursday = null;
  @SerializedName("AttendsOnFriday")
  private Boolean attendsOnFriday = null;
  @SerializedName("AttendsOnSaturday")
  private Boolean attendsOnSaturday = null;
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
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

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
  public String getMerchandise() {
    return merchandise;
  }
  public void setMerchandise(String merchandise) {
    this.merchandise = merchandise;
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
  public String getAboutTheArtistText() {
    return aboutTheArtistText;
  }
  public void setAboutTheArtistText(String aboutTheArtistText) {
    this.aboutTheArtistText = aboutTheArtistText;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getAboutTheArtText() {
    return aboutTheArtText;
  }
  public void setAboutTheArtText(String aboutTheArtText) {
    this.aboutTheArtText = aboutTheArtText;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public List<LinkFragment> getLinks() {
    return links;
  }
  public void setLinks(List<LinkFragment> links) {
    this.links = links;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTwitterHandle() {
    return twitterHandle;
  }
  public void setTwitterHandle(String twitterHandle) {
    this.twitterHandle = twitterHandle;
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
  public Boolean getAttendsOnThursday() {
    return attendsOnThursday;
  }
  public void setAttendsOnThursday(Boolean attendsOnThursday) {
    this.attendsOnThursday = attendsOnThursday;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getAttendsOnFriday() {
    return attendsOnFriday;
  }
  public void setAttendsOnFriday(Boolean attendsOnFriday) {
    this.attendsOnFriday = attendsOnFriday;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getAttendsOnSaturday() {
    return attendsOnSaturday;
  }
  public void setAttendsOnSaturday(Boolean attendsOnSaturday) {
    this.attendsOnSaturday = attendsOnSaturday;
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
    DealerRecord dealerRecord = (DealerRecord) o;
    return (id == null ? dealerRecord.id == null : id.equals(dealerRecord.id)) &&
        (lastChangeDateTimeUtc == null ? dealerRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(dealerRecord.lastChangeDateTimeUtc)) &&
        (registrationNumber == null ? dealerRecord.registrationNumber == null : registrationNumber.equals(dealerRecord.registrationNumber)) &&
        (attendeeNickname == null ? dealerRecord.attendeeNickname == null : attendeeNickname.equals(dealerRecord.attendeeNickname)) &&
        (displayName == null ? dealerRecord.displayName == null : displayName.equals(dealerRecord.displayName)) &&
        (merchandise == null ? dealerRecord.merchandise == null : merchandise.equals(dealerRecord.merchandise)) &&
        (shortDescription == null ? dealerRecord.shortDescription == null : shortDescription.equals(dealerRecord.shortDescription)) &&
        (aboutTheArtistText == null ? dealerRecord.aboutTheArtistText == null : aboutTheArtistText.equals(dealerRecord.aboutTheArtistText)) &&
        (aboutTheArtText == null ? dealerRecord.aboutTheArtText == null : aboutTheArtText.equals(dealerRecord.aboutTheArtText)) &&
        (links == null ? dealerRecord.links == null : links.equals(dealerRecord.links)) &&
        (twitterHandle == null ? dealerRecord.twitterHandle == null : twitterHandle.equals(dealerRecord.twitterHandle)) &&
        (telegramHandle == null ? dealerRecord.telegramHandle == null : telegramHandle.equals(dealerRecord.telegramHandle)) &&
        (attendsOnThursday == null ? dealerRecord.attendsOnThursday == null : attendsOnThursday.equals(dealerRecord.attendsOnThursday)) &&
        (attendsOnFriday == null ? dealerRecord.attendsOnFriday == null : attendsOnFriday.equals(dealerRecord.attendsOnFriday)) &&
        (attendsOnSaturday == null ? dealerRecord.attendsOnSaturday == null : attendsOnSaturday.equals(dealerRecord.attendsOnSaturday)) &&
        (artPreviewCaption == null ? dealerRecord.artPreviewCaption == null : artPreviewCaption.equals(dealerRecord.artPreviewCaption)) &&
        (artistThumbnailImageId == null ? dealerRecord.artistThumbnailImageId == null : artistThumbnailImageId.equals(dealerRecord.artistThumbnailImageId)) &&
        (artistImageId == null ? dealerRecord.artistImageId == null : artistImageId.equals(dealerRecord.artistImageId)) &&
        (artPreviewImageId == null ? dealerRecord.artPreviewImageId == null : artPreviewImageId.equals(dealerRecord.artPreviewImageId));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (registrationNumber == null ? 0: registrationNumber.hashCode());
    result = 31 * result + (attendeeNickname == null ? 0: attendeeNickname.hashCode());
    result = 31 * result + (displayName == null ? 0: displayName.hashCode());
    result = 31 * result + (merchandise == null ? 0: merchandise.hashCode());
    result = 31 * result + (shortDescription == null ? 0: shortDescription.hashCode());
    result = 31 * result + (aboutTheArtistText == null ? 0: aboutTheArtistText.hashCode());
    result = 31 * result + (aboutTheArtText == null ? 0: aboutTheArtText.hashCode());
    result = 31 * result + (links == null ? 0: links.hashCode());
    result = 31 * result + (twitterHandle == null ? 0: twitterHandle.hashCode());
    result = 31 * result + (telegramHandle == null ? 0: telegramHandle.hashCode());
    result = 31 * result + (attendsOnThursday == null ? 0: attendsOnThursday.hashCode());
    result = 31 * result + (attendsOnFriday == null ? 0: attendsOnFriday.hashCode());
    result = 31 * result + (attendsOnSaturday == null ? 0: attendsOnSaturday.hashCode());
    result = 31 * result + (artPreviewCaption == null ? 0: artPreviewCaption.hashCode());
    result = 31 * result + (artistThumbnailImageId == null ? 0: artistThumbnailImageId.hashCode());
    result = 31 * result + (artistImageId == null ? 0: artistImageId.hashCode());
    result = 31 * result + (artPreviewImageId == null ? 0: artPreviewImageId.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DealerRecord {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  registrationNumber: ").append(registrationNumber).append("\n");
    sb.append("  attendeeNickname: ").append(attendeeNickname).append("\n");
    sb.append("  displayName: ").append(displayName).append("\n");
    sb.append("  merchandise: ").append(merchandise).append("\n");
    sb.append("  shortDescription: ").append(shortDescription).append("\n");
    sb.append("  aboutTheArtistText: ").append(aboutTheArtistText).append("\n");
    sb.append("  aboutTheArtText: ").append(aboutTheArtText).append("\n");
    sb.append("  links: ").append(links).append("\n");
    sb.append("  twitterHandle: ").append(twitterHandle).append("\n");
    sb.append("  telegramHandle: ").append(telegramHandle).append("\n");
    sb.append("  attendsOnThursday: ").append(attendsOnThursday).append("\n");
    sb.append("  attendsOnFriday: ").append(attendsOnFriday).append("\n");
    sb.append("  attendsOnSaturday: ").append(attendsOnSaturday).append("\n");
    sb.append("  artPreviewCaption: ").append(artPreviewCaption).append("\n");
    sb.append("  artistThumbnailImageId: ").append(artistThumbnailImageId).append("\n");
    sb.append("  artistImageId: ").append(artistImageId).append("\n");
    sb.append("  artPreviewImageId: ").append(artPreviewImageId).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
