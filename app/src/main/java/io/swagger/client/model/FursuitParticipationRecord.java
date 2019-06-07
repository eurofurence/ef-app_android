package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class FursuitParticipationRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("OwnerUid")
  private String ownerUid = null;
  @SerializedName("FursuitBadgeId")
  private UUID fursuitBadgeId = null;
  @SerializedName("TokenValue")
  private String tokenValue = null;
  @SerializedName("IsBanned")
  private Boolean isBanned = null;
  @SerializedName("TokenRegistrationDateTimeUtc")
  private Date tokenRegistrationDateTimeUtc = null;
  @SerializedName("LastCollectionDateTimeUtc")
  private Date lastCollectionDateTimeUtc = null;
  @SerializedName("CollectionCount")
  private Integer collectionCount = null;

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
  public String getOwnerUid() {
    return ownerUid;
  }
  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getFursuitBadgeId() {
    return fursuitBadgeId;
  }
  public void setFursuitBadgeId(UUID fursuitBadgeId) {
    this.fursuitBadgeId = fursuitBadgeId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTokenValue() {
    return tokenValue;
  }
  public void setTokenValue(String tokenValue) {
    this.tokenValue = tokenValue;
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
  public Date getTokenRegistrationDateTimeUtc() {
    return tokenRegistrationDateTimeUtc;
  }
  public void setTokenRegistrationDateTimeUtc(Date tokenRegistrationDateTimeUtc) {
    this.tokenRegistrationDateTimeUtc = tokenRegistrationDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getLastCollectionDateTimeUtc() {
    return lastCollectionDateTimeUtc;
  }
  public void setLastCollectionDateTimeUtc(Date lastCollectionDateTimeUtc) {
    this.lastCollectionDateTimeUtc = lastCollectionDateTimeUtc;
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
    FursuitParticipationRecord fursuitParticipationRecord = (FursuitParticipationRecord) o;
    return (lastChangeDateTimeUtc == null ? fursuitParticipationRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(fursuitParticipationRecord.lastChangeDateTimeUtc)) &&
        (id == null ? fursuitParticipationRecord.id == null : id.equals(fursuitParticipationRecord.id)) &&
        (ownerUid == null ? fursuitParticipationRecord.ownerUid == null : ownerUid.equals(fursuitParticipationRecord.ownerUid)) &&
        (fursuitBadgeId == null ? fursuitParticipationRecord.fursuitBadgeId == null : fursuitBadgeId.equals(fursuitParticipationRecord.fursuitBadgeId)) &&
        (tokenValue == null ? fursuitParticipationRecord.tokenValue == null : tokenValue.equals(fursuitParticipationRecord.tokenValue)) &&
        (isBanned == null ? fursuitParticipationRecord.isBanned == null : isBanned.equals(fursuitParticipationRecord.isBanned)) &&
        (tokenRegistrationDateTimeUtc == null ? fursuitParticipationRecord.tokenRegistrationDateTimeUtc == null : tokenRegistrationDateTimeUtc.equals(fursuitParticipationRecord.tokenRegistrationDateTimeUtc)) &&
        (lastCollectionDateTimeUtc == null ? fursuitParticipationRecord.lastCollectionDateTimeUtc == null : lastCollectionDateTimeUtc.equals(fursuitParticipationRecord.lastCollectionDateTimeUtc)) &&
        (collectionCount == null ? fursuitParticipationRecord.collectionCount == null : collectionCount.equals(fursuitParticipationRecord.collectionCount));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (ownerUid == null ? 0: ownerUid.hashCode());
    result = 31 * result + (fursuitBadgeId == null ? 0: fursuitBadgeId.hashCode());
    result = 31 * result + (tokenValue == null ? 0: tokenValue.hashCode());
    result = 31 * result + (isBanned == null ? 0: isBanned.hashCode());
    result = 31 * result + (tokenRegistrationDateTimeUtc == null ? 0: tokenRegistrationDateTimeUtc.hashCode());
    result = 31 * result + (lastCollectionDateTimeUtc == null ? 0: lastCollectionDateTimeUtc.hashCode());
    result = 31 * result + (collectionCount == null ? 0: collectionCount.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FursuitParticipationRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  ownerUid: ").append(ownerUid).append("\n");
    sb.append("  fursuitBadgeId: ").append(fursuitBadgeId).append("\n");
    sb.append("  tokenValue: ").append(tokenValue).append("\n");
    sb.append("  isBanned: ").append(isBanned).append("\n");
    sb.append("  tokenRegistrationDateTimeUtc: ").append(tokenRegistrationDateTimeUtc).append("\n");
    sb.append("  lastCollectionDateTimeUtc: ").append(lastCollectionDateTimeUtc).append("\n");
    sb.append("  collectionCount: ").append(collectionCount).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
