package io.swagger.client.model;

import io.swagger.client.model.ApiErrorResult;
import io.swagger.client.model.CollectTokenResponse;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ApiSafeResultCollectTokenResponse  {
  
  @SerializedName("IsSuccessful")
  private Boolean isSuccessful = null;
  @SerializedName("Error")
  private ApiErrorResult error = null;
  @SerializedName("Result")
  private CollectTokenResponse result = null;

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

  /**
   **/
  @ApiModelProperty(value = "")
  public CollectTokenResponse getResult() {
    return result;
  }
  public void setResult(CollectTokenResponse result) {
    this.result = result;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiSafeResultCollectTokenResponse apiSafeResultCollectTokenResponse = (ApiSafeResultCollectTokenResponse) o;
    return (isSuccessful == null ? apiSafeResultCollectTokenResponse.isSuccessful == null : isSuccessful.equals(apiSafeResultCollectTokenResponse.isSuccessful)) &&
        (error == null ? apiSafeResultCollectTokenResponse.error == null : error.equals(apiSafeResultCollectTokenResponse.error)) &&
        (result == null ? apiSafeResultCollectTokenResponse.result == null : result.equals(apiSafeResultCollectTokenResponse.result));
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
    sb.append("class ApiSafeResultCollectTokenResponse {\n");
    
    sb.append("  isSuccessful: ").append(isSuccessful).append("\n");
    sb.append("  error: ").append(error).append("\n");
    sb.append("  result: ").append(result).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
