package io.swagger.client.api;

import com.sun.jersey.api.client.GenericType;

import io.swagger.client.ApiException;
import io.swagger.client.ApiClient;
import io.swagger.client.Configuration;
import io.swagger.client.Pair;

import io.swagger.client.model.Endpoint;
import java.util.Date;
import io.swagger.client.model.EventConferenceDay;
import io.swagger.client.model.EventConferenceRoom;
import io.swagger.client.model.EventConferenceTrack;
import io.swagger.client.model.EventEntry;
import io.swagger.client.model.Image;
import io.swagger.client.model.Info;
import io.swagger.client.model.InfoGroup;

import java.util.*;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.JavaClientCodegen", date = "2016-02-24T14:49:45.074+01:00")
public class DefaultApi {
  private ApiClient apiClient;

  public DefaultApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DefaultApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  
  /**
   * 
   * Gets metadata information about the API Endpoint.
   * @return Endpoint
   */
  public Endpoint endpointGet() throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/Endpoint".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<Endpoint> returnType = new GenericType<Endpoint>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * Retrieves a list of all event conference days.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventConferenceDay>
   */
  public List<EventConferenceDay> eventConferenceDayGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/EventConferenceDay".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<EventConferenceDay>> returnType = new GenericType<List<EventConferenceDay>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * Retrieves a list of all conference rooms.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventConferenceRoom>
   */
  public List<EventConferenceRoom> eventConferenceRoomGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/EventConferenceRoom".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<EventConferenceRoom>> returnType = new GenericType<List<EventConferenceRoom>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * Retrieves a list of all event conference tracks.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventConferenceTrack>
   */
  public List<EventConferenceTrack> eventConferenceTrackGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/EventConferenceTrack".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<EventConferenceTrack>> returnType = new GenericType<List<EventConferenceTrack>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * Retrieves a list of all events in the event schedule.
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<EventEntry>
   */
  public List<EventEntry> eventEntryGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/EventEntry".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<EventEntry>> returnType = new GenericType<List<EventEntry>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * tbd
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<Image>
   */
  public List<Image> imageGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/Image".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<Image>> returnType = new GenericType<List<Image>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * tbd
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<Info>
   */
  public List<Info> infoGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/Info".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<Info>> returnType = new GenericType<List<Info>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
  /**
   * 
   * tbd
   * @param since Delta reference, date time in **ISO 8610**. If set, only items with a *LastChangeDateTimeUtc* &gt;= the specified value will be returned. If not set, API will return the current set of records without deleted items. If set, items deleted since the delta specified will be returned with an *IsDeleted* flag set.
   * @return List<InfoGroup>
   */
  public List<InfoGroup> infoGroupGet(Date since) throws ApiException {
    Object postBody = null;
    
    // create path and map variables
    String path = "/InfoGroup".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    Map<String, String> headerParams = new HashMap<String, String>();
    Map<String, Object> formParams = new HashMap<String, Object>();

    
    queryParams.addAll(apiClient.parameterToPairs("", "since", since));
    

    

    

    final String[] accepts = {
      "application/json"
    };
    final String accept = apiClient.selectHeaderAccept(accepts);

    final String[] contentTypes = {
      "application/json"
    };
    final String contentType = apiClient.selectHeaderContentType(contentTypes);

    String[] authNames = new String[] {  };

    
    GenericType<List<InfoGroup>> returnType = new GenericType<List<InfoGroup>>() {};
    return apiClient.invokeAPI(path, "GET", queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    
  }
  
}
