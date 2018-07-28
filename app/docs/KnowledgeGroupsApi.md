# KnowledgeGroupsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2KnowledgeGroupsByIdDelete**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsByIdDelete) | **DELETE** /Api/v2/KnowledgeGroups/{Id} | Delete a knowledge group.
[**apiV2KnowledgeGroupsByIdGet**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsByIdGet) | **GET** /Api/v2/KnowledgeGroups/{Id} | Retrieve a single knowledge group.
[**apiV2KnowledgeGroupsByIdPut**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsByIdPut) | **PUT** /Api/v2/KnowledgeGroups/{Id} | Update an existing knowledge group.
[**apiV2KnowledgeGroupsGet**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsGet) | **GET** /Api/v2/KnowledgeGroups | Retrieves a list of all knowledge groups.
[**apiV2KnowledgeGroupsPost**](KnowledgeGroupsApi.md#apiV2KnowledgeGroupsPost) | **POST** /Api/v2/KnowledgeGroups | Create a new knowledge group.


<a name="apiV2KnowledgeGroupsByIdDelete"></a>
# **apiV2KnowledgeGroupsByIdDelete**
> apiV2KnowledgeGroupsByIdDelete(id)

Delete a knowledge group.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiV2KnowledgeGroupsByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiV2KnowledgeGroupsByIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

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
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**KnowledgeGroupRecord**](KnowledgeGroupRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2KnowledgeGroupsByIdPut"></a>
# **apiV2KnowledgeGroupsByIdPut**
> apiV2KnowledgeGroupsByIdPut(id, record)

Update an existing knowledge group.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
UUID id = new UUID(); // UUID | 
KnowledgeGroupRecord record = new KnowledgeGroupRecord(); // KnowledgeGroupRecord | 
try {
    apiInstance.apiV2KnowledgeGroupsByIdPut(id, record);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiV2KnowledgeGroupsByIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **record** | [**KnowledgeGroupRecord**](KnowledgeGroupRecord.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
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

<a name="apiV2KnowledgeGroupsPost"></a>
# **apiV2KnowledgeGroupsPost**
> UUID apiV2KnowledgeGroupsPost(record)

Create a new knowledge group.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
KnowledgeGroupRecord record = new KnowledgeGroupRecord(); // KnowledgeGroupRecord | 
try {
    UUID result = apiInstance.apiV2KnowledgeGroupsPost(record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiV2KnowledgeGroupsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **record** | [**KnowledgeGroupRecord**](KnowledgeGroupRecord.md)|  | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

