package io.swagger.client.model;

import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PlatformTagInfo  {
  
  @SerializedName("Platform")
  private String platform = null;
  @SerializedName("Tags")
  private List<String> tags = null;
  @SerializedName("DeviceCount")
  private Integer deviceCount = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getPlatform() {
    return platform;
  }
  public void setPlatform(String platform) {
    this.platform = platform;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getTags() {
    return tags;
  }
  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getDeviceCount() {
    return deviceCount;
  }
  public void setDeviceCount(Integer deviceCount) {
    this.deviceCount = deviceCount;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlatformTagInfo platformTagInfo = (PlatformTagInfo) o;
    return (platform == null ? platformTagInfo.platform == null : platform.equals(platformTagInfo.platform)) &&
        (tags == null ? platformTagInfo.tags == null : tags.equals(platformTagInfo.tags)) &&
        (deviceCount == null ? platformTagInfo.deviceCount == null : deviceCount.equals(platformTagInfo.deviceCount));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (platform == null ? 0: platform.hashCode());
    result = 31 * result + (tags == null ? 0: tags.hashCode());
    result = 31 * result + (deviceCount == null ? 0: deviceCount.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlatformTagInfo {\n");
    
    sb.append("  platform: ").append(platform).append("\n");
    sb.append("  tags: ").append(tags).append("\n");
    sb.append("  deviceCount: ").append(deviceCount).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
