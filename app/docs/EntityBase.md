
# EntityBase

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | [**UUID**](UUID.md) | Universally Unique Identifier (16bytes / 36char string), e.g 550e8400-e29b-11d4-a716-446655440000 | 
**lastChangeDateTimeUtc** | [**Date**](Date.md) | Date &amp; Time (UTC) in **ISO 8601** format of when the entity was last changed. (Will also be updated upon deletion) | 
**isDeleted** | [**BigDecimal**](BigDecimal.md) | Numeric flag that, if set to \&quot;1\&quot;, indicates that the record has been deleted sine the delta reference specified, and should be removed from the local data store after retrieval. | 



