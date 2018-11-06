# AnnouncementsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiAnnouncementsByIdDelete**](AnnouncementsApi.md#apiAnnouncementsByIdDelete) | **DELETE** /Api/Announcements/{Id} | 
[**apiAnnouncementsByIdGet**](AnnouncementsApi.md#apiAnnouncementsByIdGet) | **GET** /Api/Announcements/{Id} | Retrieve a single announcement.
[**apiAnnouncementsDelete**](AnnouncementsApi.md#apiAnnouncementsDelete) | **DELETE** /Api/Announcements | 
[**apiAnnouncementsGet**](AnnouncementsApi.md#apiAnnouncementsGet) | **GET** /Api/Announcements | Retrieves a list of all announcement entries.
[**apiAnnouncementsPost**](AnnouncementsApi.md#apiAnnouncementsPost) | **POST** /Api/Announcements | 
[**apiAnnouncementsPut**](AnnouncementsApi.md#apiAnnouncementsPut) | **PUT** /Api/Announcements | 


<a name="apiAnnouncementsByIdDelete"></a>
# **apiAnnouncementsByIdDelete**
> apiAnnouncementsByIdDelete(id)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiAnnouncementsByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiAnnouncementsByIdDelete");
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

<a name="apiAnnouncementsByIdGet"></a>
# **apiAnnouncementsByIdGet**
> AnnouncementRecord apiAnnouncementsByIdGet(id)

Retrieve a single announcement.

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    AnnouncementRecord result = apiInstance.apiAnnouncementsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiAnnouncementsByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**AnnouncementRecord**](AnnouncementRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiAnnouncementsDelete"></a>
# **apiAnnouncementsDelete**
> apiAnnouncementsDelete()



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
try {
    apiInstance.apiAnnouncementsDelete();
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiAnnouncementsDelete");
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

<a name="apiAnnouncementsGet"></a>
# **apiAnnouncementsGet**
> List&lt;AnnouncementRecord&gt; apiAnnouncementsGet()

Retrieves a list of all announcement entries.

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
try {
    List<AnnouncementRecord> result = apiInstance.apiAnnouncementsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiAnnouncementsGet");
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

<a name="apiAnnouncementsPost"></a>
# **apiAnnouncementsPost**
> apiAnnouncementsPost(record)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
AnnouncementRecord record = new AnnouncementRecord(); // AnnouncementRecord | 
try {
    apiInstance.apiAnnouncementsPost(record);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiAnnouncementsPost");
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

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

<a name="apiAnnouncementsPut"></a>
# **apiAnnouncementsPut**
> apiAnnouncementsPut(record)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.AnnouncementsApi;

AnnouncementsApi apiInstance = new AnnouncementsApi();
AnnouncementRecord record = new AnnouncementRecord(); // AnnouncementRecord | 
try {
    apiInstance.apiAnnouncementsPut(record);
} catch (ApiException e) {
    System.err.println("Exception when calling AnnouncementsApi#apiAnnouncementsPut");
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

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

