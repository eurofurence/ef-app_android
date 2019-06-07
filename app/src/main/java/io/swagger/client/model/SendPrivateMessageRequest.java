package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class SendPrivateMessageRequest  {
  
  @SerializedName("RecipientUid")
  private String recipientUid = null;
  @SerializedName("AuthorName")
  private String authorName = null;
  @SerializedName("ToastTitle")
  private String toastTitle = null;
  @SerializedName("ToastMessage")
  private String toastMessage = null;
  @SerializedName("Subject")
  private String subject = null;
  @SerializedName("Message")
  private String message = null;

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
  public String getAuthorName() {
    return authorName;
  }
  public void setAuthorName(String authorName) {
    this.authorName = authorName;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getToastTitle() {
    return toastTitle;
  }
  public void setToastTitle(String toastTitle) {
    this.toastTitle = toastTitle;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getToastMessage() {
    return toastMessage;
  }
  public void setToastMessage(String toastMessage) {
    this.toastMessage = toastMessage;
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
    SendPrivateMessageRequest sendPrivateMessageRequest = (SendPrivateMessageRequest) o;
    return (recipientUid == null ? sendPrivateMessageRequest.recipientUid == null : recipientUid.equals(sendPrivateMessageRequest.recipientUid)) &&
        (authorName == null ? sendPrivateMessageRequest.authorName == null : authorName.equals(sendPrivateMessageRequest.authorName)) &&
        (toastTitle == null ? sendPrivateMessageRequest.toastTitle == null : toastTitle.equals(sendPrivateMessageRequest.toastTitle)) &&
        (toastMessage == null ? sendPrivateMessageRequest.toastMessage == null : toastMessage.equals(sendPrivateMessageRequest.toastMessage)) &&
        (subject == null ? sendPrivateMessageRequest.subject == null : subject.equals(sendPrivateMessageRequest.subject)) &&
        (message == null ? sendPrivateMessageRequest.message == null : message.equals(sendPrivateMessageRequest.message));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (recipientUid == null ? 0: recipientUid.hashCode());
    result = 31 * result + (authorName == null ? 0: authorName.hashCode());
    result = 31 * result + (toastTitle == null ? 0: toastTitle.hashCode());
    result = 31 * result + (toastMessage == null ? 0: toastMessage.hashCode());
    result = 31 * result + (subject == null ? 0: subject.hashCode());
    result = 31 * result + (message == null ? 0: message.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendPrivateMessageRequest {\n");
    
    sb.append("  recipientUid: ").append(recipientUid).append("\n");
    sb.append("  authorName: ").append(authorName).append("\n");
    sb.append("  toastTitle: ").append(toastTitle).append("\n");
    sb.append("  toastMessage: ").append(toastMessage).append("\n");
    sb.append("  subject: ").append(subject).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
