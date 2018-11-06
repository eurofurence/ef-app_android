# KnowledgeEntriesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiKnowledgeEntriesByIdDelete**](KnowledgeEntriesApi.md#apiKnowledgeEntriesByIdDelete) | **DELETE** /Api/KnowledgeEntries/{Id} | Delete a knowledge entry.
[**apiKnowledgeEntriesByIdGet**](KnowledgeEntriesApi.md#apiKnowledgeEntriesByIdGet) | **GET** /Api/KnowledgeEntries/{Id} | Retrieve a single knowledge entry.
[**apiKnowledgeEntriesByIdPut**](KnowledgeEntriesApi.md#apiKnowledgeEntriesByIdPut) | **PUT** /Api/KnowledgeEntries/{Id} | Update an existing knowledge entry.
[**apiKnowledgeEntriesGet**](KnowledgeEntriesApi.md#apiKnowledgeEntriesGet) | **GET** /Api/KnowledgeEntries | Retrieves a list of all knowledge entries.
[**apiKnowledgeEntriesPost**](KnowledgeEntriesApi.md#apiKnowledgeEntriesPost) | **POST** /Api/KnowledgeEntries | Create a new knowledge entry.


<a name="apiKnowledgeEntriesByIdDelete"></a>
# **apiKnowledgeEntriesByIdDelete**
> apiKnowledgeEntriesByIdDelete(id)

Delete a knowledge entry.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiKnowledgeEntriesByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiKnowledgeEntriesByIdDelete");
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

<a name="apiKnowledgeEntriesByIdGet"></a>
# **apiKnowledgeEntriesByIdGet**
> KnowledgeEntryRecord apiKnowledgeEntriesByIdGet(id)

Retrieve a single knowledge entry.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    KnowledgeEntryRecord result = apiInstance.apiKnowledgeEntriesByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiKnowledgeEntriesByIdGet");
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

<a name="apiKnowledgeEntriesByIdPut"></a>
# **apiKnowledgeEntriesByIdPut**
> apiKnowledgeEntriesByIdPut(id, record)

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
    apiInstance.apiKnowledgeEntriesByIdPut(id, record);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiKnowledgeEntriesByIdPut");
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

<a name="apiKnowledgeEntriesGet"></a>
# **apiKnowledgeEntriesGet**
> List&lt;KnowledgeEntryRecord&gt; apiKnowledgeEntriesGet()

Retrieves a list of all knowledge entries.

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
try {
    List<KnowledgeEntryRecord> result = apiInstance.apiKnowledgeEntriesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiKnowledgeEntriesGet");
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

<a name="apiKnowledgeEntriesPost"></a>
# **apiKnowledgeEntriesPost**
> UUID apiKnowledgeEntriesPost(record)

Create a new knowledge entry.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.KnowledgeEntriesApi;

KnowledgeEntriesApi apiInstance = new KnowledgeEntriesApi();
KnowledgeEntryRecord record = new KnowledgeEntryRecord(); // KnowledgeEntryRecord | 
try {
    UUID result = apiInstance.apiKnowledgeEntriesPost(record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling KnowledgeEntriesApi#apiKnowledgeEntriesPost");
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

