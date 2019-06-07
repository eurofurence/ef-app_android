# ArtShowApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiArtShowItemActivitesLogPost**](ArtShowApi.md#apiArtShowItemActivitesLogPost) | **POST** /Api/ArtShow/ItemActivites/Log | 
[**apiArtShowItemActivitesNotificationBundlesSendPost**](ArtShowApi.md#apiArtShowItemActivitesNotificationBundlesSendPost) | **POST** /Api/ArtShow/ItemActivites/NotificationBundles/Send | 
[**apiArtShowItemActivitesNotificationBundlesSimulationGet**](ArtShowApi.md#apiArtShowItemActivitesNotificationBundlesSimulationGet) | **GET** /Api/ArtShow/ItemActivites/NotificationBundles/Simulation | 


<a name="apiArtShowItemActivitesLogPost"></a>
# **apiArtShowItemActivitesLogPost**
> apiArtShowItemActivitesLogPost(payload)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ArtShowApi;

ArtShowApi apiInstance = new ArtShowApi();
byte[] payload = BINARY_DATA_HERE; // byte[] | Art show log contents
try {
    apiInstance.apiArtShowItemActivitesLogPost(payload);
} catch (ApiException e) {
    System.err.println("Exception when calling ArtShowApi#apiArtShowItemActivitesLogPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **payload** | **byte[]**| Art show log contents |

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/octet-stream
 - **Accept**: Not defined

<a name="apiArtShowItemActivitesNotificationBundlesSendPost"></a>
# **apiArtShowItemActivitesNotificationBundlesSendPost**
> apiArtShowItemActivitesNotificationBundlesSendPost()



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ArtShowApi;

ArtShowApi apiInstance = new ArtShowApi();
try {
    apiInstance.apiArtShowItemActivitesNotificationBundlesSendPost();
} catch (ApiException e) {
    System.err.println("Exception when calling ArtShowApi#apiArtShowItemActivitesNotificationBundlesSendPost");
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

<a name="apiArtShowItemActivitesNotificationBundlesSimulationGet"></a>
# **apiArtShowItemActivitesNotificationBundlesSimulationGet**
> apiArtShowItemActivitesNotificationBundlesSimulationGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ArtShowApi;

ArtShowApi apiInstance = new ArtShowApi();
try {
    apiInstance.apiArtShowItemActivitesNotificationBundlesSimulationGet();
} catch (ApiException e) {
    System.err.println("Exception when calling ArtShowApi#apiArtShowItemActivitesNotificationBundlesSimulationGet");
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

