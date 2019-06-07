package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class RegSysAuthenticationRequest  {
  
  @SerializedName("RegNo")
  private Integer regNo = null;
  @SerializedName("Username")
  private String username = null;
  @SerializedName("Password")
  private String password = null;
  @SerializedName("AccessToken")
  private String accessToken = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public Integer getRegNo() {
    return regNo;
  }
  public void setRegNo(Integer regNo) {
    this.regNo = regNo;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getAccessToken() {
    return accessToken;
  }
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegSysAuthenticationRequest regSysAuthenticationRequest = (RegSysAuthenticationRequest) o;
    return (regNo == null ? regSysAuthenticationRequest.regNo == null : regNo.equals(regSysAuthenticationRequest.regNo)) &&
        (username == null ? regSysAuthenticationRequest.username == null : username.equals(regSysAuthenticationRequest.username)) &&
        (password == null ? regSysAuthenticationRequest.password == null : password.equals(regSysAuthenticationRequest.password)) &&
        (accessToken == null ? regSysAuthenticationRequest.accessToken == null : accessToken.equals(regSysAuthenticationRequest.accessToken));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (regNo == null ? 0: regNo.hashCode());
    result = 31 * result + (username == null ? 0: username.hashCode());
    result = 31 * result + (password == null ? 0: password.hashCode());
    result = 31 * result + (accessToken == null ? 0: accessToken.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegSysAuthenticationRequest {\n");
    
    sb.append("  regNo: ").append(regNo).append("\n");
    sb.append("  username: ").append(username).append("\n");
    sb.append("  password: ").append(password).append("\n");
    sb.append("  accessToken: ").append(accessToken).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
