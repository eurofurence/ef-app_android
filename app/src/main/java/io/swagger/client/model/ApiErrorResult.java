package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ApiErrorResult  {
  
  @SerializedName("Code")
  private String code = null;
  @SerializedName("Message")
  private String message = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getCode() {
    return code;
  }
  public void setCode(String code) {
    this.code = code;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApiErrorResult apiErrorResult = (ApiErrorResult) o;
    return (code == null ? apiErrorResult.code == null : code.equals(apiErrorResult.code)) &&
        (message == null ? apiErrorResult.message == null : message.equals(apiErrorResult.message));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (code == null ? 0: code.hashCode());
    result = 31 * result + (message == null ? 0: message.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApiErrorResult {\n");
    
    sb.append("  code: ").append(code).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
