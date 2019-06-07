package io.swagger.client.model;

import java.util.Date;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class AuthenticationResponse  {
  
  @SerializedName("Uid")
  private String uid = null;
  @SerializedName("Username")
  private String username = null;
  @SerializedName("Token")
  private String token = null;
  @SerializedName("TokenValidUntil")
  private Date tokenValidUntil = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getUid() {
    return uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
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
  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getTokenValidUntil() {
    return tokenValidUntil;
  }
  public void setTokenValidUntil(Date tokenValidUntil) {
    this.tokenValidUntil = tokenValidUntil;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthenticationResponse authenticationResponse = (AuthenticationResponse) o;
    return (uid == null ? authenticationResponse.uid == null : uid.equals(authenticationResponse.uid)) &&
        (username == null ? authenticationResponse.username == null : username.equals(authenticationResponse.username)) &&
        (token == null ? authenticationResponse.token == null : token.equals(authenticationResponse.token)) &&
        (tokenValidUntil == null ? authenticationResponse.tokenValidUntil == null : tokenValidUntil.equals(authenticationResponse.tokenValidUntil));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (uid == null ? 0: uid.hashCode());
    result = 31 * result + (username == null ? 0: username.hashCode());
    result = 31 * result + (token == null ? 0: token.hashCode());
    result = 31 * result + (tokenValidUntil == null ? 0: tokenValidUntil.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthenticationResponse {\n");
    
    sb.append("  uid: ").append(uid).append("\n");
    sb.append("  username: ").append(username).append("\n");
    sb.append("  token: ").append(token).append("\n");
    sb.append("  tokenValidUntil: ").append(tokenValidUntil).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
