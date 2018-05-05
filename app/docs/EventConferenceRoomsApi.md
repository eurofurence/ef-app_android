# EventConferenceRoomsApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2EventConferenceRoomsByIdGet**](EventConferenceRoomsApi.md#apiV2EventConferenceRoomsByIdGet) | **GET** /Api/v2/EventConferenceRooms/{Id} | Retrieve a single event conference room in the event schedule.
[**apiV2EventConferenceRoomsGet**](EventConferenceRoomsApi.md#apiV2EventConferenceRoomsGet) | **GET** /Api/v2/EventConferenceRooms | Retrieves a list of all event conference Rooms in the event schedule.


<a name="apiV2EventConferenceRoomsByIdGet"></a>
# **apiV2EventConferenceRoomsByIdGet**
> EventConferenceRoomRecord apiV2EventConferenceRoomsByIdGet(id)

Retrieve a single event conference room in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceRoomsApi;

EventConferenceRoomsApi apiInstance = new EventConferenceRoomsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventConferenceRoomRecord result = apiInstance.apiV2EventConferenceRoomsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceRoomsApi#apiV2EventConferenceRoomsByIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| id of the requested entity |

### Return type

[**EventConferenceRoomRecord**](EventConferenceRoomRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2EventConferenceRoomsGet"></a>
# **apiV2EventConferenceRoomsGet**
> List&lt;EventConferenceRoomRecord&gt; apiV2EventConferenceRoomsGet()

Retrieves a list of all event conference Rooms in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceRoomsApi;

EventConferenceRoomsApi apiInstance = new EventConferenceRoomsApi();
try {
    List<EventConferenceRoomRecord> result = apiInstance.apiV2EventConferenceRoomsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceRoomsApi#apiV2EventConferenceRoomsGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;EventConferenceRoomRecord&gt;**](EventConferenceRoomRecord.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

