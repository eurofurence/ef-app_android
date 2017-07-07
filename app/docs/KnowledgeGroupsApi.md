# KnowledgeGroupsApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2KnowledgeGroupsByIdGet**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsByIdGet) | **GET** /Api/v2/KnowledgeGroups/{Id} | Retrieve a single knowledge group.
[**apiV2KnowledgeGroupsGet**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsGet) | **GET** /Api/v2/KnowledgeGroups | Retrieves a list of all knowledge groups.


<a name="apiV2KnowledgeGroupsByIdGet"></a>
# **apiV2KnowledgeGroupsByIdGet**
> KnowledgeGroupRecord apiV2KnowledgeGroupsByIdGet(id)

Retrieve a single knowledge group.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    KnowledgeGroupRecord result = apiInstance.apiV2KnowledgeGroupsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiV2KnowledgeGroupsByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **UUID**| id of the requested entity |

### Return type

[**KnowledgeGroupRecord**](KnowledgeGroupRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2KnowledgeGroupsGet"></a>
# **apiV2KnowledgeGroupsGet**
> List&lt;KnowledgeGroupRecord&gt; apiV2KnowledgeGroupsGet()

Retrieves a list of all knowledge groups.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
try {
    List<KnowledgeGroupRecord> result = apiInstance.apiV2KnowledgeGroupsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiV2KnowledgeGroupsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;KnowledgeGroupRecord&gt;**](KnowledgeGroupRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

