package io.swagger.client.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EndpointEntity  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("TableName")
  private String tableName = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Count")
  private BigDecimal count = null;

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
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  /**
   * Number of records (including deleted ones) in the table.
   **/
  @ApiModelProperty(value = "Number of records (including deleted ones) in the table.")
  public BigDecimal getCount() {
    return count;
  }
  public void setCount(BigDecimal count) {
    this.count = count;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EndpointEntity endpointEntity = (EndpointEntity) o;
    return (id == null ? endpointEntity.id == null : id.equals(endpointEntity.id)) &&
        (name == null ? endpointEntity.name == null : name.equals(endpointEntity.name)) &&
        (tableName == null ? endpointEntity.tableName == null : tableName.equals(endpointEntity.tableName)) &&
        (lastChangeDateTimeUtc == null ? endpointEntity.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(endpointEntity.lastChangeDateTimeUtc)) &&
        (count == null ? endpointEntity.count == null : count.equals(endpointEntity.count));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (tableName == null ? 0: tableName.hashCode());
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (count == null ? 0: count.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class EndpointEntity {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  tableName: ").append(tableName).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  count: ").append(count).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
