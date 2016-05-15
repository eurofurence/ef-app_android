package io.swagger.client.model;

import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EndpointConfiguration  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("ResourceKey")
  private String resourceKey = null;
  @SerializedName("Value")
  private String value = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getResourceKey() {
    return resourceKey;
  }
  public void setResourceKey(String resourceKey) {
    this.resourceKey = resourceKey;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointConfiguration endpointConfiguration = (EndpointConfiguration) o;
    return (id == null ? endpointConfiguration.id == null : id.equals(endpointConfiguration.id)) &&
        (resourceKey == null ? endpointConfiguration.resourceKey == null : resourceKey.equals(endpointConfiguration.resourceKey)) &&
        (value == null ? endpointConfiguration.value == null : value.equals(endpointConfiguration.value));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (resourceKey == null ? 0: resourceKey.hashCode());
    result = 31 * result + (value == null ? 0: value.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointConfiguration {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  resourceKey: ").append(resourceKey).append("\n");
    sb.append("  value: ").append(value).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
