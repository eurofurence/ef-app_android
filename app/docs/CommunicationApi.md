# CommunicationApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2CommunicationPrivateMessagesByMessageIdReadPost**](CommunicationApi.md#apiV2CommunicationPrivateMessagesByMessageIdReadPost) | **POST** /Api/v2/Communication/PrivateMessages/{MessageId}/Read | Marks a given private message as read (reading receipt).
[**apiV2CommunicationPrivateMessagesGet**](CommunicationApi.md#apiV2CommunicationPrivateMessagesGet) | **GET** /Api/v2/Communication/PrivateMessages | Retrieves all private messages of an authenticated attendee.
[**apiV2CommunicationPrivateMessagesPost**](CommunicationApi.md#apiV2CommunicationPrivateMessagesPost) | **POST** /Api/v2/Communication/PrivateMessages | Sends a private message to a specific recipient/attendee.


<a name="apiV2CommunicationPrivateMessagesByMessageIdReadPost"></a>
# **apiV2CommunicationPrivateMessagesByMessageIdReadPost**
> Date apiV2CommunicationPrivateMessagesByMessageIdReadPost(messageId)

Marks a given private message as read (reading receipt).

  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  Calling this on a message that has already been marked as read   will not update the &#x60;ReadDateTimeUtc&#x60; property, but return the  &#x60;ReadDateTimeUtc&#x60; value of the first call.

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
UUID messageId = new UUID(); // UUID | `Id` of the message to mark as read
try {
    Date result = apiInstance.apiV2CommunicationPrivateMessagesByMessageIdReadPost(messageId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiV2CommunicationPrivateMessagesByMessageIdReadPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **messageId** | [**UUID**](.md)| &#x60;Id&#x60; of the message to mark as read |

### Return type

[**Date**](Date.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2CommunicationPrivateMessagesGet"></a>
# **apiV2CommunicationPrivateMessagesGet**
> List&lt;PrivateMessageRecord&gt; apiV2CommunicationPrivateMessagesGet()

Retrieves all private messages of an authenticated attendee.

  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  This will set the &#x60;ReceivedDateTimeUtc&#x60; to the current server time on all messages retrieved  that have not been retrieved in a previous call.

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
try {
    List<PrivateMessageRecord> result = apiInstance.apiV2CommunicationPrivateMessagesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiV2CommunicationPrivateMessagesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;PrivateMessageRecord&gt;**](PrivateMessageRecord.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2CommunicationPrivateMessagesPost"></a>
# **apiV2CommunicationPrivateMessagesPost**
> UUID apiV2CommunicationPrivateMessagesPost(request)

Sends a private message to a specific recipient/attendee.

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**  If the backend has a push-channel available to any given device(s) that are currently signed into the app   with the same recipient uid, it will push a toast message to those devices.    The toast message content is defined by the &#x60;ToastTitle&#x60; and &#x60;ToastMessage&#x60; properties.

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
SendPrivateMessageRequest request = new SendPrivateMessageRequest(); // SendPrivateMessageRequest | 
try {
    UUID result = apiInstance.apiV2CommunicationPrivateMessagesPost(request);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiV2CommunicationPrivateMessagesPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**SendPrivateMessageRequest**](SendPrivateMessageRequest.md)|  | [optional]

### Return type

[**UUID**](UUID.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: text/plain, application/json, text/json

