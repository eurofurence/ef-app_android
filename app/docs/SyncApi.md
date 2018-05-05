# SyncApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2SyncGet**](SyncApi.md#apiV2SyncGet) | **GET** /Api/v2/Sync | Returns everything you could ever wish for.


<a name="apiV2SyncGet"></a>
# **apiV2SyncGet**
> AggregatedDeltaResponse apiV2SyncGet(since)

Returns everything you could ever wish for.

### Example
```java
// Import classes:
//import io.swagger.client.api.SyncApi;

SyncApi apiInstance = new SyncApi();
Date since = new Date(); // Date | 
try {
    AggregatedDeltaResponse result = apiInstance.apiV2SyncGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SyncApi#apiV2SyncGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**|  | [optional]

### Return type

[**AggregatedDeltaResponse**](AggregatedDeltaResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

