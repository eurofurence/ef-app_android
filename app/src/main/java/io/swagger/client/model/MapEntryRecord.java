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
   * \"X\" coordinate of the *center* of a *circular area*, expressed as `[Relative Fraction of Map Image Width * 100]`.    *  A value of `RelativeX=50` indicates it's on the horizontal middle of a map, and results in an absolute X of 1000 on a map that is 2000 pixels wide,       or 500 on a map that is 1000 pixels wide. 0 would indicate the far left side, where as 100 indicates the far right side of the image.
   **/
  @ApiModelProperty(value = "\"X\" coordinate of the *center* of a *circular area*, expressed as `[Relative Fraction of Map Image Width * 100]`.    *  A value of `RelativeX=50` indicates it's on the horizontal middle of a map, and results in an absolute X of 1000 on a map that is 2000 pixels wide,       or 500 on a map that is 1000 pixels wide. 0 would indicate the far left side, where as 100 indicates the far right side of the image.")
  public Double getRelativeX() {
    return relativeX;
  }
  public void setRelativeX(Double relativeX) {
    this.relativeX = relativeX;
  }

  /**
   * \"Y\" coordinate of the *center* of a *circular area*, expressed as `[Relative Fraction of Map Image Height * 100]`.   *  A value of `RelativeY=50` indicates it's on the vertical middle of a map, and results in an absolute Y of 1000 on a map that is 2000 pixels in height,      or 500 on a map that is 1000 in height. 0 would indicate the top side, where as 100 indicates the bottom side of the image.
   **/
  @ApiModelProperty(value = "\"Y\" coordinate of the *center* of a *circular area*, expressed as `[Relative Fraction of Map Image Height * 100]`.   *  A value of `RelativeY=50` indicates it's on the vertical middle of a map, and results in an absolute Y of 1000 on a map that is 2000 pixels in height,      or 500 on a map that is 1000 in height. 0 would indicate the top side, where as 100 indicates the bottom side of the image.")
  public Double getRelativeY() {
    return relativeY;
  }
  public void setRelativeY(Double relativeY) {
    this.relativeY = relativeY;
  }

  /**
   * \"Radius\" of a *circular area* (the center of which described with RelativeX and RelativeY), expressed as `[Relative Fraction of Map Image Height]`.   *  A value of `RelativeTapRadius=0.02` indicates that the circle has an absolute tap radius of 20 pixels (and a diameter of 40 pixels) on a map that is       1000 pixels in height, or a tap radius of 10 pixels (and a diameter of 20 pixels) on a map that is 500 pixels in height.
   **/
  @ApiModelProperty(value = "\"Radius\" of a *circular area* (the center of which described with RelativeX and RelativeY), expressed as `[Relative Fraction of Map Image Height]`.   *  A value of `RelativeTapRadius=0.02` indicates that the circle has an absolute tap radius of 20 pixels (and a diameter of 40 pixels) on a map that is       1000 pixels in height, or a tap radius of 10 pixels (and a diameter of 20 pixels) on a map that is 500 pixels in height.")
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
