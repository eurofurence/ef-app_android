# DealersApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2DealersByIdGet**](DealersApi.md#apiV2DealersByIdGet) | **GET** /Api/v2/Dealers/{Id} | Retrieve a single dealer.
[**apiV2DealersGet**](DealersApi.md#apiV2DealersGet) | **GET** /Api/v2/Dealers | Retrieves a list of all dealer entries.


<a name="apiV2DealersByIdGet"></a>
# **apiV2DealersByIdGet**
> DealerRecord apiV2DealersByIdGet(id)

Retrieve a single dealer.

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    DealerRecord result = apiInstance.apiV2DealersByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiV2DealersByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**DealerRecord**](DealerRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2DealersGet"></a>
# **apiV2DealersGet**
> List&lt;DealerRecord&gt; apiV2DealersGet()

Retrieves a list of all dealer entries.

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
try {
    List<DealerRecord> result = apiInstance.apiV2DealersGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiV2DealersGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;DealerRecord&gt;**](DealerRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

