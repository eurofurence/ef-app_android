package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class FursuitBadgeRegistration  {
  
  @SerializedName("BadgeNo")
  private Integer badgeNo = null;
  @SerializedName("RegNo")
  private Integer regNo = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("WornBy")
  private String wornBy = null;
  @SerializedName("Species")
  private String species = null;
  @SerializedName("Gender")
  private String gender = null;
  @SerializedName("ImageContent")
  private String imageContent = null;
  @SerializedName("DontPublish")
  private Integer dontPublish = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getBadgeNo() {
    return badgeNo;
  }
  public void setBadgeNo(Integer badgeNo) {
    this.badgeNo = badgeNo;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getRegNo() {
    return regNo;
  }
  public void setRegNo(Integer regNo) {
    this.regNo = regNo;
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
  public String getWornBy() {
    return wornBy;
  }
  public void setWornBy(String wornBy) {
    this.wornBy = wornBy;
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
  public String getImageContent() {
    return imageContent;
  }
  public void setImageContent(String imageContent) {
    this.imageContent = imageContent;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getDontPublish() {
    return dontPublish;
  }
  public void setDontPublish(Integer dontPublish) {
    this.dontPublish = dontPublish;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FursuitBadgeRegistration fursuitBadgeRegistration = (FursuitBadgeRegistration) o;
    return (badgeNo == null ? fursuitBadgeRegistration.badgeNo == null : badgeNo.equals(fursuitBadgeRegistration.badgeNo)) &&
        (regNo == null ? fursuitBadgeRegistration.regNo == null : regNo.equals(fursuitBadgeRegistration.regNo)) &&
        (name == null ? fursuitBadgeRegistration.name == null : name.equals(fursuitBadgeRegistration.name)) &&
        (wornBy == null ? fursuitBadgeRegistration.wornBy == null : wornBy.equals(fursuitBadgeRegistration.wornBy)) &&
        (species == null ? fursuitBadgeRegistration.species == null : species.equals(fursuitBadgeRegistration.species)) &&
        (gender == null ? fursuitBadgeRegistration.gender == null : gender.equals(fursuitBadgeRegistration.gender)) &&
        (imageContent == null ? fursuitBadgeRegistration.imageContent == null : imageContent.equals(fursuitBadgeRegistration.imageContent)) &&
        (dontPublish == null ? fursuitBadgeRegistration.dontPublish == null : dontPublish.equals(fursuitBadgeRegistration.dontPublish));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (badgeNo == null ? 0: badgeNo.hashCode());
    result = 31 * result + (regNo == null ? 0: regNo.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (wornBy == null ? 0: wornBy.hashCode());
    result = 31 * result + (species == null ? 0: species.hashCode());
    result = 31 * result + (gender == null ? 0: gender.hashCode());
    result = 31 * result + (imageContent == null ? 0: imageContent.hashCode());
    result = 31 * result + (dontPublish == null ? 0: dontPublish.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class FursuitBadgeRegistration {\n");
    
    sb.append("  badgeNo: ").append(badgeNo).append("\n");
    sb.append("  regNo: ").append(regNo).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  wornBy: ").append(wornBy).append("\n");
    sb.append("  species: ").append(species).append("\n");
    sb.append("  gender: ").append(gender).append("\n");
    sb.append("  imageContent: ").append(imageContent).append("\n");
    sb.append("  dontPublish: ").append(dontPublish).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
