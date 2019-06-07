package io.swagger.client.model;

import io.swagger.client.model.ImageRecord;
import java.util.*;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class DeltaResponseImageRecord  {
  
  @SerializedName("StorageLastChangeDateTimeUtc")
  private Date storageLastChangeDateTimeUtc = null;
  @SerializedName("StorageDeltaStartChangeDateTimeUtc")
  private Date storageDeltaStartChangeDateTimeUtc = null;
  @SerializedName("RemoveAllBeforeInsert")
  private Boolean removeAllBeforeInsert = null;
  @SerializedName("ChangedEntities")
  private List<ImageRecord> changedEntities = null;
  @SerializedName("DeletedEntities")
  private List<UUID> deletedEntities = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getStorageLastChangeDateTimeUtc() {
    return storageLastChangeDateTimeUtc;
  }
  public void setStorageLastChangeDateTimeUtc(Date storageLastChangeDateTimeUtc) {
    this.storageLastChangeDateTimeUtc = storageLastChangeDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getStorageDeltaStartChangeDateTimeUtc() {
    return storageDeltaStartChangeDateTimeUtc;
  }
  public void setStorageDeltaStartChangeDateTimeUtc(Date storageDeltaStartChangeDateTimeUtc) {
    this.storageDeltaStartChangeDateTimeUtc = storageDeltaStartChangeDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getRemoveAllBeforeInsert() {
    return removeAllBeforeInsert;
  }
  public void setRemoveAllBeforeInsert(Boolean removeAllBeforeInsert) {
    this.removeAllBeforeInsert = removeAllBeforeInsert;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<ImageRecord> getChangedEntities() {
    return changedEntities;
  }
  public void setChangedEntities(List<ImageRecord> changedEntities) {
    this.changedEntities = changedEntities;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<UUID> getDeletedEntities() {
    return deletedEntities;
  }
  public void setDeletedEntities(List<UUID> deletedEntities) {
    this.deletedEntities = deletedEntities;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeltaResponseImageRecord deltaResponseImageRecord = (DeltaResponseImageRecord) o;
    return (storageLastChangeDateTimeUtc == null ? deltaResponseImageRecord.storageLastChangeDateTimeUtc == null : storageLastChangeDateTimeUtc.equals(deltaResponseImageRecord.storageLastChangeDateTimeUtc)) &&
        (storageDeltaStartChangeDateTimeUtc == null ? deltaResponseImageRecord.storageDeltaStartChangeDateTimeUtc == null : storageDeltaStartChangeDateTimeUtc.equals(deltaResponseImageRecord.storageDeltaStartChangeDateTimeUtc)) &&
        (removeAllBeforeInsert == null ? deltaResponseImageRecord.removeAllBeforeInsert == null : removeAllBeforeInsert.equals(deltaResponseImageRecord.removeAllBeforeInsert)) &&
        (changedEntities == null ? deltaResponseImageRecord.changedEntities == null : changedEntities.equals(deltaResponseImageRecord.changedEntities)) &&
        (deletedEntities == null ? deltaResponseImageRecord.deletedEntities == null : deletedEntities.equals(deltaResponseImageRecord.deletedEntities));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (storageLastChangeDateTimeUtc == null ? 0: storageLastChangeDateTimeUtc.hashCode());
    result = 31 * result + (storageDeltaStartChangeDateTimeUtc == null ? 0: storageDeltaStartChangeDateTimeUtc.hashCode());
    result = 31 * result + (removeAllBeforeInsert == null ? 0: removeAllBeforeInsert.hashCode());
    result = 31 * result + (changedEntities == null ? 0: changedEntities.hashCode());
    result = 31 * result + (deletedEntities == null ? 0: deletedEntities.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeltaResponseImageRecord {\n");
    
    sb.append("  storageLastChangeDateTimeUtc: ").append(storageLastChangeDateTimeUtc).append("\n");
    sb.append("  storageDeltaStartChangeDateTimeUtc: ").append(storageDeltaStartChangeDateTimeUtc).append("\n");
    sb.append("  removeAllBeforeInsert: ").append(removeAllBeforeInsert).append("\n");
    sb.append("  changedEntities: ").append(changedEntities).append("\n");
    sb.append("  deletedEntities: ").append(deletedEntities).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
