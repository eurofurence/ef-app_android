package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Dealer extends EntityBase {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("IsDeleted")
  private BigDecimal isDeleted = null;
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
   * Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000
   **/
  @ApiModelProperty(required = true, value = "Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000")
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

  
  /**
   * Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)
   **/
  @ApiModelProperty(required = true, value = "Date & Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion)")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  
  /**
   * Numeric flag that, if set to \"1\", indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval.
   * minimum: 0.0
   * maximum: 1.0
   **/
  @ApiModelProperty(required = true, value = "Numeric flag that, if set to \"1\", indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval.")
  public BigDecimal getIsDeleted() {
    return isDeleted;
  }
  public void setIsDeleted(BigDecimal isDeleted) {
    this.isDeleted = isDeleted;
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
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Dealer {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  isDeleted: ").append(isDeleted).append("\n");
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
