# AnnouncementsApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2AnnouncementsByIdDelete**](AnnouncementsApi.md#apiV2AnnouncementsByIdDelete) | **DELETE** /Api/v2/Announcements/{Id} | 
[**apiV2AnnouncementsByIdGet**](AnnouncementsApi.md#apiV2AnnouncementsByIdGet) | **GET** /Api/v2/Announcements/{Id} | Retrieve a single announcement.
[**apiV2AnnouncementsDelete**](AnnouncementsApi.md#apiV2AnnouncementsDelete) | **DELETE** /Api/v2/Announcements | 
[**apiV2AnnouncementsGet**](AnnouncementsApi.md#apiV2AnnouncementsGet) | **GET** /Api/v2/Announcements | Retrieves a list of all announcement entries.
[**apiV2AnnouncementsPost**](AnnouncementsApi.md#apiV2AnnouncementsPost) | **POST** /Api/v2/Announcements | 
[**apiV2AnnouncementsPut**](AnnouncementsApi.md#apiV2AnnouncementsPut) | **PUT** /Api/v2/Announcements | 


<a name="apiV2AnnouncementsByIdDelete"></a>
# **apiV2AnnouncementsByIdDelete**
> apiV2AnnouncementsByIdDelete(id)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiV2AnnouncementsByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiV2AnnouncementsByIdDelete");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **UUID**|  |

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="apiV2AnnouncementsByIdGet"></a>
# **apiV2AnnouncementsByIdGet**
> AnnouncementRecord apiV2AnnouncementsByIdGet(id)

Retrieve a single announcement.

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    AnnouncementRecord result = apiInstance.apiV2AnnouncementsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiV2AnnouncementsByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **UUID**| id of the requested entity |

### Return type

[**AnnouncementRecord**](AnnouncementRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2AnnouncementsDelete"></a>
# **apiV2AnnouncementsDelete**
> apiV2AnnouncementsDelete()



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
try {
    apiInstance.apiV2AnnouncementsDelete();
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiV2AnnouncementsDelete");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="apiV2AnnouncementsGet"></a>
# **apiV2AnnouncementsGet**
> List&lt;AnnouncementRecord&gt; apiV2AnnouncementsGet()

Retrieves a list of all announcement entries.

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
try {
    List<AnnouncementRecord> result = apiInstance.apiV2AnnouncementsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiV2AnnouncementsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;AnnouncementRecord&gt;**](AnnouncementRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2AnnouncementsPost"></a>
# **apiV2AnnouncementsPost**
> apiV2AnnouncementsPost(record)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
AnnouncementRecord record = new AnnouncementRecord(); // AnnouncementRecord | 
try {
    apiInstance.apiV2AnnouncementsPost(record);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiV2AnnouncementsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **record** | [**AnnouncementRecord**](AnnouncementRecord.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: Not defined

<a name="apiV2AnnouncementsPut"></a>
# **apiV2AnnouncementsPut**
> apiV2AnnouncementsPut(record)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
AnnouncementRecord record = new AnnouncementRecord(); // AnnouncementRecord | 
try {
    apiInstance.apiV2AnnouncementsPut(record);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiV2AnnouncementsPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **record** | [**AnnouncementRecord**](AnnouncementRecord.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: Not defined

