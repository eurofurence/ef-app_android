# FursuitsApi

All URIs are relative to *https://localhost/*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2FursuitsBadgesByIdImageGet**](FursuitsApi.md#apiV2FursuitsBadgesByIdImageGet) | **GET** /Api/v2/Fursuits/Badges/{Id}/Image | 
[**apiV2FursuitsBadgesRegistrationPost**](FursuitsApi.md#apiV2FursuitsBadgesRegistrationPost) | **POST** /Api/v2/Fursuits/Badges/Registration | 
[**apiV2FursuitsCollectingGameFursuitParticipationGet**](FursuitsApi.md#apiV2FursuitsCollectingGameFursuitParticipationGet) | **GET** /Api/v2/Fursuits/CollectingGame/FursuitParticipation | 
[**apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet**](FursuitsApi.md#apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet) | **GET** /Api/v2/Fursuits/CollectingGame/FursuitParticipation/Scoreboard | 
[**apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost**](FursuitsApi.md#apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost) | **POST** /Api/v2/Fursuits/CollectingGame/FursuitParticpation/Badges/{FursuitBadgeId}/Token | 
[**apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost**](FursuitsApi.md#apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost) | **POST** /Api/v2/Fursuits/CollectingGame/PlayerParticipation/CollectToken | 
[**apiV2FursuitsCollectingGamePlayerParticipationGet**](FursuitsApi.md#apiV2FursuitsCollectingGamePlayerParticipationGet) | **GET** /Api/v2/Fursuits/CollectingGame/PlayerParticipation | 
[**apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet**](FursuitsApi.md#apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet) | **GET** /Api/v2/Fursuits/CollectingGame/PlayerParticipation/Scoreboard | 


<a name="apiV2FursuitsBadgesByIdImageGet"></a>
# **apiV2FursuitsBadgesByIdImageGet**
> apiV2FursuitsBadgesByIdImageGet(id)



### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
UUID id = new UUID(); // UUID | 
try {
    apiInstance.apiV2FursuitsBadgesByIdImageGet(id);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsBadgesByIdImageGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="apiV2FursuitsBadgesRegistrationPost"></a>
# **apiV2FursuitsBadgesRegistrationPost**
> apiV2FursuitsBadgesRegistrationPost(registration)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;FursuitBadgeSystem&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
FursuitBadgeRegistration registration = new FursuitBadgeRegistration(); // FursuitBadgeRegistration | 
try {
    apiInstance.apiV2FursuitsBadgesRegistrationPost(registration);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsBadgesRegistrationPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **registration** | [**FursuitBadgeRegistration**](FursuitBadgeRegistration.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2FursuitsCollectingGameFursuitParticipationGet"></a>
# **apiV2FursuitsCollectingGameFursuitParticipationGet**
> List&lt;FursuitParticipationInfo&gt; apiV2FursuitsCollectingGameFursuitParticipationGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<FursuitParticipationInfo> result = apiInstance.apiV2FursuitsCollectingGameFursuitParticipationGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsCollectingGameFursuitParticipationGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;FursuitParticipationInfo&gt;**](FursuitParticipationInfo.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet"></a>
# **apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet**
> List&lt;FursuitScoreboardEntry&gt; apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet()



### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<FursuitScoreboardEntry> result = apiInstance.apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsCollectingGameFursuitParticipationScoreboardGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;FursuitScoreboardEntry&gt;**](FursuitScoreboardEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost"></a>
# **apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost**
> apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost(fursuitBadgeId, tokenValue)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
UUID fursuitBadgeId = new UUID(); // UUID | 
String tokenValue = "tokenValue_example"; // String | 
try {
    apiInstance.apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost(fursuitBadgeId, tokenValue);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fursuitBadgeId** | [**UUID**](.md)|  |
 **tokenValue** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost"></a>
# **apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost**
> CollectTokenResponse apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost(tokenValue)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
String tokenValue = "tokenValue_example"; // String | 
try {
    CollectTokenResponse result = apiInstance.apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost(tokenValue);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsCollectingGamePlayerParticipationCollectTokenPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tokenValue** | **String**|  | [optional]

### Return type

[**CollectTokenResponse**](CollectTokenResponse.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json, text/json, application/json-patch+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2FursuitsCollectingGamePlayerParticipationGet"></a>
# **apiV2FursuitsCollectingGamePlayerParticipationGet**
> List&lt;PlayerParticipationInfo&gt; apiV2FursuitsCollectingGamePlayerParticipationGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<PlayerParticipationInfo> result = apiInstance.apiV2FursuitsCollectingGamePlayerParticipationGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsCollectingGamePlayerParticipationGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;PlayerParticipationInfo&gt;**](PlayerParticipationInfo.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet"></a>
# **apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet**
> List&lt;PlayerScoreboardEntry&gt; apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet()



### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<PlayerScoreboardEntry> result = apiInstance.apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiV2FursuitsCollectingGamePlayerParticipationScoreboardGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;PlayerScoreboardEntry&gt;**](PlayerScoreboardEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

