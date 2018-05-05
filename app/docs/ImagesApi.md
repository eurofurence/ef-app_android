# ImagesApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2ImagesByIdContentGet**](ImagesApi.md#apiV2ImagesByIdContentGet) | **GET** /Api/v2/Images/{Id}/Content | Retrieve a single image content.
[**apiV2ImagesByIdContentPut**](ImagesApi.md#apiV2ImagesByIdContentPut) | **PUT** /Api/v2/Images/{Id}/Content | 
[**apiV2ImagesByIdGet**](ImagesApi.md#apiV2ImagesByIdGet) | **GET** /Api/v2/Images/{Id} | Retrieve a single image.
[**apiV2ImagesGet**](ImagesApi.md#apiV2ImagesGet) | **GET** /Api/v2/Images | Retrieves a list of all images.


<a name="apiV2ImagesByIdContentGet"></a>
# **apiV2ImagesByIdContentGet**
> byte[] apiV2ImagesByIdContentGet(id)

Retrieve a single image content.

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    byte[] result = apiInstance.apiV2ImagesByIdContentGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiV2ImagesByIdContentGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

**byte[]**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2ImagesByIdContentPut"></a>
# **apiV2ImagesByIdContentPut**
> apiV2ImagesByIdContentPut(id, imageContent)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | 
String imageContent = "imageContent_example"; // String | 
try {
    apiInstance.apiV2ImagesByIdContentPut(id, imageContent);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiV2ImagesByIdContentPut");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |
 **imageContent** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

<a name="apiV2ImagesByIdGet"></a>
# **apiV2ImagesByIdGet**
> ImageRecord apiV2ImagesByIdGet(id)

Retrieve a single image.

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    ImageRecord result = apiInstance.apiV2ImagesByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiV2ImagesByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**ImageRecord**](ImageRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2ImagesGet"></a>
# **apiV2ImagesGet**
> List&lt;ImageRecord&gt; apiV2ImagesGet()

Retrieves a list of all images.

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
try {
    List<ImageRecord> result = apiInstance.apiV2ImagesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiV2ImagesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;ImageRecord&gt;**](ImageRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

