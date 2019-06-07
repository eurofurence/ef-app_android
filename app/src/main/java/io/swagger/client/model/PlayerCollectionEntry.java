package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PlayerCollectionEntry  {
  
  @SerializedName("BadgeId")
  private UUID badgeId = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Species")
  private String species = null;
  @SerializedName("Gender")
  private String gender = null;
  @SerializedName("CollectedAtDateTimeUtc")
  private Date collectedAtDateTimeUtc = null;
  @SerializedName("CollectionCount")
  private Integer collectionCount = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getBadgeId() {
    return badgeId;
  }
  public void setBadgeId(UUID badgeId) {
    this.badgeId = badgeId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getSpecies() {
    return species;
  }
  public void setSpecies(String species) {
    this.species = species;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCollectedAtDateTimeUtc() {
    return collectedAtDateTimeUtc;
  }
  public void setCollectedAtDateTimeUtc(Date collectedAtDateTimeUtc) {
    this.collectedAtDateTimeUtc = collectedAtDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getCollectionCount() {
    return collectionCount;
  }
  public void setCollectionCount(Integer collectionCount) {
    this.collectionCount = collectionCount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerCollectionEntry playerCollectionEntry = (PlayerCollectionEntry) o;
    return (badgeId == null ? playerCollectionEntry.badgeId == null : badgeId.equals(playerCollectionEntry.badgeId)) &&
        (name == null ? playerCollectionEntry.name == null : name.equals(playerCollectionEntry.name)) &&
        (species == null ? playerCollectionEntry.species == null : species.equals(playerCollectionEntry.species)) &&
        (gender == null ? playerCollectionEntry.gender == null : gender.equals(playerCollectionEntry.gender)) &&
        (collectedAtDateTimeUtc == null ? playerCollectionEntry.collectedAtDateTimeUtc == null : collectedAtDateTimeUtc.equals(playerCollectionEntry.collectedAtDateTimeUtc)) &&
        (collectionCount == null ? playerCollectionEntry.collectionCount == null : collectionCount.equals(playerCollectionEntry.collectionCount));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (badgeId == null ? 0: badgeId.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (species == null ? 0: species.hashCode());
    result = 31 * result + (gender == null ? 0: gender.hashCode());
    result = 31 * result + (collectedAtDateTimeUtc == null ? 0: collectedAtDateTimeUtc.hashCode());
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerCollectionEntry {\n");
    
    sb.append("  badgeId: ").append(badgeId).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  species: ").append(species).append("\n");
    sb.append("  gender: ").append(gender).append("\n");
    sb.append("  collectedAtDateTimeUtc: ").append(collectedAtDateTimeUtc).append("\n");
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
