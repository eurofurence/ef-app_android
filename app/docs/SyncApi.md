# SyncApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiSyncGet**](SyncApi.md#apiSyncGet) | **GET** /Api/Sync | Returns everything you could ever wish for.


<a name="apiSyncGet"></a>
# **apiSyncGet**
> AggregatedDeltaResponse apiSyncGet(since)

Returns everything you could ever wish for.

### Example
```java
// Import classes:
//import io.swagger.client.api.SyncApi;

SyncApi apiInstance = new SyncApi();
Date since = new Date(); // Date | 
try {
    AggregatedDeltaResponse result = apiInstance.apiSyncGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling SyncApi#apiSyncGet");
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

