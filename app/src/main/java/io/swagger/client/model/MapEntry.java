package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class MapEntry extends EntityBase {
  
  @SerializedName("MapId")
  private UUID mapId = null;
  @SerializedName("RelativeX")
  private BigDecimal relativeX = null;
  @SerializedName("RelativeY")
  private BigDecimal relativeY = null;
  @SerializedName("RelativeTapRadius")
  private BigDecimal relativeTapRadius = null;
  @SerializedName("MarkerType")
  private String markerType = null;
  @SerializedName("TargetId")
  private UUID targetId = null;
  @SerializedName("TargetDescription")
  private String targetDescription = null;

  /**
   * Id of the Map entity this entry belongs to
   **/
  @ApiModelProperty(required = true, value = "Id of the Map entity this entry belongs to")
  public UUID getMapId() {
    return mapId;
  }
  public void setMapId(UUID mapId) {
    this.mapId = mapId;
  }

  /**
   * Relative X position on the map (0-100 scale). To convert to absolute position, use this formula: AbsoluteX = [RelativeX / 100] * [Map.Image.Width]
   * minimum: 0.0
   * maximum: 100.0
   **/
  @ApiModelProperty(required = true, value = "Relative X position on the map (0-100 scale). To convert to absolute position, use this formula: AbsoluteX = [RelativeX / 100] * [Map.Image.Width]")
  public BigDecimal getRelativeX() {
    return relativeX;
  }
  public void setRelativeX(BigDecimal relativeX) {
    this.relativeX = relativeX;
  }

  /**
   * Relative Y position on the map (0-100 scale). To convert to absolute position, use this formula: AbsoluteY = [RelativeY / 100] * [Map.Image.Height]
   * minimum: 0.0
   * maximum: 100.0
   **/
  @ApiModelProperty(required = true, value = "Relative Y position on the map (0-100 scale). To convert to absolute position, use this formula: AbsoluteY = [RelativeY / 100] * [Map.Image.Height]")
  public BigDecimal getRelativeY() {
    return relativeY;
  }
  public void setRelativeY(BigDecimal relativeY) {
    this.relativeY = relativeY;
  }

  /**
   * Radius around the center defined by x/y which should be tap-active for events regarding this map entry. Scale 0-1, relative to Height. To convert to absolute radius, use this formula: AbsoluteTapRadius = RelativeTapradius * [Map.Image.Height]
   * minimum: 0.0
   * maximum: 1.0
   **/
  @ApiModelProperty(required = true, value = "Radius around the center defined by x/y which should be tap-active for events regarding this map entry. Scale 0-1, relative to Height. To convert to absolute radius, use this formula: AbsoluteTapRadius = RelativeTapradius * [Map.Image.Height]")
  public BigDecimal getRelativeTapRadius() {
    return relativeTapRadius;
  }
  public void setRelativeTapRadius(BigDecimal relativeTapRadius) {
    this.relativeTapRadius = relativeTapRadius;
  }

  /**
   * Type of marker. Valid options: 'Dealer'
   **/
  @ApiModelProperty(required = true, value = "Type of marker. Valid options: 'Dealer'")
  public String getMarkerType() {
    return markerType;
  }
  public void setMarkerType(String markerType) {
    this.markerType = markerType;
  }

  /**
   * Target entity referenced by the marker, e.G. for a marker of type 'Dealer', this would id of the dealer.
   **/
  @ApiModelProperty(value = "Target entity referenced by the marker, e.G. for a marker of type 'Dealer', this would id of the dealer.")
  public UUID getTargetId() {
    return targetId;
  }
  public void setTargetId(UUID targetId) {
    this.targetId = targetId;
  }

  /**
   * Alternative description for marker types that do not reference an entity (or need additional data9, not used yet.
   **/
  @ApiModelProperty(value = "Alternative description for marker types that do not reference an entity (or need additional data9, not used yet.")
  public String getTargetDescription() {
    return targetDescription;
  }
  public void setTargetDescription(String targetDescription) {
    this.targetDescription = targetDescription;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapEntry mapEntry = (MapEntry) o;
    return (mapId == null ? mapEntry.mapId == null : mapId.equals(mapEntry.mapId)) &&
        (relativeX == null ? mapEntry.relativeX == null : relativeX.equals(mapEntry.relativeX)) &&
        (relativeY == null ? mapEntry.relativeY == null : relativeY.equals(mapEntry.relativeY)) &&
        (relativeTapRadius == null ? mapEntry.relativeTapRadius == null : relativeTapRadius.equals(mapEntry.relativeTapRadius)) &&
        (markerType == null ? mapEntry.markerType == null : markerType.equals(mapEntry.markerType)) &&
        (targetId == null ? mapEntry.targetId == null : targetId.equals(mapEntry.targetId)) &&
        (targetDescription == null ? mapEntry.targetDescription == null : targetDescription.equals(mapEntry.targetDescription));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (mapId == null ? 0: mapId.hashCode());
    result = 31 * result + (relativeX == null ? 0: relativeX.hashCode());
    result = 31 * result + (relativeY == null ? 0: relativeY.hashCode());
    result = 31 * result + (relativeTapRadius == null ? 0: relativeTapRadius.hashCode());
    result = 31 * result + (markerType == null ? 0: markerType.hashCode());
    result = 31 * result + (targetId == null ? 0: targetId.hashCode());
    result = 31 * result + (targetDescription == null ? 0: targetDescription.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MapEntry {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  mapId: ").append(mapId).append("\n");
    sb.append("  relativeX: ").append(relativeX).append("\n");
    sb.append("  relativeY: ").append(relativeY).append("\n");
    sb.append("  relativeTapRadius: ").append(relativeTapRadius).append("\n");
    sb.append("  markerType: ").append(markerType).append("\n");
    sb.append("  targetId: ").append(targetId).append("\n");
    sb.append("  targetDescription: ").append(targetDescription).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
