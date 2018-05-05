# RichPreviewApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**linkEventsByIdGet**](RichPreviewApi.md#linkEventsByIdGet) | **GET** /Link/Events/{Id} | 


<a name="linkEventsByIdGet"></a>
# **linkEventsByIdGet**
> linkEventsByIdGet(id)



### Example
```java
// Import classes:
//import io.swagger.client.api.RichPreviewApi;

RichPreviewApi apiInstance = new RichPreviewApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.linkEventsByIdGet(id);
} catch (ApiException e) {
    System.err.println("Exception when calling RichPreviewApi#linkEventsByIdGet");
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

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

