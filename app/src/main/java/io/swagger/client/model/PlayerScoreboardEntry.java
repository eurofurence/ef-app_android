package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PlayerScoreboardEntry  {
  
  @SerializedName("CollectionCount")
  private Integer collectionCount = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Rank")
  private Integer rank = null;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerScoreboardEntry playerScoreboardEntry = (PlayerScoreboardEntry) o;
    return (collectionCount == null ? playerScoreboardEntry.collectionCount == null : collectionCount.equals(playerScoreboardEntry.collectionCount)) &&
        (name == null ? playerScoreboardEntry.name == null : name.equals(playerScoreboardEntry.name)) &&
        (rank == null ? playerScoreboardEntry.rank == null : rank.equals(playerScoreboardEntry.rank));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (rank == null ? 0: rank.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerScoreboardEntry {\n");
    
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  rank: ").append(rank).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
