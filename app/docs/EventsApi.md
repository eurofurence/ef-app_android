# EventsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiEventsByIdGet**](EventsApi.md#apiEventsByIdGet) | **GET** /Api/Events/{Id} | Retrieve a single event in the event schedule.
[**apiEventsGet**](EventsApi.md#apiEventsGet) | **GET** /Api/Events | Retrieves a list of all events in the event schedule.


<a name="apiEventsByIdGet"></a>
# **apiEventsByIdGet**
> EventRecord apiEventsByIdGet(id)

Retrieve a single event in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventsApi;

EventsApi apiInstance = new EventsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventRecord result = apiInstance.apiEventsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#apiEventsByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**EventRecord**](EventRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiEventsGet"></a>
# **apiEventsGet**
> List&lt;EventRecord&gt; apiEventsGet()

Retrieves a list of all events in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventsApi;

EventsApi apiInstance = new EventsApi();
try {
    List<EventRecord> result = apiInstance.apiEventsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#apiEventsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;EventRecord&gt;**](EventRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

