# KnowledgeEntriesApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2KnowledgeEntriesByIdGet**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesByIdGet) | **GET** /Api/v2/KnowledgeEntries/{Id} | Retrieve a single knowledge entry.
[**apiV2KnowledgeEntriesGet**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesGet) | **GET** /Api/v2/KnowledgeEntries | Retrieves a list of all knowledge entries.


<a name="apiV2KnowledgeEntriesByIdGet"></a>
# **apiV2KnowledgeEntriesByIdGet**
> KnowledgeEntryRecord apiV2KnowledgeEntriesByIdGet(id)

Retrieve a single knowledge entry.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    KnowledgeEntryRecord result = apiInstance.apiV2KnowledgeEntriesByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiV2KnowledgeEntriesByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**KnowledgeEntryRecord**](KnowledgeEntryRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2KnowledgeEntriesGet"></a>
# **apiV2KnowledgeEntriesGet**
> List&lt;KnowledgeEntryRecord&gt; apiV2KnowledgeEntriesGet()

Retrieves a list of all knowledge entries.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
try {
    List<KnowledgeEntryRecord> result = apiInstance.apiV2KnowledgeEntriesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiV2KnowledgeEntriesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;KnowledgeEntryRecord&gt;**](KnowledgeEntryRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

