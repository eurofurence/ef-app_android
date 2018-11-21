# PushNotificationsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiPushNotificationsFcmDeviceRegistrationPost**](PushNotificationsApi.md#apiPushNotificationsFcmDeviceRegistrationPost) | **POST** /Api/PushNotifications/FcmDeviceRegistration | 
[**apiPushNotificationsStatisticsGet**](PushNotificationsApi.md#apiPushNotificationsStatisticsGet) | **GET** /Api/PushNotifications/Statistics | 
[**apiPushNotificationsSyncRequestPost**](PushNotificationsApi.md#apiPushNotificationsSyncRequestPost) | **POST** /Api/PushNotifications/SyncRequest | 
[**apiPushNotificationsWnsChannelRegistrationPost**](PushNotificationsApi.md#apiPushNotificationsWnsChannelRegistrationPost) | **POST** /Api/PushNotifications/WnsChannelRegistration | 
[**apiPushNotificationsWnsToastPost**](PushNotificationsApi.md#apiPushNotificationsWnsToastPost) | **POST** /Api/PushNotifications/WnsToast | 


<a name="apiPushNotificationsFcmDeviceRegistrationPost"></a>
# **apiPushNotificationsFcmDeviceRegistrationPost**
> apiPushNotificationsFcmDeviceRegistrationPost(request)



### Example
```java
// Import classes:
//import io.swagger.client.api.PushNotificationsApi;

PushNotificationsApi apiInstance = new PushNotificationsApi();
PostFcmDeviceRegistrationRequest request = new PostFcmDeviceRegistrationRequest(); // PostFcmDeviceRegistrationRequest | 
try {
    apiInstance.apiPushNotificationsFcmDeviceRegistrationPost(request);
} catch (ApiException e) {
    System.err.println("Exception when calling PushNotificationsApi#apiPushNotificationsFcmDeviceRegistrationPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**PostFcmDeviceRegistrationRequest**](PostFcmDeviceRegistrationRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

<a name="apiPushNotificationsStatisticsGet"></a>
# **apiPushNotificationsStatisticsGet**
> PushNotificationChannelStatistics apiPushNotificationsStatisticsGet(since)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.PushNotificationsApi;

PushNotificationsApi apiInstance = new PushNotificationsApi();
Date since = new Date(); // Date | 
try {
    PushNotificationChannelStatistics result = apiInstance.apiPushNotificationsStatisticsGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PushNotificationsApi#apiPushNotificationsStatisticsGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**|  | [optional]

### Return type

[**PushNotificationChannelStatistics**](PushNotificationChannelStatistics.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiPushNotificationsSyncRequestPost"></a>
# **apiPushNotificationsSyncRequestPost**
> apiPushNotificationsSyncRequestPost()



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.PushNotificationsApi;

PushNotificationsApi apiInstance = new PushNotificationsApi();
try {
    apiInstance.apiPushNotificationsSyncRequestPost();
} catch (ApiException e) {
    System.err.println("Exception when calling PushNotificationsApi#apiPushNotificationsSyncRequestPost");
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

<a name="apiPushNotificationsWnsChannelRegistrationPost"></a>
# **apiPushNotificationsWnsChannelRegistrationPost**
> apiPushNotificationsWnsChannelRegistrationPost(request)



### Example
```java
// Import classes:
//import io.swagger.client.api.PushNotificationsApi;

PushNotificationsApi apiInstance = new PushNotificationsApi();
PostWnsChannelRegistrationRequest request = new PostWnsChannelRegistrationRequest(); // PostWnsChannelRegistrationRequest | 
try {
    apiInstance.apiPushNotificationsWnsChannelRegistrationPost(request);
} catch (ApiException e) {
    System.err.println("Exception when calling PushNotificationsApi#apiPushNotificationsWnsChannelRegistrationPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**PostWnsChannelRegistrationRequest**](PostWnsChannelRegistrationRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

<a name="apiPushNotificationsWnsToastPost"></a>
# **apiPushNotificationsWnsToastPost**
> apiPushNotificationsWnsToastPost(request)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.PushNotificationsApi;

PushNotificationsApi apiInstance = new PushNotificationsApi();
ToastTest request = new ToastTest(); // ToastTest | 
try {
    apiInstance.apiPushNotificationsWnsToastPost(request);
} catch (ApiException e) {
    System.err.println("Exception when calling PushNotificationsApi#apiPushNotificationsWnsToastPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**ToastTest**](ToastTest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

