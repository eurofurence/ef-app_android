package io.swagger.client.api;

import io.swagger.client.ApiInvoker;
import io.swagger.client.ApiException;
import io.swagger.client.Pair;

import io.swagger.client.model.*;

import java.util.*;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.UUID;
import java.util.Date;
import io.swagger.client.model.PrivateMessageRecord;
import io.swagger.client.model.SendPrivateMessageRequest;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class CommunicationApi {
  String basePath = "https://localhost/";
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
  * Marks a given private message as read (reading receipt).
  *   * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  Calling this on a message that has already been marked as read  will not update the &#x60;ReadDateTimeUtc&#x60; property, but return the  &#x60;ReadDateTimeUtc&#x60; value of the first call.
   * @param messageId &#x60;Id&#x60; of the message to mark as read
   * @return Date
  */
  public Date apiV2CommunicationPrivateMessagesByMessageIdReadPost (UUID messageId) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
     Object postBody = null;
  
      // verify the required parameter 'messageId' is set
      if (messageId == null) {
      VolleyError error = new VolleyError("Missing the required parameter 'messageId' when calling apiV2CommunicationPrivateMessagesByMessageIdReadPost",
      new ApiException(400, "Missing the required parameter 'messageId' when calling apiV2CommunicationPrivateMessagesByMessageIdReadPost"));
      }
  

  // create path and map variables
  String path = "/Api/v2/Communication/PrivateMessages/{MessageId}/Read".replaceAll("\\{format\\}","json").replaceAll("\\{" + "MessageId" + "\\}", apiInvoker.escapeString(messageId.toString()));

  // query params
  List<Pair> queryParams = new ArrayList<Pair>();
      // header params
      Map<String, String> headerParams = new HashMap<String, String>();
      // form params
      Map<String, String> formParams = new HashMap<String, String>();



      String[] contentTypes = {
  
      };
      String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

      if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
  

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
      } else {
      // normal form params
        }

      String[] authNames = new String[] { "Bearer" };

      try {
        String localVarResponse = apiInvoker.invokeAPI (basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, authNames);
        if(localVarResponse != null){
           return (Date) ApiInvoker.deserialize(localVarResponse, "", Date.class);
        } else {
           return null;
        }
      } catch (ApiException ex) {
         throw ex;
      } catch (InterruptedException ex) {
         throw ex;
      } catch (ExecutionException ex) {
         if(ex.getCause() instanceof VolleyError) {
	    VolleyError volleyError = (VolleyError)ex.getCause();
	    if (volleyError.networkResponse != null) {
	       throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
	    }
         }
         throw ex;
      } catch (TimeoutException ex) {
         throw ex;
      }
  }

      /**
   * Marks a given private message as read (reading receipt).
   *   * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  Calling this on a message that has already been marked as read  will not update the &#x60;ReadDateTimeUtc&#x60; property, but return the  &#x60;ReadDateTimeUtc&#x60; value of the first call.
   * @param messageId &#x60;Id&#x60; of the message to mark as read
  */
  public void apiV2CommunicationPrivateMessagesByMessageIdReadPost (UUID messageId, final Response.Listener<Date> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

  
    // verify the required parameter 'messageId' is set
    if (messageId == null) {
       VolleyError error = new VolleyError("Missing the required parameter 'messageId' when calling apiV2CommunicationPrivateMessagesByMessageIdReadPost",
         new ApiException(400, "Missing the required parameter 'messageId' when calling apiV2CommunicationPrivateMessagesByMessageIdReadPost"));
    }
    

    // create path and map variables
    String path = "/Api/v2/Communication/PrivateMessages/{MessageId}/Read".replaceAll("\\{format\\}","json").replaceAll("\\{" + "MessageId" + "\\}", apiInvoker.escapeString(messageId.toString()));

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

      String[] authNames = new String[] { "Bearer" };

    try {
      apiInvoker.invokeAPI(basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((Date) ApiInvoker.deserialize(localVarResponse,  "", Date.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
  /**
  * Retrieves all private messages of an authenticated attendee.
  *   * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  This will set the &#x60;ReceivedDateTimeUtc&#x60; to the current server time on all messages retrieved  that have not been retrieved in a previous call.
   * @return List<PrivateMessageRecord>
  */
  public List<PrivateMessageRecord> apiV2CommunicationPrivateMessagesGet () throws TimeoutException, ExecutionException, InterruptedException, ApiException {
     Object postBody = null;
  

  // create path and map variables
  String path = "/Api/v2/Communication/PrivateMessages".replaceAll("\\{format\\}","json");

  // query params
  List<Pair> queryParams = new ArrayList<Pair>();
      // header params
      Map<String, String> headerParams = new HashMap<String, String>();
      // form params
      Map<String, String> formParams = new HashMap<String, String>();



      String[] contentTypes = {
  
      };
      String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

      if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
  

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
      } else {
      // normal form params
        }

      String[] authNames = new String[] { "Bearer" };

      try {
        String localVarResponse = apiInvoker.invokeAPI (basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames);
        if(localVarResponse != null){
           return (List<PrivateMessageRecord>) ApiInvoker.deserialize(localVarResponse, "array", PrivateMessageRecord.class);
        } else {
           return null;
        }
      } catch (ApiException ex) {
         throw ex;
      } catch (InterruptedException ex) {
         throw ex;
      } catch (ExecutionException ex) {
         if(ex.getCause() instanceof VolleyError) {
	    VolleyError volleyError = (VolleyError)ex.getCause();
	    if (volleyError.networkResponse != null) {
	       throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
	    }
         }
         throw ex;
      } catch (TimeoutException ex) {
         throw ex;
      }
  }

      /**
   * Retrieves all private messages of an authenticated attendee.
   *   * Requires authorization     * Requires any of the following roles: **&#x60;Attendee&#x60;**  This will set the &#x60;ReceivedDateTimeUtc&#x60; to the current server time on all messages retrieved  that have not been retrieved in a previous call.

  */
  public void apiV2CommunicationPrivateMessagesGet (final Response.Listener<List<PrivateMessageRecord>> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = null;

  

    // create path and map variables
    String path = "/Api/v2/Communication/PrivateMessages".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

      String[] authNames = new String[] { "Bearer" };

    try {
      apiInvoker.invokeAPI(basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((List<PrivateMessageRecord>) ApiInvoker.deserialize(localVarResponse,  "array", PrivateMessageRecord.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
  /**
  * Sends a private message to a specific recipient/attendee.
  *   * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**  If the backend has a push-channel available to any given device(s) that are currently signed into the app  with the same recipient uid, it will push a toast message to those devices.  The toast message content is defined by the &#x60;ToastTitle&#x60; and &#x60;ToastMessage&#x60; properties.
   * @param request 
   * @return UUID
  */
  public UUID apiV2CommunicationPrivateMessagesPost (SendPrivateMessageRequest request) throws TimeoutException, ExecutionException, InterruptedException, ApiException {
     Object postBody = request;
  

  // create path and map variables
  String path = "/Api/v2/Communication/PrivateMessages".replaceAll("\\{format\\}","json");

  // query params
  List<Pair> queryParams = new ArrayList<Pair>();
      // header params
      Map<String, String> headerParams = new HashMap<String, String>();
      // form params
      Map<String, String> formParams = new HashMap<String, String>();



      String[] contentTypes = {
  "application/json","text/json","application/json-patch+json"
      };
      String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

      if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
  

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
      } else {
      // normal form params
        }

      String[] authNames = new String[] { "Bearer" };

      try {
        String localVarResponse = apiInvoker.invokeAPI (basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, authNames);
        if(localVarResponse != null){
           return (UUID) ApiInvoker.deserialize(localVarResponse, "", UUID.class);
        } else {
           return null;
        }
      } catch (ApiException ex) {
         throw ex;
      } catch (InterruptedException ex) {
         throw ex;
      } catch (ExecutionException ex) {
         if(ex.getCause() instanceof VolleyError) {
	    VolleyError volleyError = (VolleyError)ex.getCause();
	    if (volleyError.networkResponse != null) {
	       throw new ApiException(volleyError.networkResponse.statusCode, volleyError.getMessage());
	    }
         }
         throw ex;
      } catch (TimeoutException ex) {
         throw ex;
      }
  }

      /**
   * Sends a private message to a specific recipient/attendee.
   *   * Requires authorization     * Requires any of the following roles: **&#x60;Developer&#x60;**  If the backend has a push-channel available to any given device(s) that are currently signed into the app  with the same recipient uid, it will push a toast message to those devices.  The toast message content is defined by the &#x60;ToastTitle&#x60; and &#x60;ToastMessage&#x60; properties.
   * @param request 
  */
  public void apiV2CommunicationPrivateMessagesPost (SendPrivateMessageRequest request, final Response.Listener<UUID> responseListener, final Response.ErrorListener errorListener) {
    Object postBody = request;

  

    // create path and map variables
    String path = "/Api/v2/Communication/PrivateMessages".replaceAll("\\{format\\}","json");

    // query params
    List<Pair> queryParams = new ArrayList<Pair>();
    // header params
    Map<String, String> headerParams = new HashMap<String, String>();
    // form params
    Map<String, String> formParams = new HashMap<String, String>();



    String[] contentTypes = {
      "application/json","text/json","application/json-patch+json"
    };
    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

    if (contentType.startsWith("multipart/form-data")) {
      // file uploading
      MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
      

      HttpEntity httpEntity = localVarBuilder.build();
      postBody = httpEntity;
    } else {
      // normal form params
          }

      String[] authNames = new String[] { "Bearer" };

    try {
      apiInvoker.invokeAPI(basePath, path, "POST", queryParams, postBody, headerParams, formParams, contentType, authNames,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String localVarResponse) {
            try {
              responseListener.onResponse((UUID) ApiInvoker.deserialize(localVarResponse,  "", UUID.class));
            } catch (ApiException exception) {
               errorListener.onErrorResponse(new VolleyError(exception));
            }
          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            errorListener.onErrorResponse(error);
          }
      });
    } catch (ApiException ex) {
      errorListener.onErrorResponse(new VolleyError(ex));
    }
  }
}
