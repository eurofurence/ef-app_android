# MapsApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2MapsByIdEntriesByEntryIdDelete**](MapsApi.md#apiV2MapsByIdEntriesByEntryIdDelete) | **DELETE** /Api/v2/Maps/{Id}/Entries/{EntryId} | Delete a specific map entry for a specific map
[**apiV2MapsByIdEntriesByEntryIdGet**](MapsApi.md#apiV2MapsByIdEntriesByEntryIdGet) | **GET** /Api/v2/Maps/{Id}/Entries/{EntryId} | Get all specific map entry for a specific map
[**apiV2MapsByIdEntriesByEntryIdPut**](MapsApi.md#apiV2MapsByIdEntriesByEntryIdPut) | **PUT** /Api/v2/Maps/{Id}/Entries/{EntryId} | Create or Update an existing map entry in a specific map
[**apiV2MapsByIdEntriesDelete**](MapsApi.md#apiV2MapsByIdEntriesDelete) | **DELETE** /Api/v2/Maps/{Id}/Entries | Delete all map entries for a specific map
[**apiV2MapsByIdEntriesGet**](MapsApi.md#apiV2MapsByIdEntriesGet) | **GET** /Api/v2/Maps/{Id}/Entries | Get all map entries for a specific map
[**apiV2MapsByIdEntriesPost**](MapsApi.md#apiV2MapsByIdEntriesPost) | **POST** /Api/v2/Maps/{Id}/Entries | Create a new map entry in a specific map
[**apiV2MapsByIdGet**](MapsApi.md#apiV2MapsByIdGet) | **GET** /Api/v2/Maps/{Id} | Get a specific map
[**apiV2MapsGet**](MapsApi.md#apiV2MapsGet) | **GET** /Api/v2/Maps | Get all maps


<a name="apiV2MapsByIdEntriesByEntryIdDelete"></a>
# **apiV2MapsByIdEntriesByEntryIdDelete**
> apiV2MapsByIdEntriesByEntryIdDelete(id, entryId)

Delete a specific map entry for a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
UUID entryId = new UUID(); // UUID | 
try {
    apiInstance.apiV2MapsByIdEntriesByEntryIdDelete(id, entryId);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdEntriesByEntryIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **entryId** | [**UUID**](.md)|  |

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="apiV2MapsByIdEntriesByEntryIdGet"></a>
# **apiV2MapsByIdEntriesByEntryIdGet**
> MapEntryRecord apiV2MapsByIdEntriesByEntryIdGet(id, entryId)

Get all specific map entry for a specific map

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
UUID entryId = new UUID(); // UUID | 
try {
    MapEntryRecord result = apiInstance.apiV2MapsByIdEntriesByEntryIdGet(id, entryId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdEntriesByEntryIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **entryId** | [**UUID**](.md)|  |

### Return type

[**MapEntryRecord**](MapEntryRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2MapsByIdEntriesByEntryIdPut"></a>
# **apiV2MapsByIdEntriesByEntryIdPut**
> UUID apiV2MapsByIdEntriesByEntryIdPut(id, entryId, record)

Create or Update an existing map entry in a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**  This both works for updating an existing entry and creating a new entry. The id property of the              model (request body) must match the {EntryId} part of the uri.

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
UUID entryId = new UUID(); // UUID | 
MapEntryRecord record = new MapEntryRecord(); // MapEntryRecord | \"Id\" property must match the {EntryId} part of the uri
try {
    UUID result = apiInstance.apiV2MapsByIdEntriesByEntryIdPut(id, entryId, record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdEntriesByEntryIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **entryId** | [**UUID**](.md)|  |
 **record** | [**MapEntryRecord**](MapEntryRecord.md)| \&quot;Id\&quot; property must match the {EntryId} part of the uri | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2MapsByIdEntriesDelete"></a>
# **apiV2MapsByIdEntriesDelete**
> apiV2MapsByIdEntriesDelete(id)

Delete all map entries for a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiV2MapsByIdEntriesDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdEntriesDelete");
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
 - **Accept**: Not defined

<a name="apiV2MapsByIdEntriesGet"></a>
# **apiV2MapsByIdEntriesGet**
> List&lt;MapEntryRecord&gt; apiV2MapsByIdEntriesGet(id)

Get all map entries for a specific map

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
try {
    List<MapEntryRecord> result = apiInstance.apiV2MapsByIdEntriesGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdEntriesGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |

### Return type

[**List&lt;MapEntryRecord&gt;**](MapEntryRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2MapsByIdEntriesPost"></a>
# **apiV2MapsByIdEntriesPost**
> UUID apiV2MapsByIdEntriesPost(id, record)

Create a new map entry in a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**  If you can generate guids client-side, you can also use the PUT variant for both create and update.

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
MapEntryRecord record = new MapEntryRecord(); // MapEntryRecord | Do not specify the \"Id\" property. It will be auto-assigned and returned in the response.
try {
    UUID result = apiInstance.apiV2MapsByIdEntriesPost(id, record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdEntriesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **record** | [**MapEntryRecord**](MapEntryRecord.md)| Do not specify the \&quot;Id\&quot; property. It will be auto-assigned and returned in the response. | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2MapsByIdGet"></a>
# **apiV2MapsByIdGet**
> MapRecord apiV2MapsByIdGet(id)

Get a specific map

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
try {
    MapRecord result = apiInstance.apiV2MapsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |

### Return type

[**MapRecord**](MapRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2MapsGet"></a>
# **apiV2MapsGet**
> List&lt;MapRecord&gt; apiV2MapsGet()

Get all maps

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
try {
    List<MapRecord> result = apiInstance.apiV2MapsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiV2MapsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;MapRecord&gt;**](MapRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

