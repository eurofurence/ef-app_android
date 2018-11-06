# EventFeedbackApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiEventFeedbackGet**](EventFeedbackApi.md#apiEventFeedbackGet) | **GET** /Api/EventFeedback | 
[**apiEventFeedbackPost**](EventFeedbackApi.md#apiEventFeedbackPost) | **POST** /Api/EventFeedback | 


<a name="apiEventFeedbackGet"></a>
# **apiEventFeedbackGet**
> List&lt;EventFeedbackRecord&gt; apiEventFeedbackGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**, **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.EventFeedbackApi;

EventFeedbackApi apiInstance = new EventFeedbackApi();
try {
    List<EventFeedbackRecord> result = apiInstance.apiEventFeedbackGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventFeedbackApi#apiEventFeedbackGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;EventFeedbackRecord&gt;**](EventFeedbackRecord.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiEventFeedbackPost"></a>
# **apiEventFeedbackPost**
> apiEventFeedbackPost(record)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**, **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

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

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

