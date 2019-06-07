# TokensApi

All URIs are relative to *https://localhost/EF25*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiTokensRegSysPost**](TokensApi.md#apiTokensRegSysPost) | **POST** /Api/Tokens/RegSys | 
[**apiTokensWhoAmIGet**](TokensApi.md#apiTokensWhoAmIGet) | **GET** /Api/Tokens/WhoAmI | 


<a name="apiTokensRegSysPost"></a>
# **apiTokensRegSysPost**
> AuthenticationResponse apiTokensRegSysPost(request)



### Example
```java
// Import classes:
//import io.swagger.client.api.TokensApi;

TokensApi apiInstance = new TokensApi();
RegSysAuthenticationRequest request = new RegSysAuthenticationRequest(); // RegSysAuthenticationRequest | 
try {
    AuthenticationResponse result = apiInstance.apiTokensRegSysPost(request);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#apiTokensRegSysPost");
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

<a name="apiTokensWhoAmIGet"></a>
# **apiTokensWhoAmIGet**
> AuthenticationResponse apiTokensWhoAmIGet()



  * Requires authorization   

### Example
```java
// Import classes:
//import io.swagger.client.api.TokensApi;

TokensApi apiInstance = new TokensApi();
try {
    AuthenticationResponse result = apiInstance.apiTokensWhoAmIGet();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TokensApi#apiTokensWhoAmIGet");
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

