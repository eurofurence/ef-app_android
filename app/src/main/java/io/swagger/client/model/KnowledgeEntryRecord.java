package io.swagger.client.model;

import io.swagger.client.model.LinkFragment;
import java.util.*;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.*;
import com.google.gson.annotations.SerializedName;


@ApiModel(description = "")
public class KnowledgeEntryRecord  {
  
  @SerializedName("Id")
  private UUID id = null;
  @SerializedName("LastChangeDateTimeUtc")
  private Date lastChangeDateTimeUtc = null;
  @SerializedName("KnowledgeGroupId")
  private UUID knowledgeGroupId = null;
  @SerializedName("Title")
  private String title = null;
  @SerializedName("Text")
  private String text = null;
  @SerializedName("Order")
  private Integer order = null;
  @SerializedName("Links")
  private List<LinkFragment> links = null;

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
  public Date getLastChangeDateTimeUtc() {
    return lastChangeDateTimeUtc;
  }
  public void setLastChangeDateTimeUtc(Date lastChangeDateTimeUtc) {
    this.lastChangeDateTimeUtc = lastChangeDateTimeUtc;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public UUID getKnowledgeGroupId() {
    return knowledgeGroupId;
  }
  public void setKnowledgeGroupId(UUID knowledgeGroupId) {
    this.knowledgeGroupId = knowledgeGroupId;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   **/
  @ApiModelProperty(required = true, value = "")
  public String getText() {
    return text;
  }
  public void setText(String text) {
    this.text = text;
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
  public List<LinkFragment> getLinks() {
    return links;
  }
  public void setLinks(List<LinkFragment> links) {
    this.links = links;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KnowledgeEntryRecord knowledgeEntryRecord = (KnowledgeEntryRecord) o;
    return (id == null ? knowledgeEntryRecord.id == null : id.equals(knowledgeEntryRecord.id)) &&
        (lastChangeDateTimeUtc == null ? knowledgeEntryRecord.lastChangeDateTimeUtc == null : lastChangeDateTimeUtc.equals(knowledgeEntryRecord.lastChangeDateTimeUtc)) &&
        (knowledgeGroupId == null ? knowledgeEntryRecord.knowledgeGroupId == null : knowledgeGroupId.equals(knowledgeEntryRecord.knowledgeGroupId)) &&
        (title == null ? knowledgeEntryRecord.title == null : title.equals(knowledgeEntryRecord.title)) &&
        (text == null ? knowledgeEntryRecord.text == null : text.equals(knowledgeEntryRecord.text)) &&
        (order == null ? knowledgeEntryRecord.order == null : order.equals(knowledgeEntryRecord.order)) &&
        (links == null ? knowledgeEntryRecord.links == null : links.equals(knowledgeEntryRecord.links));
  }

  @Override 
  public int hashCode() {
    int result = 17;
    result = 31 * result + (id == null ? 0: id.hashCode());
    result = 31 * result + (lastChangeDateTimeUtc == null ? 0: lastChangeDateTimeUtc.hashCode());
    result = 31 * result + (knowledgeGroupId == null ? 0: knowledgeGroupId.hashCode());
    result = 31 * result + (title == null ? 0: title.hashCode());
    result = 31 * result + (text == null ? 0: text.hashCode());
    result = 31 * result + (order == null ? 0: order.hashCode());
    result = 31 * result + (links == null ? 0: links.hashCode());
    return result;
  }

  @Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class KnowledgeEntryRecord {\n");
    
    sb.append("  id: ").append(id).append("\n");
    sb.append("  lastChangeDateTimeUtc: ").append(lastChangeDateTimeUtc).append("\n");
    sb.append("  knowledgeGroupId: ").append(knowledgeGroupId).append("\n");
    sb.append("  title: ").append(title).append("\n");
    sb.append("  text: ").append(text).append("\n");
    sb.append("  order: ").append(order).append("\n");
    sb.append("  links: ").append(links).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}
