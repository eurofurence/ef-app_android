package io.swagger.client.model;

import io.swagger.client.model.EntityBase;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class MapEntity extends EntityBase {
  
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("IsBrowseable")
  private BigDecimal isBrowseable = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getImageId() {
    return imageId;
  }
  public void setImageId(UUID imageId) {
    this.imageId = imageId;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * minimum: 0.0
   * maximum: 1.0
   **/
  @ApiModelProperty(required = true, value = "")
  public BigDecimal getIsBrowseable() {
    return isBrowseable;
  }
  public void setIsBrowseable(BigDecimal isBrowseable) {
    this.isBrowseable = isBrowseable;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapEntity mapEntity = (MapEntity) o;
    return (imageId == null ? mapEntity.imageId == null : imageId.equals(mapEntity.imageId)) &&
        (description == null ? mapEntity.description == null : description.equals(mapEntity.description)) &&
        (isBrowseable == null ? mapEntity.isBrowseable == null : isBrowseable.equals(mapEntity.isBrowseable));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (imageId == null ? 0: imageId.hashCode());
    result = 31 * result + (description == null ? 0: description.hashCode());
    result = 31 * result + (isBrowseable == null ? 0: isBrowseable.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MapEntity {\n");
    sb.append("  " + super.toString()).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  isBrowseable: ").append(isBrowseable).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
