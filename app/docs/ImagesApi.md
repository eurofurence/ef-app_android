# ImagesApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiImagesByIdContentGet**](ImagesApi.md#apiImagesByIdContentGet) | **GET** /Api/Images/{Id}/Content | Retrieve a single image content.
[**apiImagesByIdContentPut**](ImagesApi.md#apiImagesByIdContentPut) | **PUT** /Api/Images/{Id}/Content | 
[**apiImagesByIdContentWithHashcontentHashBase64EncodedGet**](ImagesApi.md#apiImagesByIdContentWithHashcontentHashBase64EncodedGet) | **GET** /Api/Images/{Id}/Content/with-hash:{contentHashBase64Encoded} | Retrieve a single image content using hash code (preferred, as it allows caching).
[**apiImagesByIdGet**](ImagesApi.md#apiImagesByIdGet) | **GET** /Api/Images/{Id} | Retrieve a single image.
[**apiImagesGet**](ImagesApi.md#apiImagesGet) | **GET** /Api/Images | Retrieves a list of all images.


<a name="apiImagesByIdContentGet"></a>
# **apiImagesByIdContentGet**
> byte[] apiImagesByIdContentGet(id)

Retrieve a single image content.

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    byte[] result = apiInstance.apiImagesByIdContentGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiImagesByIdContentGet");
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

<a name="apiImagesByIdContentPut"></a>
# **apiImagesByIdContentPut**
> apiImagesByIdContentPut(id, imageContent)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;KnowledgeBase-Maintainer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | 
String imageContent = "imageContent_example"; // String | 
try {
    apiInstance.apiImagesByIdContentPut(id, imageContent);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiImagesByIdContentPut");
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

<a name="apiImagesByIdContentWithHashcontentHashBase64EncodedGet"></a>
# **apiImagesByIdContentWithHashcontentHashBase64EncodedGet**
> byte[] apiImagesByIdContentWithHashcontentHashBase64EncodedGet(id, contentHashBase64Encoded)

Retrieve a single image content using hash code (preferred, as it allows caching).

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | id of the requested entity
String contentHashBase64Encoded = "contentHashBase64Encoded_example"; // String | Base64 Encoded ContentHashSha1 of the requested entity
try {
    byte[] result = apiInstance.apiImagesByIdContentWithHashcontentHashBase64EncodedGet(id, contentHashBase64Encoded);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiImagesByIdContentWithHashcontentHashBase64EncodedGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |
 **contentHashBase64Encoded** | **String**| Base64 Encoded ContentHashSha1 of the requested entity |

### Return type

**byte[]**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiImagesByIdGet"></a>
# **apiImagesByIdGet**
> ImageRecord apiImagesByIdGet(id)

Retrieve a single image.

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    ImageRecord result = apiInstance.apiImagesByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiImagesByIdGet");
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

<a name="apiImagesGet"></a>
# **apiImagesGet**
> List&lt;ImageRecord&gt; apiImagesGet()

Retrieves a list of all images.

### Example
```java
// Import classes:
//import io.swagger.client.api.ImagesApi;

ImagesApi apiInstance = new ImagesApi();
try {
    List<ImageRecord> result = apiInstance.apiImagesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ImagesApi#apiImagesGet");
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

