package io.swagger.client.model;


import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class LinkFragment  {
  
  public enum FragmentTypeEnum {
     WebExternal,  MapExternal,  MapEntry,  DealerDetail,  EventConferenceRoom, 
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
   * * For FragmentType `DealerDetail`: The `Id` of the dealer record the link is referencing to.  * For FragmentType `MapEntry`: The `Id` of the map entry record the link is referencing to.  * For FragmentType `EventConferenceRoom`: The `Id` of the event conference room record the link is referencing to.  * For FragmentType `MapExternal`: An stringified json object.    * Acceptable properties and their expected value (type):      * `name` - name of target POI (*string*)      * `street` - street name (*string*)      * `house` - house humber (*string*)      * `zip` - zip code of city (*string*)      * `city` - city (*string*)      * `country` - country (*string*)      * `country-a3` - ISO 3166-1 alpha-3 code for country [http://unstats.un.org/unsd/methods/m49/m49alpha.htm] (*string*)      * `lat` - latitude (*decimal*)      * `lon` - longitude (*decimal*)    * Example:      * `{ name: \"Estrel Hotel Berlin\", house: \"225\", street: \"Sonnenallee\", zip: \"12057\", city: \"Berlin\", country: \"Germany\", lat: 52.473336, lon: 13.458729 }`
   **/
  @ApiModelProperty(required = true, value = "* For FragmentType `DealerDetail`: The `Id` of the dealer record the link is referencing to.  * For FragmentType `MapEntry`: The `Id` of the map entry record the link is referencing to.  * For FragmentType `EventConferenceRoom`: The `Id` of the event conference room record the link is referencing to.  * For FragmentType `MapExternal`: An stringified json object.    * Acceptable properties and their expected value (type):      * `name` - name of target POI (*string*)      * `street` - street name (*string*)      * `house` - house humber (*string*)      * `zip` - zip code of city (*string*)      * `city` - city (*string*)      * `country` - country (*string*)      * `country-a3` - ISO 3166-1 alpha-3 code for country [http://unstats.un.org/unsd/methods/m49/m49alpha.htm] (*string*)      * `lat` - latitude (*decimal*)      * `lon` - longitude (*decimal*)    * Example:      * `{ name: \"Estrel Hotel Berlin\", house: \"225\", street: \"Sonnenallee\", zip: \"12057\", city: \"Berlin\", country: \"Germany\", lat: 52.473336, lon: 13.458729 }`")
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
