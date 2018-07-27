# TokensApi

All URIs are relative to *https://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV2TokensRegSysPost**](TokensApi.md#apiV2TokensRegSysPost) | **POST** /Api/v2/Tokens/RegSys | 
[**apiV2TokensWhoAmIGet**](TokensApi.md#apiV2TokensWhoAmIGet) | **GET** /Api/v2/Tokens/WhoAmI | 


<a name="apiV2TokensRegSysPost"></a>
# **apiV2TokensRegSysPost**
> AuthenticationResponse apiV2TokensRegSysPost(request)



### Example
```java
// Import classes:
//import io.swagger.client.api.TokensApi;

TokensApi apiInstance = new TokensApi();
RegSysAuthenticationRequest request = new RegSysAuthenticationRequest(); // RegSysAuthenticationRequest | 
try {
    AuthenticationResponse result = apiInstance.apiV2TokensRegSysPost(request);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#apiV2TokensRegSysPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**RegSysAuthenticationRequest**](RegSysAuthenticationRequest.md)|  | [optional]

### Return type

[**AuthenticationResponse**](AuthenticationResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json-patch+json, application/json, text/json, application/*+json
 - **Accept**: text/plain, application/json, text/json

<a name="apiV2TokensWhoAmIGet"></a>
# **apiV2TokensWhoAmIGet**
> AuthenticationResponse apiV2TokensWhoAmIGet()



  * Requires authorization   

### Example
```java
// Import classes:
//import io.swagger.client.api.TokensApi;

TokensApi apiInstance = new TokensApi();
try {
    AuthenticationResponse result = apiInstance.apiV2TokensWhoAmIGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#apiV2TokensWhoAmIGet");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**AuthenticationResponse**](AuthenticationResponse.md)

### Authorization

[Bearer](../README.md#Bearer)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: text/plain, application/json, text/json

