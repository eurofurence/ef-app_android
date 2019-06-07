# EventConferenceDaysApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiEventConferenceDaysByIdGet**](EventConferenceDaysApi.md#apiEventConferenceDaysByIdGet) | **GET** /Api/EventConferenceDays/{Id} | Retrieve a single event conference day in the event schedule.
[**apiEventConferenceDaysGet**](EventConferenceDaysApi.md#apiEventConferenceDaysGet) | **GET** /Api/EventConferenceDays | Retrieves a list of all event conference days in the event schedule.


<a name="apiEventConferenceDaysByIdGet"></a>
# **apiEventConferenceDaysByIdGet**
> EventConferenceDayRecord apiEventConferenceDaysByIdGet(id)

Retrieve a single event conference day in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceDaysApi;

EventConferenceDaysApi apiInstance = new EventConferenceDaysApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventConferenceDayRecord result = apiInstance.apiEventConferenceDaysByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceDaysApi#apiEventConferenceDaysByIdGet");
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

<a name="apiEventConferenceDaysGet"></a>
# **apiEventConferenceDaysGet**
> List&lt;EventConferenceDayRecord&gt; apiEventConferenceDaysGet()

Retrieves a list of all event conference days in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceDaysApi;

EventConferenceDaysApi apiInstance = new EventConferenceDaysApi();
try {
    List<EventConferenceDayRecord> result = apiInstance.apiEventConferenceDaysGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceDaysApi#apiEventConferenceDaysGet");
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

