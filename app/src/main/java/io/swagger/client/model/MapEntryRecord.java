package io.swagger.client.model;

import io.swagger.client.model.LinkFragment;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class MapEntryRecord  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("RelativeX")
  private Double relativeX = null;
  @SerializedName("RelativeY")
  private Double relativeY = null;
  @SerializedName("RelativeTapRadius")
  private Double relativeTapRadius = null;
  @SerializedName("Link")
  private LinkFragment link = null;

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
  public Double getRelativeX() {
    return relativeX;
  }
  public void setRelativeX(Double relativeX) {
    this.relativeX = relativeX;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Double getRelativeY() {
    return relativeY;
  }
  public void setRelativeY(Double relativeY) {
    this.relativeY = relativeY;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Double getRelativeTapRadius() {
    return relativeTapRadius;
  }
  public void setRelativeTapRadius(Double relativeTapRadius) {
    this.relativeTapRadius = relativeTapRadius;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public LinkFragment getLink() {
    return link;
  }
  public void setLink(LinkFragment link) {
    this.link = link;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapEntryRecord mapEntryRecord = (MapEntryRecord) o;
    return (id == null ? mapEntryRecord.id == null : id.equals(mapEntryRecord.id)) &&
        (relativeX == null ? mapEntryRecord.relativeX == null : relativeX.equals(mapEntryRecord.relativeX)) &&
        (relativeY == null ? mapEntryRecord.relativeY == null : relativeY.equals(mapEntryRecord.relativeY)) &&
        (relativeTapRadius == null ? mapEntryRecord.relativeTapRadius == null : relativeTapRadius.equals(mapEntryRecord.relativeTapRadius)) &&
        (link == null ? mapEntryRecord.link == null : link.equals(mapEntryRecord.link));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (relativeX == null ? 0: relativeX.hashCode());
    result = 31 * result + (relativeY == null ? 0: relativeY.hashCode());
    result = 31 * result + (relativeTapRadius == null ? 0: relativeTapRadius.hashCode());
    result = 31 * result + (link == null ? 0: link.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MapEntryRecord {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  relativeX: ").append(relativeX).append("\n");
    sb.append("  relativeY: ").append(relativeY).append("\n");
    sb.append("  relativeTapRadius: ").append(relativeTapRadius).append("\n");
    sb.append("  link: ").append(link).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
