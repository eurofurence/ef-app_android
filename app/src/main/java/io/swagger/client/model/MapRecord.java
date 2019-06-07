package io.swagger.client.model;

import io.swagger.client.model.MapEntryRecord;
import java.util.*;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class MapRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("ImageId")
  private UUID imageId = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("IsBrowseable")
  private Boolean isBrowseable = null;
  @SerializedName("Entries")
  private List<MapEntryRecord> entries = null;

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
   **/
  @ApiModelProperty(required = true, value = "")
  public Boolean getIsBrowseable() {
    return isBrowseable;
  }
  public void setIsBrowseable(Boolean isBrowseable) {
    this.isBrowseable = isBrowseable;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public List<MapEntryRecord> getEntries() {
    return entries;
  }
  public void setEntries(List<MapEntryRecord> entries) {
    this.entries = entries;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MapRecord mapRecord = (MapRecord) o;
    return (lastChangeDateTimeUtc == null ? mapRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(mapRecord.lastChangeDateTimeUtc)) &&
        (id == null ? mapRecord.id == null : id.equals(mapRecord.id)) &&
        (imageId == null ? mapRecord.imageId == null : imageId.equals(mapRecord.imageId)) &&
        (description == null ? mapRecord.description == null : description.equals(mapRecord.description)) &&
        (isBrowseable == null ? mapRecord.isBrowseable == null : isBrowseable.equals(mapRecord.isBrowseable)) &&
        (entries == null ? mapRecord.entries == null : entries.equals(mapRecord.entries));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (imageId == null ? 0: imageId.hashCode());
    result = 31 * result + (description == null ? 0: description.hashCode());
    result = 31 * result + (isBrowseable == null ? 0: isBrowseable.hashCode());
    result = 31 * result + (entries == null ? 0: entries.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class MapRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  imageId: ").append(imageId).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  isBrowseable: ").append(isBrowseable).append("\n");
    sb.append("  entries: ").append(entries).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
