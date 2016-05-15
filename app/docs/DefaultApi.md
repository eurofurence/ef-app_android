# DefaultApi

All URIs are relative to *https://eurofurencewebapi.azurewebsites.net*

Method | HTTP request | Description
------------- | ------------- | -------------
[**announcementGet**](DefaultApi.md#announcementGet) | **GET** /Announcement | 
[**dealerGet**](DefaultApi.md#dealerGet) | **GET** /Dealer | 
[**endpointGet**](DefaultApi.md#endpointGet) | **GET** /Endpoint | 
[**eventConferenceDayGet**](DefaultApi.md#eventConferenceDayGet) | **GET** /EventConferenceDay | 
[**eventConferenceRoomGet**](DefaultApi.md#eventConferenceRoomGet) | **GET** /EventConferenceRoom | 
[**eventConferenceTrackGet**](DefaultApi.md#eventConferenceTrackGet) | **GET** /EventConferenceTrack | 
[**eventEntryGet**](DefaultApi.md#eventEntryGet) | **GET** /EventEntry | 
[**imageGet**](DefaultApi.md#imageGet) | **GET** /Image | 
[**infoGet**](DefaultApi.md#infoGet) | **GET** /Info | 
[**infoGroupGet**](DefaultApi.md#infoGroupGet) | **GET** /InfoGroup | 


<a name="announcementGet"></a>
# **announcementGet**
> List&lt;Announcement&gt; announcementGet(since)



tbd

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<Announcement> result = apiInstance.announcementGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#announcementGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;Announcement&gt;**](Announcement.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="dealerGet"></a>
# **dealerGet**
> List&lt;Dealer&gt; dealerGet(since)



tbd

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<Dealer> result = apiInstance.dealerGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#dealerGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;Dealer&gt;**](Dealer.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="endpointGet"></a>
# **endpointGet**
> Endpoint endpointGet()



Gets metadata information about the API Endpoint.

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
try {
    Endpoint result = apiInstance.endpointGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#endpointGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Endpoint**](Endpoint.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="eventConferenceDayGet"></a>
# **eventConferenceDayGet**
> List&lt;EventConferenceDay&gt; eventConferenceDayGet(since)



Retrieves a list of all event conference days.

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<EventConferenceDay> result = apiInstance.eventConferenceDayGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#eventConferenceDayGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;EventConferenceDay&gt;**](EventConferenceDay.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="eventConferenceRoomGet"></a>
# **eventConferenceRoomGet**
> List&lt;EventConferenceRoom&gt; eventConferenceRoomGet(since)



Retrieves a list of all conference rooms.

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<EventConferenceRoom> result = apiInstance.eventConferenceRoomGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#eventConferenceRoomGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;EventConferenceRoom&gt;**](EventConferenceRoom.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="eventConferenceTrackGet"></a>
# **eventConferenceTrackGet**
> List&lt;EventConferenceTrack&gt; eventConferenceTrackGet(since)



Retrieves a list of all event conference tracks.

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<EventConferenceTrack> result = apiInstance.eventConferenceTrackGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#eventConferenceTrackGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;EventConferenceTrack&gt;**](EventConferenceTrack.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="eventEntryGet"></a>
# **eventEntryGet**
> List&lt;EventEntry&gt; eventEntryGet(since)



Retrieves a list of all events in the event schedule.

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<EventEntry> result = apiInstance.eventEntryGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#eventEntryGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;EventEntry&gt;**](EventEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="imageGet"></a>
# **imageGet**
> List&lt;Image&gt; imageGet(since)



tbd

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<Image> result = apiInstance.imageGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#imageGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;Image&gt;**](Image.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="infoGet"></a>
# **infoGet**
> List&lt;Info&gt; infoGet(since)



tbd

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<Info> result = apiInstance.infoGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#infoGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;Info&gt;**](Info.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="infoGroupGet"></a>
# **infoGroupGet**
> List&lt;InfoGroup&gt; infoGroupGet(since)



tbd

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
Date since = new Date(); // Date | Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* >= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
try {
    List<InfoGroup> result = apiInstance.infoGroupGet(since);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#infoGroupGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **since** | **Date**| Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;&#x3D; the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set. | [optional]

### Return type

[**List&lt;InfoGroup&gt;**](InfoGroup.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

