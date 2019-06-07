package io.swagger.client.model;

import io.swagger.client.model.LinkFragment;
import java.util.*;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class MapEntryRecord  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("X")
  private Integer X = null;
  @SerializedName("Y")
  private Integer Y = null;
  @SerializedName("TapRadius")
  private Integer tapRadius = null;
  @SerializedName("Links")
  private List<LinkFragment> links = null;

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
   * \"X\" coordinate of the *center* of a *circular area*, expressed in pixels.
   **/
  @ApiModelProperty(value = "\"X\" coordinate of the *center* of a *circular area*, expressed in pixels.")
  public Integer getX() {
    return X;
  }
  public void setX(Integer X) {
    this.X = X;
  }

  /**
   * \"Y\" coordinate of the *center* of a *circular area*, expressed in pixels.
   **/
  @ApiModelProperty(value = "\"Y\" coordinate of the *center* of a *circular area*, expressed in pixels.")
  public Integer getY() {
    return Y;
  }
  public void setY(Integer Y) {
    this.Y = Y;
  }

  /**
   * \"Radius\" of a *circular area* (the center of which described with X and Y), expressed in pixels.
   **/
  @ApiModelProperty(value = "\"Radius\" of a *circular area* (the center of which described with X and Y), expressed in pixels.")
  public Integer getTapRadius() {
    return tapRadius;
  }
  public void setTapRadius(Integer tapRadius) {
    this.tapRadius = tapRadius;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<LinkFragment> getLinks() {
    return links;
  }
  public void setLinks(List<LinkFragment> links) {
    this.links = links;
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
        (X == null ? mapEntryRecord.X == null : X.equals(mapEntryRecord.X)) &&
        (Y == null ? mapEntryRecord.Y == null : Y.equals(mapEntryRecord.Y)) &&
        (tapRadius == null ? mapEntryRecord.tapRadius == null : tapRadius.equals(mapEntryRecord.tapRadius)) &&
        (links == null ? mapEntryRecord.links == null : links.equals(mapEntryRecord.links));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (X == null ? 0: X.hashCode());
    result = 31 * result + (Y == null ? 0: Y.hashCode());
    result = 31 * result + (tapRadius == null ? 0: tapRadius.hashCode());
    result = 31 * result + (links == null ? 0: links.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MapEntryRecord {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  X: ").append(X).append("\n");
    sb.append("  Y: ").append(Y).append("\n");
    sb.append("  tapRadius: ").append(tapRadius).append("\n");
    sb.append("  links: ").append(links).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
