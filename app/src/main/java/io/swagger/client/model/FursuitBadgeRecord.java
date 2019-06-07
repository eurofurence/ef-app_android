package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class FursuitBadgeRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("ExternalReference")
  private String externalReference = null;
  @SerializedName("OwnerUid")
  private String ownerUid = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("WornBy")
  private String wornBy = null;
  @SerializedName("Species")
  private String species = null;
  @SerializedName("Gender")
  private String gender = null;
  @SerializedName("IsPublic")
  private Boolean isPublic = null;

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
  public String getExternalReference() {
    return externalReference;
  }
  public void setExternalReference(String externalReference) {
    this.externalReference = externalReference;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getOwnerUid() {
    return ownerUid;
  }
  public void setOwnerUid(String ownerUid) {
    this.ownerUid = ownerUid;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getWornBy() {
    return wornBy;
  }
  public void setWornBy(String wornBy) {
    this.wornBy = wornBy;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getSpecies() {
    return species;
  }
  public void setSpecies(String species) {
    this.species = species;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getIsPublic() {
    return isPublic;
  }
  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FursuitBadgeRecord fursuitBadgeRecord = (FursuitBadgeRecord) o;
    return (lastChangeDateTimeUtc == null ? fursuitBadgeRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(fursuitBadgeRecord.lastChangeDateTimeUtc)) &&
        (id == null ? fursuitBadgeRecord.id == null : id.equals(fursuitBadgeRecord.id)) &&
        (externalReference == null ? fursuitBadgeRecord.externalReference == null : externalReference.equals(fursuitBadgeRecord.externalReference)) &&
        (ownerUid == null ? fursuitBadgeRecord.ownerUid == null : ownerUid.equals(fursuitBadgeRecord.ownerUid)) &&
        (name == null ? fursuitBadgeRecord.name == null : name.equals(fursuitBadgeRecord.name)) &&
        (wornBy == null ? fursuitBadgeRecord.wornBy == null : wornBy.equals(fursuitBadgeRecord.wornBy)) &&
        (species == null ? fursuitBadgeRecord.species == null : species.equals(fursuitBadgeRecord.species)) &&
        (gender == null ? fursuitBadgeRecord.gender == null : gender.equals(fursuitBadgeRecord.gender)) &&
        (isPublic == null ? fursuitBadgeRecord.isPublic == null : isPublic.equals(fursuitBadgeRecord.isPublic));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (externalReference == null ? 0: externalReference.hashCode());
    result = 31 * result + (ownerUid == null ? 0: ownerUid.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (wornBy == null ? 0: wornBy.hashCode());
    result = 31 * result + (species == null ? 0: species.hashCode());
    result = 31 * result + (gender == null ? 0: gender.hashCode());
    result = 31 * result + (isPublic == null ? 0: isPublic.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FursuitBadgeRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  externalReference: ").append(externalReference).append("\n");
    sb.append("  ownerUid: ").append(ownerUid).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  wornBy: ").append(wornBy).append("\n");
    sb.append("  species: ").append(species).append("\n");
    sb.append("  gender: ").append(gender).append("\n");
    sb.append("  isPublic: ").append(isPublic).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
