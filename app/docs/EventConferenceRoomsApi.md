# EventConferenceRoomsApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiEventConferenceRoomsByIdGet**](EventConferenceRoomsApi.md#apiEventConferenceRoomsByIdGet) | **GET** /Api/EventConferenceRooms/{Id} | Retrieve a single event conference room in the event schedule.
[**apiEventConferenceRoomsGet**](EventConferenceRoomsApi.md#apiEventConferenceRoomsGet) | **GET** /Api/EventConferenceRooms | Retrieves a list of all event conference Rooms in the event schedule.


<a name="apiEventConferenceRoomsByIdGet"></a>
# **apiEventConferenceRoomsByIdGet**
> EventConferenceRoomRecord apiEventConferenceRoomsByIdGet(id)

Retrieve a single event conference room in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceRoomsApi;

EventConferenceRoomsApi apiInstance = new EventConferenceRoomsApi();
UUID id = new UUID(); // UUID | id of the requested entity
try {
    EventConferenceRoomRecord result = apiInstance.apiEventConferenceRoomsByIdGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceRoomsApi#apiEventConferenceRoomsByIdGet");
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

<a name="apiEventConferenceRoomsGet"></a>
# **apiEventConferenceRoomsGet**
> List&lt;EventConferenceRoomRecord&gt; apiEventConferenceRoomsGet()

Retrieves a list of all event conference Rooms in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.EventConferenceRoomsApi;

EventConferenceRoomsApi apiInstance = new EventConferenceRoomsApi();
try {
    List<EventConferenceRoomRecord> result = apiInstance.apiEventConferenceRoomsGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling EventConferenceRoomsApi#apiEventConferenceRoomsGet");
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

