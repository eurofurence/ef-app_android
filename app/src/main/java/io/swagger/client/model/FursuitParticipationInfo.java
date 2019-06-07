package io.swagger.client.model;

import io.swagger.client.model.FursuitBadgeRecord;
import io.swagger.client.model.FursuitParticipationRecord;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class FursuitParticipationInfo  {
  
  @SerializedName("Badge")
  private FursuitBadgeRecord badge = null;
  @SerializedName("IsParticipating")
  private Boolean isParticipating = null;
  @SerializedName("Participation")
  private FursuitParticipationRecord participation = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public FursuitBadgeRecord getBadge() {
    return badge;
  }
  public void setBadge(FursuitBadgeRecord badge) {
    this.badge = badge;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getIsParticipating() {
    return isParticipating;
  }
  public void setIsParticipating(Boolean isParticipating) {
    this.isParticipating = isParticipating;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public FursuitParticipationRecord getParticipation() {
    return participation;
  }
  public void setParticipation(FursuitParticipationRecord participation) {
    this.participation = participation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FursuitParticipationInfo fursuitParticipationInfo = (FursuitParticipationInfo) o;
    return (badge == null ? fursuitParticipationInfo.badge == null : badge.equals(fursuitParticipationInfo.badge)) &&
        (isParticipating == null ? fursuitParticipationInfo.isParticipating == null : isParticipating.equals(fursuitParticipationInfo.isParticipating)) &&
        (participation == null ? fursuitParticipationInfo.participation == null : participation.equals(fursuitParticipationInfo.participation));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (badge == null ? 0: badge.hashCode());
    result = 31 * result + (isParticipating == null ? 0: isParticipating.hashCode());
    result = 31 * result + (participation == null ? 0: participation.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FursuitParticipationInfo {\n");
    
    sb.append("  badge: ").append(badge).append("\n");
    sb.append("  isParticipating: ").append(isParticipating).append("\n");
    sb.append("  participation: ").append(participation).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
