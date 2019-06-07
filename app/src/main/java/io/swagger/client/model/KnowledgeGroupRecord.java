package io.swagger.client.model;

import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class KnowledgeGroupRecord  {
  
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("Name")
  private String name = null;
  @SerializedName("Description")
  private String description = null;
  @SerializedName("Order")
  private Integer order = null;
  @SerializedName("ShowInHamburgerMenu")
  private Boolean showInHamburgerMenu = null;
  @SerializedName("FontAwesomeIconCharacterUnicodeAddress")
  private String fontAwesomeIconCharacterUnicodeAddress = null;

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
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public Integer getOrder() {
    return order;
  }
  public void setOrder(Integer order) {
    this.order = order;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public Boolean getShowInHamburgerMenu() {
    return showInHamburgerMenu;
  }
  public void setShowInHamburgerMenu(Boolean showInHamburgerMenu) {
    this.showInHamburgerMenu = showInHamburgerMenu;
  }

  /**
   **/
  @ApiModelProperty(value = "")
  public String getFontAwesomeIconCharacterUnicodeAddress() {
    return fontAwesomeIconCharacterUnicodeAddress;
  }
  public void setFontAwesomeIconCharacterUnicodeAddress(String fontAwesomeIconCharacterUnicodeAddress) {
    this.fontAwesomeIconCharacterUnicodeAddress = fontAwesomeIconCharacterUnicodeAddress;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KnowledgeGroupRecord knowledgeGroupRecord = (KnowledgeGroupRecord) o;
    return (lastChangeDateTimeUtc == null ? knowledgeGroupRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(knowledgeGroupRecord.lastChangeDateTimeUtc)) &&
        (id == null ? knowledgeGroupRecord.id == null : id.equals(knowledgeGroupRecord.id)) &&
        (name == null ? knowledgeGroupRecord.name == null : name.equals(knowledgeGroupRecord.name)) &&
        (description == null ? knowledgeGroupRecord.description == null : description.equals(knowledgeGroupRecord.description)) &&
        (order == null ? knowledgeGroupRecord.order == null : order.equals(knowledgeGroupRecord.order)) &&
        (showInHamburgerMenu == null ? knowledgeGroupRecord.showInHamburgerMenu == null : showInHamburgerMenu.equals(knowledgeGroupRecord.showInHamburgerMenu)) &&
        (fontAwesomeIconCharacterUnicodeAddress == null ? knowledgeGroupRecord.fontAwesomeIconCharacterUnicodeAddress == null : fontAwesomeIconCharacterUnicodeAddress.equals(knowledgeGroupRecord.fontAwesomeIconCharacterUnicodeAddress));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (name == null ? 0: name.hashCode());
    result = 31 * result + (description == null ? 0: description.hashCode());
    result = 31 * result + (order == null ? 0: order.hashCode());
    result = 31 * result + (showInHamburgerMenu == null ? 0: showInHamburgerMenu.hashCode());
    result = 31 * result + (fontAwesomeIconCharacterUnicodeAddress == null ? 0: fontAwesomeIconCharacterUnicodeAddress.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class KnowledgeGroupRecord {\n");
    
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  order: ").append(order).append("\n");
    sb.append("  showInHamburgerMenu: ").append(showInHamburgerMenu).append("\n");
    sb.append("  fontAwesomeIconCharacterUnicodeAddress: ").append(fontAwesomeIconCharacterUnicodeAddress).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
