package io.swagger.client.model;

import io.swagger.client.model.ApiErrorResult;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ApiSafeResult  {
  
  @SerializedName("IsSuccessful")
  private Boolean isSuccessful = null;
  @SerializedName("Error")
  private ApiErrorResult error = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getIsSuccessful() {
    return isSuccessful;
  }
  public void setIsSuccessful(Boolean isSuccessful) {
    this.isSuccessful = isSuccessful;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public ApiErrorResult getError() {
    return error;
  }
  public void setError(ApiErrorResult error) {
    this.error = error;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiSafeResult apiSafeResult = (ApiSafeResult) o;
    return (isSuccessful == null ? apiSafeResult.isSuccessful == null : isSuccessful.equals(apiSafeResult.isSuccessful)) &&
        (error == null ? apiSafeResult.error == null : error.equals(apiSafeResult.error));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (isSuccessful == null ? 0: isSuccessful.hashCode());
    result = 31 * result + (error == null ? 0: error.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiSafeResult {\n");
    
    sb.append("  isSuccessful: ").append(isSuccessful).append("\n");
    sb.append("  error: ").append(error).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
