package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PrivateMessageStatus  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("RecipientUid")
  private String recipientUid = null;
  @SerializedName("CreatedDateTimeUtc")
  private Date createdDateTimeUtc = null;
  @SerializedName("ReceivedDateTimeUtc")
  private Date receivedDateTimeUtc = null;
  @SerializedName("ReadDateTimeUtc")
  private Date readDateTimeUtc = null;

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
  public String getRecipientUid() {
    return recipientUid;
  }
  public void setRecipientUid(String recipientUid) {
    this.recipientUid = recipientUid;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getCreatedDateTimeUtc() {
    return createdDateTimeUtc;
  }
  public void setCreatedDateTimeUtc(Date createdDateTimeUtc) {
    this.createdDateTimeUtc = createdDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getReceivedDateTimeUtc() {
    return receivedDateTimeUtc;
  }
  public void setReceivedDateTimeUtc(Date receivedDateTimeUtc) {
    this.receivedDateTimeUtc = receivedDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Date getReadDateTimeUtc() {
    return readDateTimeUtc;
  }
  public void setReadDateTimeUtc(Date readDateTimeUtc) {
    this.readDateTimeUtc = readDateTimeUtc;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrivateMessageStatus privateMessageStatus = (PrivateMessageStatus) o;
    return (id == null ? privateMessageStatus.id == null : id.equals(privateMessageStatus.id)) &&
        (recipientUid == null ? privateMessageStatus.recipientUid == null : recipientUid.equals(privateMessageStatus.recipientUid)) &&
        (createdDateTimeUtc == null ? privateMessageStatus.createdDateTimeUtc == null : createdDateTimeUtc.equals(privateMessageStatus.createdDateTimeUtc)) &&
        (receivedDateTimeUtc == null ? privateMessageStatus.receivedDateTimeUtc == null : receivedDateTimeUtc.equals(privateMessageStatus.receivedDateTimeUtc)) &&
        (readDateTimeUtc == null ? privateMessageStatus.readDateTimeUtc == null : readDateTimeUtc.equals(privateMessageStatus.readDateTimeUtc));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (recipientUid == null ? 0: recipientUid.hashCode());
    result = 31 * result + (createdDateTimeUtc == null ? 0: createdDateTimeUtc.hashCode());
    result = 31 * result + (receivedDateTimeUtc == null ? 0: receivedDateTimeUtc.hashCode());
    result = 31 * result + (readDateTimeUtc == null ? 0: readDateTimeUtc.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrivateMessageStatus {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  recipientUid: ").append(recipientUid).append("\n");
    sb.append("  createdDateTimeUtc: ").append(createdDateTimeUtc).append("\n");
    sb.append("  receivedDateTimeUtc: ").append(receivedDateTimeUtc).append("\n");
    sb.append("  readDateTimeUtc: ").append(readDateTimeUtc).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
