# KnowledgeGroupsApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiKnowledgeGroupsByIdDelete**](KnowledgeGroupsApi.md#apiKnowledgeGroupsByIdDelete) | **DELETE** /Api/KnowledgeGroups/{Id} | Delete a knowledge group.
[**apiKnowledgeGroupsByIdGet**](KnowledgeGroupsApi.md#apiKnowledgeGroupsByIdGet) | **GET** /Api/KnowledgeGroups/{Id} | Retrieve a single knowledge group.
[**apiKnowledgeGroupsByIdPut**](KnowledgeGroupsApi.md#apiKnowledgeGroupsByIdPut) | **PUT** /Api/KnowledgeGroups/{Id} | Update an existing knowledge group.
[**apiKnowledgeGroupsGet**](KnowledgeGroupsApi.md#apiKnowledgeGroupsGet) | **GET** /Api/KnowledgeGroups | Retrieves a list of all knowledge groups.
[**apiKnowledgeGroupsPost**](KnowledgeGroupsApi.md#apiKnowledgeGroupsPost) | **POST** /Api/KnowledgeGroups | Create a new knowledge group.


<a name="apiKnowledgeGroupsByIdDelete"></a>
# **apiKnowledgeGroupsByIdDelete**
> apiKnowledgeGroupsByIdDelete(id)

Delete a knowledge group.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiKnowledgeGroupsByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiKnowledgeGroupsByIdDelete");
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

<a name="apiKnowledgeGroupsByIdGet"></a>
# **apiKnowledgeGroupsByIdGet**
> KnowledgeGroupRecord apiKnowledgeGroupsByIdGet(id)

Retrieve a single knowledge group.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    KnowledgeGroupRecord result = apiInstance.apiKnowledgeGroupsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiKnowledgeGroupsByIdGet");
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

<a name="apiKnowledgeGroupsByIdPut"></a>
# **apiKnowledgeGroupsByIdPut**
> apiKnowledgeGroupsByIdPut(id, record)

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
    apiInstance.apiKnowledgeGroupsByIdPut(id, record);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiKnowledgeGroupsByIdPut");
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

<a name="apiKnowledgeGroupsGet"></a>
# **apiKnowledgeGroupsGet**
> List&lt;KnowledgeGroupRecord&gt; apiKnowledgeGroupsGet()

Retrieves a list of all knowledge groups.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
try {
    List<KnowledgeGroupRecord> result = apiInstance.apiKnowledgeGroupsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiKnowledgeGroupsGet");
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

<a name="apiKnowledgeGroupsPost"></a>
# **apiKnowledgeGroupsPost**
> UUID apiKnowledgeGroupsPost(record)

Create a new knowledge group.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeGroupsApi;

KnowledgeGroupsApi apiInstance = new KnowledgeGroupsApi();
KnowledgeGroupRecord record = new KnowledgeGroupRecord(); // KnowledgeGroupRecord | 
try {
    UUID result = apiInstance.apiKnowledgeGroupsPost(record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeGroupsApi#apiKnowledgeGroupsPost");
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

