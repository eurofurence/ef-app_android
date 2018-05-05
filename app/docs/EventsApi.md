# EventsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2EventsByIdGet**](EventsApi.md#apiV2EventsByIdGet) | **GET** /Api/v2/Events/{Id} | Retrieve a single event in the event schedule.
[**apiV2EventsGet**](EventsApi.md#apiV2EventsGet) | **GET** /Api/v2/Events | Retrieves a list of all events in the event schedule.


<a name="apiV2EventsByIdGet"></a>
# **apiV2EventsByIdGet**
> EventRecord apiV2EventsByIdGet(id)

Retrieve a single event in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventsApi;

EventsApi apiInstance = new EventsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventRecord result = apiInstance.apiV2EventsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#apiV2EventsByIdGet");
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

<a name="apiV2EventsGet"></a>
# **apiV2EventsGet**
> List&lt;EventRecord&gt; apiV2EventsGet()

Retrieves a list of all events in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventsApi;

EventsApi apiInstance = new EventsApi();
try {
    List<EventRecord> result = apiInstance.apiV2EventsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventsApi#apiV2EventsGet");
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

