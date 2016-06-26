package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class NamedUrl  {
  
  @SerializedName("Target")
  private String target = null;
  @SerializedName("Text")
  private String text = null;

  /**
   * URL/URI of the resource.
   **/
  @ApiModelProperty(value = "URL/URI of the resource.")
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Text/Description of the resource.
   **/
  @ApiModelProperty(value = "Text/Description of the resource.")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NamedUrl namedUrl = (NamedUrl) o;
    return (target == null ? namedUrl.target == null : target.equals(namedUrl.target)) &&
        (text == null ? namedUrl.text == null : text.equals(namedUrl.text));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (target == null ? 0: target.hashCode());
    result = 31 * result + (text == null ? 0: text.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class NamedUrl {\n");
    
    sb.append("  target: ").append(target).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
