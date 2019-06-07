package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class PrivateMessageRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("RecipientUid")
  private String recipientUid = null;
  @SerializedName("SenderUid")
  private String senderUid = null;
  @SerializedName("CreatedDateTimeUtc")
  private Date createdDateTimeUtc = null;
  @SerializedName("ReceivedDateTimeUtc")
  private Date receivedDateTimeUtc = null;
  @SerializedName("ReadDateTimeUtc")
  private Date readDateTimeUtc = null;
  @SerializedName("AuthorName")
  private String authorName = null;
  @SerializedName("Subject")
  private String subject = null;
  @SerializedName("Message")
  private String message = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getId() {
    return id;
  }
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getRecipientUid() {
    return recipientUid;
  }
  public void setRecipientUid(String recipientUid) {
    this.recipientUid = recipientUid;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getSenderUid() {
    return senderUid;
  }
  public void setSenderUid(String senderUid) {
    this.senderUid = senderUid;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
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

  /**
   **/
  @ApiModelProperty(value = "")
  public String getAuthorName() {
    return authorName;
  }
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getSubject() {
    return subject;
  }
  public void setSubject(String subject) {
    this.subject = subject;
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
    PrivateMessageRecord privateMessageRecord = (PrivateMessageRecord) o;
    return (lastChangeDateTimeUtc == null ? privateMessageRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(privateMessageRecord.lastChangeDateTimeUtc)) &&
        (id == null ? privateMessageRecord.id == null : id.equals(privateMessageRecord.id)) &&
        (recipientUid == null ? privateMessageRecord.recipientUid == null : recipientUid.equals(privateMessageRecord.recipientUid)) &&
        (senderUid == null ? privateMessageRecord.senderUid == null : senderUid.equals(privateMessageRecord.senderUid)) &&
        (createdDateTimeUtc == null ? privateMessageRecord.createdDateTimeUtc == null : createdDateTimeUtc.equals(privateMessageRecord.createdDateTimeUtc)) &&
        (receivedDateTimeUtc == null ? privateMessageRecord.receivedDateTimeUtc == null : receivedDateTimeUtc.equals(privateMessageRecord.receivedDateTimeUtc)) &&
        (readDateTimeUtc == null ? privateMessageRecord.readDateTimeUtc == null : readDateTimeUtc.equals(privateMessageRecord.readDateTimeUtc)) &&
        (authorName == null ? privateMessageRecord.authorName == null : authorName.equals(privateMessageRecord.authorName)) &&
        (subject == null ? privateMessageRecord.subject == null : subject.equals(privateMessageRecord.subject)) &&
        (message == null ? privateMessageRecord.message == null : message.equals(privateMessageRecord.message));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (recipientUid == null ? 0: recipientUid.hashCode());
    result = 31 * result + (senderUid == null ? 0: senderUid.hashCode());
    result = 31 * result + (createdDateTimeUtc == null ? 0: createdDateTimeUtc.hashCode());
    result = 31 * result + (receivedDateTimeUtc == null ? 0: receivedDateTimeUtc.hashCode());
    result = 31 * result + (readDateTimeUtc == null ? 0: readDateTimeUtc.hashCode());
    result = 31 * result + (authorName == null ? 0: authorName.hashCode());
    result = 31 * result + (subject == null ? 0: subject.hashCode());
    result = 31 * result + (message == null ? 0: message.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class PrivateMessageRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  recipientUid: ").append(recipientUid).append("\n");
    sb.append("  senderUid: ").append(senderUid).append("\n");
    sb.append("  createdDateTimeUtc: ").append(createdDateTimeUtc).append("\n");
    sb.append("  receivedDateTimeUtc: ").append(receivedDateTimeUtc).append("\n");
    sb.append("  readDateTimeUtc: ").append(readDateTimeUtc).append("\n");
    sb.append("  authorName: ").append(authorName).append("\n");
    sb.append("  subject: ").append(subject).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
