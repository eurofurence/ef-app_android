# EventConferenceTracksApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiEventConferenceTracksByIdGet**](EventConferenceTracksApi.md#apiEventConferenceTracksByIdGet) | **GET** /Api/EventConferenceTracks/{Id} | Retrieve a single event conference track in the event schedule.
[**apiEventConferenceTracksGet**](EventConferenceTracksApi.md#apiEventConferenceTracksGet) | **GET** /Api/EventConferenceTracks | Retrieves a list of all event conference tracks in the event schedule.


<a name="apiEventConferenceTracksByIdGet"></a>
# **apiEventConferenceTracksByIdGet**
> EventConferenceTrackRecord apiEventConferenceTracksByIdGet(id)

Retrieve a single event conference track in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceTracksApi;

EventConferenceTracksApi apiInstance = new EventConferenceTracksApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventConferenceTrackRecord result = apiInstance.apiEventConferenceTracksByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceTracksApi#apiEventConferenceTracksByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**EventConferenceTrackRecord**](EventConferenceTrackRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiEventConferenceTracksGet"></a>
# **apiEventConferenceTracksGet**
> List&lt;EventConferenceTrackRecord&gt; apiEventConferenceTracksGet()

Retrieves a list of all event conference tracks in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceTracksApi;

EventConferenceTracksApi apiInstance = new EventConferenceTracksApi();
try {
    List<EventConferenceTrackRecord> result = apiInstance.apiEventConferenceTracksGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceTracksApi#apiEventConferenceTracksGet");
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

