# EventFeedbackApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2EventFeedbackGet**](EventFeedbackApi.md#apiV2EventFeedbackGet) | **GET** /Api/v2/EventFeedback | 
[**apiV2EventFeedbackPost**](EventFeedbackApi.md#apiV2EventFeedbackPost) | **POST** /Api/v2/EventFeedback | 


<a name="apiV2EventFeedbackGet"></a>
# **apiV2EventFeedbackGet**
> List&lt;EventFeedbackRecord&gt; apiV2EventFeedbackGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**, **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.EventFeedbackApi;

EventFeedbackApi apiInstance = new EventFeedbackApi();
try {
    List<EventFeedbackRecord> result = apiInstance.apiV2EventFeedbackGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventFeedbackApi#apiV2EventFeedbackGet");
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

<a name="apiV2EventFeedbackPost"></a>
# **apiV2EventFeedbackPost**
> apiV2EventFeedbackPost(record)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**, **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.EventFeedbackApi;

EventFeedbackApi apiInstance = new EventFeedbackApi();
EventFeedbackRecord record = new EventFeedbackRecord(); // EventFeedbackRecord | 
try {
    apiInstance.apiV2EventFeedbackPost(record);
} catch (ApiException e) {
    System.err.println("Exception when calling EventFeedbackApi#apiV2EventFeedbackPost");
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

