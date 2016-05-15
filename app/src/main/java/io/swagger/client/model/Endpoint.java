package io.swagger.client.model;

import io.swagger.client.model.EndpointConfiguration;
import io.swagger.client.model.EndpointEntity;
import java.util.*;
import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class Endpoint  {
  
  @SerializedName("CurrentDateTimeUtc")
  private Date currentDateTimeUtc = null;
  @SerializedName("Configuration")
  private List<EndpointConfiguration> configuration = null;
  @SerializedName("Entities")
  private List<EndpointEntity> entities = null;

  
  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCurrentDateTimeUtc() {
    return currentDateTimeUtc;
  }
  public void setCurrentDateTimeUtc(Date currentDateTimeUtc) {
    this.currentDateTimeUtc = currentDateTimeUtc;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public List<EndpointConfiguration> getConfiguration() {
    return configuration;
  }
  public void setConfiguration(List<EndpointConfiguration> configuration) {
    this.configuration = configuration;
  }

  
  /**
   **/
  @ApiModelProperty(value = "")
  public List<EndpointEntity> getEntities() {
    return entities;
  }
  public void setEntities(List<EndpointEntity> entities) {
    this.entities = entities;
  }

  

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Endpoint {\n");
    
    sb.append("  currentDateTimeUtc: ").append(currentDateTimeUtc).append("\n");
    sb.append("  configuration: ").append(configuration).append("\n");
    sb.append("  entities: ").append(entities).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
