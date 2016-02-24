package io.swagger.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.EndpointConfiguration;
import io.swagger.client.model.EndpointEntity;
import java.util.*;
import java.util.Date;

import java.io.Serializable;



@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-02-24T14:49:45.074+01:00")
public class Endpoint  implements Serializable {
  
  private Date currentDateTimeUtc = null;
  private List<EndpointConfiguration> configuration = new ArrayList<EndpointConfiguration>();
  private List<EndpointEntity> entities = new ArrayList<EndpointEntity>();

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("CurrentDateTimeUtc")
  public Date getCurrentDateTimeUtc() {
    return currentDateTimeUtc;
  }
  public void setCurrentDateTimeUtc(Date currentDateTimeUtc) {
    this.currentDateTimeUtc = currentDateTimeUtc;
  }

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("Configuration")
  public List<EndpointConfiguration> getConfiguration() {
    return configuration;
  }
  public void setConfiguration(List<EndpointConfiguration> configuration) {
    this.configuration = configuration;
  }

  
  /**
   **/
  
  @ApiModelProperty(value = "")
  @JsonProperty("Entities")
  public List<EndpointEntity> getEntities() {
    return entities;
  }
  public void setEntities(List<EndpointEntity> entities) {
    this.entities = entities;
  }

  

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Endpoint endpoint = (Endpoint) o;
    return Objects.equals(currentDateTimeUtc, endpoint.currentDateTimeUtc) &&
        Objects.equals(configuration, endpoint.configuration) &&
        Objects.equals(entities, endpoint.entities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentDateTimeUtc, configuration, entities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Endpoint {\n");
    
    sb.append("    currentDateTimeUtc: ").append(toIndentedString(currentDateTimeUtc)).append("\n");
    sb.append("    configuration: ").append(toIndentedString(configuration)).append("\n");
    sb.append("    entities: ").append(toIndentedString(entities)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

