# DealersApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiDealersByIdGet**](DealersApi.md#apiDealersByIdGet) | **GET** /Api/Dealers/{Id} | Retrieve a single dealer.
[**apiDealersGet**](DealersApi.md#apiDealersGet) | **GET** /Api/Dealers | Retrieves a list of all dealer entries.


<a name="apiDealersByIdGet"></a>
# **apiDealersByIdGet**
> DealerRecord apiDealersByIdGet(id)

Retrieve a single dealer.

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    DealerRecord result = apiInstance.apiDealersByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiDealersByIdGet");
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

<a name="apiDealersGet"></a>
# **apiDealersGet**
> List&lt;DealerRecord&gt; apiDealersGet()

Retrieves a list of all dealer entries.

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
try {
    List<DealerRecord> result = apiInstance.apiDealersGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiDealersGet");
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

