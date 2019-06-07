package io.swagger.client.model;

import io.swagger.client.model.PlatformTagInfo;
import java.util.*;
import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PushNotificationChannelStatistics  {
  
  @SerializedName("SinceLastSeenDateTimeUtc")
  private Date sinceLastSeenDateTimeUtc = null;
  @SerializedName("NumberOfDevices")
  private Integer numberOfDevices = null;
  @SerializedName("NumberOfAuthenticatedDevices")
  private Integer numberOfAuthenticatedDevices = null;
  @SerializedName("NumberOfUniqueUserIds")
  private Integer numberOfUniqueUserIds = null;
  @SerializedName("PlatformStatistics")
  private List<PlatformTagInfo> platformStatistics = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getSinceLastSeenDateTimeUtc() {
    return sinceLastSeenDateTimeUtc;
  }
  public void setSinceLastSeenDateTimeUtc(Date sinceLastSeenDateTimeUtc) {
    this.sinceLastSeenDateTimeUtc = sinceLastSeenDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getNumberOfDevices() {
    return numberOfDevices;
  }
  public void setNumberOfDevices(Integer numberOfDevices) {
    this.numberOfDevices = numberOfDevices;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getNumberOfAuthenticatedDevices() {
    return numberOfAuthenticatedDevices;
  }
  public void setNumberOfAuthenticatedDevices(Integer numberOfAuthenticatedDevices) {
    this.numberOfAuthenticatedDevices = numberOfAuthenticatedDevices;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getNumberOfUniqueUserIds() {
    return numberOfUniqueUserIds;
  }
  public void setNumberOfUniqueUserIds(Integer numberOfUniqueUserIds) {
    this.numberOfUniqueUserIds = numberOfUniqueUserIds;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<PlatformTagInfo> getPlatformStatistics() {
    return platformStatistics;
  }
  public void setPlatformStatistics(List<PlatformTagInfo> platformStatistics) {
    this.platformStatistics = platformStatistics;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PushNotificationChannelStatistics pushNotificationChannelStatistics = (PushNotificationChannelStatistics) o;
    return (sinceLastSeenDateTimeUtc == null ? pushNotificationChannelStatistics.sinceLastSeenDateTimeUtc == null : sinceLastSeenDateTimeUtc.equals(pushNotificationChannelStatistics.sinceLastSeenDateTimeUtc)) &&
        (numberOfDevices == null ? pushNotificationChannelStatistics.numberOfDevices == null : numberOfDevices.equals(pushNotificationChannelStatistics.numberOfDevices)) &&
        (numberOfAuthenticatedDevices == null ? pushNotificationChannelStatistics.numberOfAuthenticatedDevices == null : numberOfAuthenticatedDevices.equals(pushNotificationChannelStatistics.numberOfAuthenticatedDevices)) &&
        (numberOfUniqueUserIds == null ? pushNotificationChannelStatistics.numberOfUniqueUserIds == null : numberOfUniqueUserIds.equals(pushNotificationChannelStatistics.numberOfUniqueUserIds)) &&
        (platformStatistics == null ? pushNotificationChannelStatistics.platformStatistics == null : platformStatistics.equals(pushNotificationChannelStatistics.platformStatistics));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (sinceLastSeenDateTimeUtc == null ? 0: sinceLastSeenDateTimeUtc.hashCode());
    result = 31 * result + (numberOfDevices == null ? 0: numberOfDevices.hashCode());
    result = 31 * result + (numberOfAuthenticatedDevices == null ? 0: numberOfAuthenticatedDevices.hashCode());
    result = 31 * result + (numberOfUniqueUserIds == null ? 0: numberOfUniqueUserIds.hashCode());
    result = 31 * result + (platformStatistics == null ? 0: platformStatistics.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PushNotificationChannelStatistics {\n");
    
    sb.append("  sinceLastSeenDateTimeUtc: ").append(sinceLastSeenDateTimeUtc).append("\n");
    sb.append("  numberOfDevices: ").append(numberOfDevices).append("\n");
    sb.append("  numberOfAuthenticatedDevices: ").append(numberOfAuthenticatedDevices).append("\n");
    sb.append("  numberOfUniqueUserIds: ").append(numberOfUniqueUserIds).append("\n");
    sb.append("  platformStatistics: ").append(platformStatistics).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
