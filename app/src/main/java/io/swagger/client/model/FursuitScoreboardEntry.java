package io.swagger.client.model;

import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class FursuitScoreboardEntry  {
  
  @SerializedName("CollectionCount")
  private Integer collectionCount = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Rank")
  private Integer rank = null;
  @SerializedName("BadgeId")
  private UUID badgeId = null;
  @SerializedName("Gender")
  private String gender = null;
  @SerializedName("Species")
  private String species = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getCollectionCount() {
    return collectionCount;
  }
  public void setCollectionCount(Integer collectionCount) {
    this.collectionCount = collectionCount;
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
  public Integer getRank() {
    return rank;
  }
  public void setRank(Integer rank) {
    this.rank = rank;
  }

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
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FursuitScoreboardEntry fursuitScoreboardEntry = (FursuitScoreboardEntry) o;
    return (collectionCount == null ? fursuitScoreboardEntry.collectionCount == null : collectionCount.equals(fursuitScoreboardEntry.collectionCount)) &&
        (name == null ? fursuitScoreboardEntry.name == null : name.equals(fursuitScoreboardEntry.name)) &&
        (rank == null ? fursuitScoreboardEntry.rank == null : rank.equals(fursuitScoreboardEntry.rank)) &&
        (badgeId == null ? fursuitScoreboardEntry.badgeId == null : badgeId.equals(fursuitScoreboardEntry.badgeId)) &&
        (gender == null ? fursuitScoreboardEntry.gender == null : gender.equals(fursuitScoreboardEntry.gender)) &&
        (species == null ? fursuitScoreboardEntry.species == null : species.equals(fursuitScoreboardEntry.species));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (rank == null ? 0: rank.hashCode());
    result = 31 * result + (badgeId == null ? 0: badgeId.hashCode());
    result = 31 * result + (gender == null ? 0: gender.hashCode());
    result = 31 * result + (species == null ? 0: species.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FursuitScoreboardEntry {\n");
    
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  rank: ").append(rank).append("\n");
    sb.append("  badgeId: ").append(badgeId).append("\n");
    sb.append("  gender: ").append(gender).append("\n");
    sb.append("  species: ").append(species).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
