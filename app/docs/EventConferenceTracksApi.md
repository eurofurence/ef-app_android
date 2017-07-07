# EventConferenceTracksApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2EventConferenceTracksByIdGet**](EventConferenceTracksApi.md#apiV2EventConferenceTracksByIdGet) | **GET** /Api/v2/EventConferenceTracks/{Id} | Retrieve a single event conference track in the event schedule.
[**apiV2EventConferenceTracksGet**](EventConferenceTracksApi.md#apiV2EventConferenceTracksGet) | **GET** /Api/v2/EventConferenceTracks | Retrieves a list of all event conference tracks in the event schedule.


<a name="apiV2EventConferenceTracksByIdGet"></a>
# **apiV2EventConferenceTracksByIdGet**
> EventConferenceTrackRecord apiV2EventConferenceTracksByIdGet(id)

Retrieve a single event conference track in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceTracksApi;

EventConferenceTracksApi apiInstance = new EventConferenceTracksApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventConferenceTrackRecord result = apiInstance.apiV2EventConferenceTracksByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceTracksApi#apiV2EventConferenceTracksByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **UUID**| id of the requested entity |

### Return type

[**EventConferenceTrackRecord**](EventConferenceTrackRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2EventConferenceTracksGet"></a>
# **apiV2EventConferenceTracksGet**
> List&lt;EventConferenceTrackRecord&gt; apiV2EventConferenceTracksGet()

Retrieves a list of all event conference tracks in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceTracksApi;

EventConferenceTracksApi apiInstance = new EventConferenceTracksApi();
try {
    List<EventConferenceTrackRecord> result = apiInstance.apiV2EventConferenceTracksGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceTracksApi#apiV2EventConferenceTracksGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;EventConferenceTrackRecord&gt;**](EventConferenceTrackRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

