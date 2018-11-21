# MapsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiMapsByIdEntriesByEntryIdDelete**](MapsApi.md#apiMapsByIdEntriesByEntryIdDelete) | **DELETE** /Api/Maps/{Id}/Entries/{EntryId} | Delete a specific map entry for a specific map
[**apiMapsByIdEntriesByEntryIdGet**](MapsApi.md#apiMapsByIdEntriesByEntryIdGet) | **GET** /Api/Maps/{Id}/Entries/{EntryId} | Get all specific map entry for a specific map
[**apiMapsByIdEntriesByEntryIdPut**](MapsApi.md#apiMapsByIdEntriesByEntryIdPut) | **PUT** /Api/Maps/{Id}/Entries/{EntryId} | Create or Update an existing map entry in a specific map
[**apiMapsByIdEntriesDelete**](MapsApi.md#apiMapsByIdEntriesDelete) | **DELETE** /Api/Maps/{Id}/Entries | Delete all map entries for a specific map
[**apiMapsByIdEntriesGet**](MapsApi.md#apiMapsByIdEntriesGet) | **GET** /Api/Maps/{Id}/Entries | Get all map entries for a specific map
[**apiMapsByIdEntriesPost**](MapsApi.md#apiMapsByIdEntriesPost) | **POST** /Api/Maps/{Id}/Entries | Create a new map entry in a specific map
[**apiMapsByIdGet**](MapsApi.md#apiMapsByIdGet) | **GET** /Api/Maps/{Id} | Get a specific map
[**apiMapsGet**](MapsApi.md#apiMapsGet) | **GET** /Api/Maps | Get all maps


<a name="apiMapsByIdEntriesByEntryIdDelete"></a>
# **apiMapsByIdEntriesByEntryIdDelete**
> apiMapsByIdEntriesByEntryIdDelete(id, entryId)

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
    apiInstance.apiMapsByIdEntriesByEntryIdDelete(id, entryId);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdEntriesByEntryIdDelete");
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

<a name="apiMapsByIdEntriesByEntryIdGet"></a>
# **apiMapsByIdEntriesByEntryIdGet**
> MapEntryRecord apiMapsByIdEntriesByEntryIdGet(id, entryId)

Get all specific map entry for a specific map

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
UUID entryId = new UUID(); // UUID | 
try {
    MapEntryRecord result = apiInstance.apiMapsByIdEntriesByEntryIdGet(id, entryId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdEntriesByEntryIdGet");
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

<a name="apiMapsByIdEntriesByEntryIdPut"></a>
# **apiMapsByIdEntriesByEntryIdPut**
> UUID apiMapsByIdEntriesByEntryIdPut(id, entryId, record)

Create or Update an existing map entry in a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**  This both works for updating an existing entry and creating a new entry. The id property of the  model (request body) must match the {EntryId} part of the uri.

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | \"Id\" of the map.
UUID entryId = new UUID(); // UUID | \"Id\" of the entry that gets inserted.
MapEntryRecord record = new MapEntryRecord(); // MapEntryRecord | \"Id\" property must match the {EntryId} part of the uri
try {
    UUID result = apiInstance.apiMapsByIdEntriesByEntryIdPut(id, entryId, record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdEntriesByEntryIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| \&quot;Id\&quot; of the map. |
 **entryId** | [**UUID**](.md)| \&quot;Id\&quot; of the entry that gets inserted. |
 **record** | [**MapEntryRecord**](MapEntryRecord.md)| \&quot;Id\&quot; property must match the {EntryId} part of the uri | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiMapsByIdEntriesDelete"></a>
# **apiMapsByIdEntriesDelete**
> apiMapsByIdEntriesDelete(id)

Delete all map entries for a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiMapsByIdEntriesDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdEntriesDelete");
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

<a name="apiMapsByIdEntriesGet"></a>
# **apiMapsByIdEntriesGet**
> List&lt;MapEntryRecord&gt; apiMapsByIdEntriesGet(id)

Get all map entries for a specific map

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
try {
    List<MapEntryRecord> result = apiInstance.apiMapsByIdEntriesGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdEntriesGet");
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

<a name="apiMapsByIdEntriesPost"></a>
# **apiMapsByIdEntriesPost**
> UUID apiMapsByIdEntriesPost(id, record)

Create a new map entry in a specific map

  * Requires authorization     * Requires any of the following roles: **&#x60;Admin&#x60;**, **&#x60;Developer&#x60;**  If you can generate guids client-side, you can also use the PUT variant for both create and update.

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | \"Id\" of the map
MapEntryRecord record = new MapEntryRecord(); // MapEntryRecord | Do not specify the \"Id\" property. It will be auto-assigned and returned in the response.
try {
    UUID result = apiInstance.apiMapsByIdEntriesPost(id, record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdEntriesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| \&quot;Id\&quot; of the map |
 **record** | [**MapEntryRecord**](MapEntryRecord.md)| Do not specify the \&quot;Id\&quot; property. It will be auto-assigned and returned in the response. | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiMapsByIdGet"></a>
# **apiMapsByIdGet**
> MapRecord apiMapsByIdGet(id)

Get a specific map

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
UUID id = new UUID(); // UUID | 
try {
    MapRecord result = apiInstance.apiMapsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsByIdGet");
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

<a name="apiMapsGet"></a>
# **apiMapsGet**
> List&lt;MapRecord&gt; apiMapsGet()

Get all maps

### Example
```java
// Import classes:
//import io.swagger.client.api.MapsApi;

MapsApi apiInstance = new MapsApi();
try {
    List<MapRecord> result = apiInstance.apiMapsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MapsApi#apiMapsGet");
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

