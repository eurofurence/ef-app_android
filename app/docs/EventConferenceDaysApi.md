# EventConferenceDaysApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2EventConferenceDaysByIdGet**](EventConferenceDaysApi.md#apiV2EventConferenceDaysByIdGet) | **GET** /Api/v2/EventConferenceDays/{Id} | Retrieve a single event conference day in the event schedule.
[**apiV2EventConferenceDaysGet**](EventConferenceDaysApi.md#apiV2EventConferenceDaysGet) | **GET** /Api/v2/EventConferenceDays | Retrieves a list of all event conference days in the event schedule.


<a name="apiV2EventConferenceDaysByIdGet"></a>
# **apiV2EventConferenceDaysByIdGet**
> EventConferenceDayRecord apiV2EventConferenceDaysByIdGet(id)

Retrieve a single event conference day in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceDaysApi;

EventConferenceDaysApi apiInstance = new EventConferenceDaysApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventConferenceDayRecord result = apiInstance.apiV2EventConferenceDaysByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceDaysApi#apiV2EventConferenceDaysByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**EventConferenceDayRecord**](EventConferenceDayRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2EventConferenceDaysGet"></a>
# **apiV2EventConferenceDaysGet**
> List&lt;EventConferenceDayRecord&gt; apiV2EventConferenceDaysGet()

Retrieves a list of all event conference days in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceDaysApi;

EventConferenceDaysApi apiInstance = new EventConferenceDaysApi();
try {
    List<EventConferenceDayRecord> result = apiInstance.apiV2EventConferenceDaysGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceDaysApi#apiV2EventConferenceDaysGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;EventConferenceDayRecord&gt;**](EventConferenceDayRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

