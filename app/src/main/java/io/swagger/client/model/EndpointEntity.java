package io.swagger.client.model;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class EndpointEntity  {
  
  @SerializedName("Id")
  private String id = null;
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
  public String getId() {
    return id;
  }
  public void setId(String id) {
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
