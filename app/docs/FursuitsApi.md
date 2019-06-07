# FursuitsApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiFursuitsBadgesByIdImageGet**](FursuitsApi.md#apiFursuitsBadgesByIdImageGet) | **GET** /Api/Fursuits/Badges/{Id}/Image | Retrieve the badge image content for a given fursuit badge id
[**apiFursuitsBadgesGet**](FursuitsApi.md#apiFursuitsBadgesGet) | **GET** /Api/Fursuits/Badges | Return all Fursuit Badge Registrations
[**apiFursuitsBadgesRegistrationPost**](FursuitsApi.md#apiFursuitsBadgesRegistrationPost) | **POST** /Api/Fursuits/Badges/Registration | Upsert Fursuit Badge information
[**apiFursuitsCollectingGameFursuitParticipationGet**](FursuitsApi.md#apiFursuitsCollectingGameFursuitParticipationGet) | **GET** /Api/Fursuits/CollectingGame/FursuitParticipation | 
[**apiFursuitsCollectingGameFursuitParticipationScoreboardGet**](FursuitsApi.md#apiFursuitsCollectingGameFursuitParticipationScoreboardGet) | **GET** /Api/Fursuits/CollectingGame/FursuitParticipation/Scoreboard | 
[**apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost**](FursuitsApi.md#apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost) | **POST** /Api/Fursuits/CollectingGame/FursuitParticpation/Badges/{FursuitBadgeId}/Token | Register (link/assign) a valid, unused token to a fursuit badge.
[**apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost**](FursuitsApi.md#apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost) | **POST** /Api/Fursuits/CollectingGame/FursuitParticpation/Badges/{FursuitBadgeId}/Token:safe | 
[**apiFursuitsCollectingGamePlayerParticipationCollectTokenPost**](FursuitsApi.md#apiFursuitsCollectingGamePlayerParticipationCollectTokenPost) | **POST** /Api/Fursuits/CollectingGame/PlayerParticipation/CollectToken | 
[**apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost**](FursuitsApi.md#apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost) | **POST** /Api/Fursuits/CollectingGame/PlayerParticipation/CollectToken:safe | 
[**apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet**](FursuitsApi.md#apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet) | **GET** /Api/Fursuits/CollectingGame/PlayerParticipation/CollectionEntries | 
[**apiFursuitsCollectingGamePlayerParticipationGet**](FursuitsApi.md#apiFursuitsCollectingGamePlayerParticipationGet) | **GET** /Api/Fursuits/CollectingGame/PlayerParticipation | 
[**apiFursuitsCollectingGamePlayerParticipationScoreboardGet**](FursuitsApi.md#apiFursuitsCollectingGamePlayerParticipationScoreboardGet) | **GET** /Api/Fursuits/CollectingGame/PlayerParticipation/Scoreboard | 
[**apiFursuitsCollectingGameRecalculatePost**](FursuitsApi.md#apiFursuitsCollectingGameRecalculatePost) | **POST** /Api/Fursuits/CollectingGame/Recalculate | 
[**apiFursuitsCollectingGameTokensBatchPost**](FursuitsApi.md#apiFursuitsCollectingGameTokensBatchPost) | **POST** /Api/Fursuits/CollectingGame/Tokens/Batch | 
[**apiFursuitsCollectingGameTokensPost**](FursuitsApi.md#apiFursuitsCollectingGameTokensPost) | **POST** /Api/Fursuits/CollectingGame/Tokens | 


<a name="apiFursuitsBadgesByIdImageGet"></a>
# **apiFursuitsBadgesByIdImageGet**
> byte[] apiFursuitsBadgesByIdImageGet(id)

Retrieve the badge image content for a given fursuit badge id

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
UUID id = new UUID(); // UUID | \"Id\" of the fursuit badge
try {
    byte[] result = apiInstance.apiFursuitsBadgesByIdImageGet(id);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsBadgesByIdImageGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | [**UUID**](.md)| \&quot;Id\&quot; of the fursuit badge |

### Return type

**byte[]**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsBadgesGet"></a>
# **apiFursuitsBadgesGet**
> List&lt;FursuitBadgeRecord&gt; apiFursuitsBadgesGet()

Return all Fursuit Badge Registrations

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;FursuitBadgeSystem&#x60;**, **&#x60;System&#x60;**  **Not meant to be consumed by the mobile apps**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<FursuitBadgeRecord> result = apiInstance.apiFursuitsBadgesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsBadgesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;FursuitBadgeRecord&gt;**](FursuitBadgeRecord.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsBadgesRegistrationPost"></a>
# **apiFursuitsBadgesRegistrationPost**
> apiFursuitsBadgesRegistrationPost(registration)

Upsert Fursuit Badge information

  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;FursuitBadgeSystem&#x60;**, **&#x60;System&#x60;**  This is used by the fursuit badge system to push badge information to this backend.  **Not meant to be consumed by the mobile apps**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
FursuitBadgeRegistration registration = new FursuitBadgeRegistration(); // FursuitBadgeRegistration | 
try {
    apiInstance.apiFursuitsBadgesRegistrationPost(registration);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsBadgesRegistrationPost");
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

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGameFursuitParticipationGet"></a>
# **apiFursuitsCollectingGameFursuitParticipationGet**
> List&lt;FursuitParticipationInfo&gt; apiFursuitsCollectingGameFursuitParticipationGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<FursuitParticipationInfo> result = apiInstance.apiFursuitsCollectingGameFursuitParticipationGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameFursuitParticipationGet");
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

<a name="apiFursuitsCollectingGameFursuitParticipationScoreboardGet"></a>
# **apiFursuitsCollectingGameFursuitParticipationScoreboardGet**
> List&lt;FursuitScoreboardEntry&gt; apiFursuitsCollectingGameFursuitParticipationScoreboardGet(top)



### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
Integer top = 25; // Integer | 
try {
    List<FursuitScoreboardEntry> result = apiInstance.apiFursuitsCollectingGameFursuitParticipationScoreboardGet(top);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameFursuitParticipationScoreboardGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **top** | **Integer**|  | [optional] [default to 25]

### Return type

[**List&lt;FursuitScoreboardEntry&gt;**](FursuitScoreboardEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost"></a>
# **apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost**
> apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost(fursuitBadgeId, tokenValue)

Register (link/assign) a valid, unused token to a fursuit badge.

  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
UUID fursuitBadgeId = new UUID(); // UUID | 
String tokenValue = "tokenValue_example"; // String | 
try {
    apiInstance.apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost(fursuitBadgeId, tokenValue);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenPost");
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

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost"></a>
# **apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost**
> ApiSafeResult apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost(fursuitBadgeId, tokenValue)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
UUID fursuitBadgeId = new UUID(); // UUID | 
String tokenValue = "tokenValue_example"; // String | 
try {
    ApiSafeResult result = apiInstance.apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost(fursuitBadgeId, tokenValue);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameFursuitParticpationBadgesByFursuitBadgeIdTokenSafePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **fursuitBadgeId** | [**UUID**](.md)|  |
 **tokenValue** | **String**|  | [optional]

### Return type

[**ApiSafeResult**](ApiSafeResult.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGamePlayerParticipationCollectTokenPost"></a>
# **apiFursuitsCollectingGamePlayerParticipationCollectTokenPost**
> CollectTokenResponse apiFursuitsCollectingGamePlayerParticipationCollectTokenPost(tokenValue)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
String tokenValue = "tokenValue_example"; // String | 
try {
    CollectTokenResponse result = apiInstance.apiFursuitsCollectingGamePlayerParticipationCollectTokenPost(tokenValue);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGamePlayerParticipationCollectTokenPost");
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

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost"></a>
# **apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost**
> ApiSafeResultCollectTokenResponse apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost(tokenValue)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
String tokenValue = "tokenValue_example"; // String | 
try {
    ApiSafeResultCollectTokenResponse result = apiInstance.apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost(tokenValue);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGamePlayerParticipationCollectTokenSafePost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tokenValue** | **String**|  | [optional]

### Return type

[**ApiSafeResultCollectTokenResponse**](ApiSafeResultCollectTokenResponse.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet"></a>
# **apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet**
> List&lt;PlayerCollectionEntry&gt; apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    List<PlayerCollectionEntry> result = apiInstance.apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGamePlayerParticipationCollectionEntriesGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;PlayerCollectionEntry&gt;**](PlayerCollectionEntry.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGamePlayerParticipationGet"></a>
# **apiFursuitsCollectingGamePlayerParticipationGet**
> PlayerParticipationInfo apiFursuitsCollectingGamePlayerParticipationGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    PlayerParticipationInfo result = apiInstance.apiFursuitsCollectingGamePlayerParticipationGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGamePlayerParticipationGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**PlayerParticipationInfo**](PlayerParticipationInfo.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGamePlayerParticipationScoreboardGet"></a>
# **apiFursuitsCollectingGamePlayerParticipationScoreboardGet**
> List&lt;PlayerScoreboardEntry&gt; apiFursuitsCollectingGamePlayerParticipationScoreboardGet(top)



### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
Integer top = 25; // Integer | 
try {
    List<PlayerScoreboardEntry> result = apiInstance.apiFursuitsCollectingGamePlayerParticipationScoreboardGet(top);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGamePlayerParticipationScoreboardGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **top** | **Integer**|  | [optional] [default to 25]

### Return type

[**List&lt;PlayerScoreboardEntry&gt;**](PlayerScoreboardEntry.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGameRecalculatePost"></a>
# **apiFursuitsCollectingGameRecalculatePost**
> apiFursuitsCollectingGameRecalculatePost()



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
try {
    apiInstance.apiFursuitsCollectingGameRecalculatePost();
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameRecalculatePost");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGameTokensBatchPost"></a>
# **apiFursuitsCollectingGameTokensBatchPost**
> apiFursuitsCollectingGameTokensBatchPost(tokenValues)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
List<String> tokenValues = Arrays.asList(new List<String>()); // List<String> | 
try {
    apiInstance.apiFursuitsCollectingGameTokensBatchPost(tokenValues);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameTokensBatchPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tokenValues** | **List&lt;String&gt;**|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiFursuitsCollectingGameTokensPost"></a>
# **apiFursuitsCollectingGameTokensPost**
> apiFursuitsCollectingGameTokensPost(tokenValue)



  * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**, **&#x60;System&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.FursuitsApi;

FursuitsApi apiInstance = new FursuitsApi();
String tokenValue = "tokenValue_example"; // String | 
try {
    apiInstance.apiFursuitsCollectingGameTokensPost(tokenValue);
} catch (ApiException e) {
    System.err.println("Exception when calling FursuitsApi#apiFursuitsCollectingGameTokensPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tokenValue** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

