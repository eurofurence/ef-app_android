package io.swagger.client.model;

import java.util.*;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PostFcmDeviceRegistrationRequest  {
  
  @SerializedName("DeviceId")
  private String deviceId = null;
  @SerializedName("Topics")
  private List<String> topics = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getDeviceId() {
    return deviceId;
  }
  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public List<String> getTopics() {
    return topics;
  }
  public void setTopics(List<String> topics) {
    this.topics = topics;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFcmDeviceRegistrationRequest postFcmDeviceRegistrationRequest = (PostFcmDeviceRegistrationRequest) o;
    return (deviceId == null ? postFcmDeviceRegistrationRequest.deviceId == null : deviceId.equals(postFcmDeviceRegistrationRequest.deviceId)) &&
        (topics == null ? postFcmDeviceRegistrationRequest.topics == null : topics.equals(postFcmDeviceRegistrationRequest.topics));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (deviceId == null ? 0: deviceId.hashCode());
    result = 31 * result + (topics == null ? 0: topics.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostFcmDeviceRegistrationRequest {\n");
    
    sb.append("  deviceId: ").append(deviceId).append("\n");
    sb.append("  topics: ").append(topics).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
