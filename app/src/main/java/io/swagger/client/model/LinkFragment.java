package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class LinkFragment  {
  
  public enum FragmentTypeEnum {
     WebExternal,  MapExternal,  MapInternal,  DealerDetail, 
  };
  @SerializedName("FragmentType")
  private FragmentTypeEnum fragmentType = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Target")
  private String target = null;

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public FragmentTypeEnum getFragmentType() {
    return fragmentType;
  }
  public void setFragmentType(FragmentTypeEnum fragmentType) {
    this.fragmentType = fragmentType;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   * * For FragmentType `DealerDetail`: The `Id` of the dealer record the link is referencing to.
   **/
  @ApiModelProperty(required = true, value = "* For FragmentType `DealerDetail`: The `Id` of the dealer record the link is referencing to.")
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkFragment linkFragment = (LinkFragment) o;
    return (fragmentType == null ? linkFragment.fragmentType == null : fragmentType.equals(linkFragment.fragmentType)) &&
        (name == null ? linkFragment.name == null : name.equals(linkFragment.name)) &&
        (target == null ? linkFragment.target == null : target.equals(linkFragment.target));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (fragmentType == null ? 0: fragmentType.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (target == null ? 0: target.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkFragment {\n");
    
    sb.append("  fragmentType: ").append(fragmentType).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  target: ").append(target).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
