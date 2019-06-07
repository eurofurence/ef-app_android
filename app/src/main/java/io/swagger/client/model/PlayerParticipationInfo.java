package io.swagger.client.model;

import io.swagger.client.model.BadgeInfo;
import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PlayerParticipationInfo  {
  
  @SerializedName("Name")
  private String name = null;
  @SerializedName("IsBanned")
  private Boolean isBanned = null;
  @SerializedName("CollectionCount")
  private Integer collectionCount = null;
  @SerializedName("ScoreboardRank")
  private Integer scoreboardRank = null;
  @SerializedName("RecentlyCollected")
  private List<BadgeInfo> recentlyCollected = null;

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
  public Boolean getIsBanned() {
    return isBanned;
  }
  public void setIsBanned(Boolean isBanned) {
    this.isBanned = isBanned;
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

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getScoreboardRank() {
    return scoreboardRank;
  }
  public void setScoreboardRank(Integer scoreboardRank) {
    this.scoreboardRank = scoreboardRank;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<BadgeInfo> getRecentlyCollected() {
    return recentlyCollected;
  }
  public void setRecentlyCollected(List<BadgeInfo> recentlyCollected) {
    this.recentlyCollected = recentlyCollected;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerParticipationInfo playerParticipationInfo = (PlayerParticipationInfo) o;
    return (name == null ? playerParticipationInfo.name == null : name.equals(playerParticipationInfo.name)) &&
        (isBanned == null ? playerParticipationInfo.isBanned == null : isBanned.equals(playerParticipationInfo.isBanned)) &&
        (collectionCount == null ? playerParticipationInfo.collectionCount == null : collectionCount.equals(playerParticipationInfo.collectionCount)) &&
        (scoreboardRank == null ? playerParticipationInfo.scoreboardRank == null : scoreboardRank.equals(playerParticipationInfo.scoreboardRank)) &&
        (recentlyCollected == null ? playerParticipationInfo.recentlyCollected == null : recentlyCollected.equals(playerParticipationInfo.recentlyCollected));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (isBanned == null ? 0: isBanned.hashCode());
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    result = 31 * result + (scoreboardRank == null ? 0: scoreboardRank.hashCode());
    result = 31 * result + (recentlyCollected == null ? 0: recentlyCollected.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerParticipationInfo {\n");
    
    sb.append("  name: ").append(name).append("\n");
    sb.append("  isBanned: ").append(isBanned).append("\n");
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("  scoreboardRank: ").append(scoreboardRank).append("\n");
    sb.append("  recentlyCollected: ").append(recentlyCollected).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
