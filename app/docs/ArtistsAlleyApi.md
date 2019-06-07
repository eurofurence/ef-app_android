# ArtistsAlleyApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiArtistsAlleyTableRegistrationMyLatestGet**](ArtistsAlleyApi.md#apiArtistsAlleyTableRegistrationMyLatestGet) | **GET** /Api/ArtistsAlley/TableRegistration/:my-latest | 
[**apiArtistsAlleyTableRegistrationRequestPost**](ArtistsAlleyApi.md#apiArtistsAlleyTableRegistrationRequestPost) | **POST** /Api/ArtistsAlley/TableRegistrationRequest | 


<a name="apiArtistsAlleyTableRegistrationMyLatestGet"></a>
# **apiArtistsAlleyTableRegistrationMyLatestGet**
> TableRegistrationRecord apiArtistsAlleyTableRegistrationMyLatestGet()



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ArtistsAlleyApi;

ArtistsAlleyApi apiInstance = new ArtistsAlleyApi();
try {
    TableRegistrationRecord result = apiInstance.apiArtistsAlleyTableRegistrationMyLatestGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ArtistsAlleyApi#apiArtistsAlleyTableRegistrationMyLatestGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**TableRegistrationRecord**](TableRegistrationRecord.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

<a name="apiArtistsAlleyTableRegistrationRequestPost"></a>
# **apiArtistsAlleyTableRegistrationRequestPost**
> apiArtistsAlleyTableRegistrationRequestPost(request)



  * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**

### Example
```java
// Import classes:
//import io.swagger.client.api.ArtistsAlleyApi;

ArtistsAlleyApi apiInstance = new ArtistsAlleyApi();
TableRegistrationRequest request = new TableRegistrationRequest(); // TableRegistrationRequest | 
try {
    apiInstance.apiArtistsAlleyTableRegistrationRequestPost(request);
} catch (ApiException e) {
    System.err.println("Exception when calling ArtistsAlleyApi#apiArtistsAlleyTableRegistrationRequestPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**TableRegistrationRequest**](TableRegistrationRequest.md)|  | [optional]

### Return type

null (empty response body)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: Not defined

