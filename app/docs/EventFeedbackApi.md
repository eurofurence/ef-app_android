# EventFeedbackApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiEventFeedbackPost**](EventFeedbackApi.md#apiEventFeedbackPost) | **POST** /Api/EventFeedback | 


<a name="apiEventFeedbackPost"></a>
# **apiEventFeedbackPost**
> apiEventFeedbackPost(record)



### Example
```java
// Import classes:
//import io.swagger.client.api.EventFeedbackApi;

EventFeedbackApi apiInstance = new EventFeedbackApi();
EventFeedbackRecord record = new EventFeedbackRecord(); // EventFeedbackRecord | 
try {
    apiInstance.apiEventFeedbackPost(record);
} catch (ApiException e) {
    System.err.println("Exception when calling EventFeedbackApi#apiEventFeedbackPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **record** | [**EventFeedbackRecord**](EventFeedbackRecord.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

