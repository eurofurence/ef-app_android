# DealersApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiDealersByIdDelete**](DealersApi.md#apiDealersByIdDelete) | **DELETE** /Api/Dealers/{Id} | Delete a dealer.
[**apiDealersByIdGet**](DealersApi.md#apiDealersByIdGet) | **GET** /Api/Dealers/{Id} | Retrieve a single dealer.
[**apiDealersByIdPut**](DealersApi.md#apiDealersByIdPut) | **PUT** /Api/Dealers/{Id} | Update an existing dealer.
[**apiDealersGet**](DealersApi.md#apiDealersGet) | **GET** /Api/Dealers | Retrieves a list of all dealer entries.
[**apiDealersPost**](DealersApi.md#apiDealersPost) | **POST** /Api/Dealers | Create a new dealer.


<a name="apiDealersByIdDelete"></a>
# **apiDealersByIdDelete**
> apiDealersByIdDelete(id)

Delete a dealer.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiDealersByIdDelete(id);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiDealersByIdDelete");
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
 - **Accept**: text/plain, application/json, text/json

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

<a name="apiDealersByIdPut"></a>
# **apiDealersByIdPut**
> apiDealersByIdPut(id, record)

Update an existing dealer.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
UUID id = new UUID(); // UUID | 
DealerRecord record = new DealerRecord(); // DealerRecord | 
try {
    apiInstance.apiDealersByIdPut(id, record);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiDealersByIdPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **record** | [**DealerRecord**](DealerRecord.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
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

<a name="apiDealersPost"></a>
# **apiDealersPost**
> UUID apiDealersPost(record)

Create a new dealer.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.DealersApi;

DealersApi apiInstance = new DealersApi();
DealerRecord record = new DealerRecord(); // DealerRecord | 
try {
    UUID result = apiInstance.apiDealersPost(record);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DealersApi#apiDealersPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **record** | [**DealerRecord**](DealerRecord.md)|  | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

