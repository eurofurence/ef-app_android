# KnowledgeEntriesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2KnowledgeEntriesByIdDelete**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesByIdDelete) | **DELETE** /Api/v2/KnowledgeEntries/{Id} | Delete a knowledge entry.
[**apiV2KnowledgeEntriesByIdGet**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesByIdGet) | **GET** /Api/v2/KnowledgeEntries/{Id} | Retrieve a single knowledge entry.
[**apiV2KnowledgeEntriesByIdPut**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesByIdPut) | **PUT** /Api/v2/KnowledgeEntries/{Id} | Update an existing knowledge entry.
[**apiV2KnowledgeEntriesGet**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesGet) | **GET** /Api/v2/KnowledgeEntries | Retrieves a list of all knowledge entries.
[**apiV2KnowledgeEntriesPost**](KnowledgeEntriesApi.md#apiV2KnowledgeEntriesPost) | **POST** /Api/v2/KnowledgeEntries | Create a new knowledge entry.


<a name="apiV2KnowledgeEntriesByIdDelete"></a>
# **apiV2KnowledgeEntriesByIdDelete**
> apiV2KnowledgeEntriesByIdDelete(id)

Delete a knowledge entry.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiV2KnowledgeEntriesByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiV2KnowledgeEntriesByIdDelete");
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

<a name="apiV2KnowledgeEntriesByIdPut"></a>
# **apiV2KnowledgeEntriesByIdPut**
> apiV2KnowledgeEntriesByIdPut(id, record)

Update an existing knowledge entry.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
UUID id = new UUID(); // UUID | 
KnowledgeEntryRecord record = new KnowledgeEntryRecord(); // KnowledgeEntryRecord | 
try {
    apiInstance.apiV2KnowledgeEntriesByIdPut(id, record);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiV2KnowledgeEntriesByIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **record** | [**KnowledgeEntryRecord**](KnowledgeEntryRecord.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
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

<a name="apiV2KnowledgeEntriesPost"></a>
# **apiV2KnowledgeEntriesPost**
> UUID apiV2KnowledgeEntriesPost(record)

Create a new knowledge entry.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
KnowledgeEntryRecord record = new KnowledgeEntryRecord(); // KnowledgeEntryRecord | 
try {
    UUID result = apiInstance.apiV2KnowledgeEntriesPost(record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiV2KnowledgeEntriesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **record** | [**KnowledgeEntryRecord**](KnowledgeEntryRecord.md)|  | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

