package io.swagger.client.api;

import io.swagger.client.ApiException;
import io.swagger.client.ApiInvoker;
import io.swagger.client.Pair;

import io.swagger.client.model.*;

import java.util.*;

import io.swagger.client.model.Endpoint;
import io.swagger.client.model.EventConferenceDay;
import java.util.Date;
import io.swagger.client.model.EventConferenceRoom;
import io.swagger.client.model.EventConferenceTrack;
import io.swagger.client.model.EventEntry;
import io.swagger.client.model.Image;
import io.swagger.client.model.Info;
import io.swagger.client.model.InfoGroup;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

public class DefaultApi {
  String basePath = "https://eurofurencewebapi.azurewebsites.net";
  ApiInvoker apiInvoker = ApiInvoker.getInstance();

  public void addHeader(String key, String value) {
    getInvoker().addDefaultHeader(key, value);
  }

  public ApiInvoker getInvoker() {
    return apiInvoker;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getBasePath() {
    return basePath;
  }

  
  /**
   * 
   * Gets metadata information about the API Endpoint.
   * @return Endpoint
   */
  public Endpoint  endpointGet () throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/Endpoint".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (Endpoint) ApiInvoker.deserialize(localVarResponse, "", Endpoint.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * Retrieves a list of all event conference days.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventConferenceDay>
   */
  public List<EventConferenceDay>  eventConferenceDayGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/EventConferenceDay".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<EventConferenceDay>) ApiInvoker.deserialize(localVarResponse, "array", EventConferenceDay.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * Retrieves a list of all conference rooms.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventConferenceRoom>
   */
  public List<EventConferenceRoom>  eventConferenceRoomGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/EventConferenceRoom".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<EventConferenceRoom>) ApiInvoker.deserialize(localVarResponse, "array", EventConferenceRoom.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * Retrieves a list of all event conference tracks.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventConferenceTrack>
   */
  public List<EventConferenceTrack>  eventConferenceTrackGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/EventConferenceTrack".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<EventConferenceTrack>) ApiInvoker.deserialize(localVarResponse, "array", EventConferenceTrack.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * Retrieves a list of all events in the event schedule.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventEntry>
   */
  public List<EventEntry>  eventEntryGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/EventEntry".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<EventEntry>) ApiInvoker.deserialize(localVarResponse, "array", EventEntry.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * tbd
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<Image>
   */
  public List<Image>  imageGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/Image".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<Image>) ApiInvoker.deserialize(localVarResponse, "array", Image.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * tbd
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<Info>
   */
  public List<Info>  infoGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/Info".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<Info>) ApiInvoker.deserialize(localVarResponse, "array", Info.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
  /**
   * 
   * tbd
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<InfoGroup>
   */
  public List<InfoGroup>  infoGroupGet (Date since) throws ApiException {
    Object localVarPostBody = null;
    

    // create path and map variables
    String localVarPath = "/InfoGroup".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    // form params
    Map<String, String> localVarFormParams = new HashMap<String, String>();

    
    localVarQueryParams.addAll(ApiInvoker.parameterToPairs("", "since", since));
    

    

    String[] localVarContentTypes = {
      "application/json"
    };
    String localVarContentType = localVarContentTypes.length > 0 ? localVarContentTypes[0] : "application/json";

    if (localVarContentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      localVarPostBody = localVarBuilder.build();
    } else {
      // normal form params
      
    }

    try {
      String localVarResponse = apiInvoker.invokeAPI(basePath, localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarContentType);
      if(localVarResponse != null){
        return (List<InfoGroup>) ApiInvoker.deserialize(localVarResponse, "array", InfoGroup.class);
      }
      else {
        return null;
      }
    } catch (ApiException ex) {
      throw ex;
    }
  }
  
}
