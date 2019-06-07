# CommunicationApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiCommunicationPrivateMessagesByMessageIdReadPost**](CommunicationApi.md#apiCommunicationPrivateMessagesByMessageIdReadPost) | **POST** /Api/Communication/PrivateMessages/{MessageId}/Read | Marks a given private message as read (reading receipt).
[**apiCommunicationPrivateMessagesByMessageIdStatusGet**](CommunicationApi.md#apiCommunicationPrivateMessagesByMessageIdStatusGet) | **GET** /Api/Communication/PrivateMessages/{MessageId}/Status | 
[**apiCommunicationPrivateMessagesGet**](CommunicationApi.md#apiCommunicationPrivateMessagesGet) | **GET** /Api/Communication/PrivateMessages | Retrieves all private messages of an authenticated attendee.
[**apiCommunicationPrivateMessagesPost**](CommunicationApi.md#apiCommunicationPrivateMessagesPost) | **POST** /Api/Communication/PrivateMessages | Sends a private message to a specific recipient/attendee.
[**apiCommunicationPrivateMessagesSentByMeGet**](CommunicationApi.md#apiCommunicationPrivateMessagesSentByMeGet) | **GET** /Api/Communication/PrivateMessages/:sent-by-me | 


<a name="apiCommunicationPrivateMessagesByMessageIdReadPost"></a>
# **apiCommunicationPrivateMessagesByMessageIdReadPost**
> Date apiCommunicationPrivateMessagesByMessageIdReadPost(messageId, isRead)

Marks a given private message as read (reading receipt).

  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  Calling this on a message that has already been marked as read  will not update the &#x60;ReadDateTimeUtc&#x60; property, but return the  &#x60;ReadDateTimeUtc&#x60; value of the first call.

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
UUID messageId = new UUID(); // UUID | `Id` of the message to mark as read
Boolean isRead = true; // Boolean | boolean, expected to be 'true' always
try {
    Date result = apiInstance.apiCommunicationPrivateMessagesByMessageIdReadPost(messageId, isRead);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiCommunicationPrivateMessagesByMessageIdReadPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **messageId** | [**UUID**](.md)| &#x60;Id&#x60; of the message to mark as read |
 **isRead** | **Boolean**| boolean, expected to be &#39;true&#39; always | [optional]

### Return type

[**Date**](Date.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiCommunicationPrivateMessagesByMessageIdStatusGet"></a>
# **apiCommunicationPrivateMessagesByMessageIdStatusGet**
> PrivateMessageStatus apiCommunicationPrivateMessagesByMessageIdStatusGet(messageId)



  * Requires authorization     * Requires any of the following roles: **&#x60;Action-PrivateMessages-Query&#x60;**, **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
UUID messageId = new UUID(); // UUID | 
try {
    PrivateMessageStatus result = apiInstance.apiCommunicationPrivateMessagesByMessageIdStatusGet(messageId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiCommunicationPrivateMessagesByMessageIdStatusGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **messageId** | [**UUID**](.md)|  |

### Return type

[**PrivateMessageStatus**](PrivateMessageStatus.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiCommunicationPrivateMessagesGet"></a>
# **apiCommunicationPrivateMessagesGet**
> List&lt;PrivateMessageRecord&gt; apiCommunicationPrivateMessagesGet()

Retrieves all private messages of an authenticated attendee.

  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  This will set the &#x60;ReceivedDateTimeUtc&#x60; to the current server time on all messages retrieved  that have not been retrieved in a previous call.

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
try {
    List<PrivateMessageRecord> result = apiInstance.apiCommunicationPrivateMessagesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiCommunicationPrivateMessagesGet");
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

<a name="apiCommunicationPrivateMessagesPost"></a>
# **apiCommunicationPrivateMessagesPost**
> UUID apiCommunicationPrivateMessagesPost(request)

Sends a private message to a specific recipient/attendee.

  * Requires authorization     * Requires any of the following roles: **&#x60;Action-PrivateMessages-Send&#x60;**, **&#x60;Developer&#x60;**, **&#x60;System&#x60;**  If the backend has a push-channel available to any given device(s) that are currently signed into the app  with the same recipient uid, it will push a toast message to those devices.  The toast message content is defined by the &#x60;ToastTitle&#x60; and &#x60;ToastMessage&#x60; properties.

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
SendPrivateMessageRequest request = new SendPrivateMessageRequest(); // SendPrivateMessageRequest | 
try {
    UUID result = apiInstance.apiCommunicationPrivateMessagesPost(request);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiCommunicationPrivateMessagesPost");
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

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiCommunicationPrivateMessagesSentByMeGet"></a>
# **apiCommunicationPrivateMessagesSentByMeGet**
> List&lt;PrivateMessageRecord&gt; apiCommunicationPrivateMessagesSentByMeGet()



  * Requires authorization   

### Example
```java
// Import classes:
//import io.swagger.client.api.CommunicationApi;

CommunicationApi apiInstance = new CommunicationApi();
try {
    List<PrivateMessageRecord> result = apiInstance.apiCommunicationPrivateMessagesSentByMeGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling CommunicationApi#apiCommunicationPrivateMessagesSentByMeGet");
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

