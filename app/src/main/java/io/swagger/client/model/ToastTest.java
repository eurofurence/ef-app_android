package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class ToastTest  {
  
  @SerializedName("Topic")
  private String topic = null;
  @SerializedName("Message")
  private String message = null;

  /**
   **/
  @ApiModelProperty(value = "")
  public String getTopic() {
    return topic;
  }
  public void setTopic(String topic) {
    this.topic = topic;
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
    ToastTest toastTest = (ToastTest) o;
    return (topic == null ? toastTest.topic == null : topic.equals(toastTest.topic)) &&
        (message == null ? toastTest.message == null : message.equals(toastTest.message));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (topic == null ? 0: topic.hashCode());
    result = 31 * result + (message == null ? 0: message.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class ToastTest {\n");
    
    sb.append("  topic: ").append(topic).append("\n");
    sb.append("  message: ").append(message).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
