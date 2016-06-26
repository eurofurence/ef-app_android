package io.swagger.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import io.swagger.client.model.*;

public class JsonUtil {
  public static GsonBuilder gsonBuilder;

  static {
    gsonBuilder = new GsonBuilder();
    gsonBuilder.serializeNulls();
    gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
  }

  public static Gson getGson() {
    return gsonBuilder.create();
  }

  public static String serialize(Object obj){
    return getGson().toJson(obj);
  }

  public static <T> T deserializeToList(String jsonString, Class cls){
    return getGson().fromJson(jsonString, getListTypeForDeserialization(cls));
  }

  public static <T> T deserializeToObject(String jsonString, Class cls){
    return getGson().fromJson(jsonString, getTypeForDeserialization(cls));
  }

  public static Type getListTypeForDeserialization(Class cls) {
    String className = cls.getSimpleName();
    
    if ("Announcement".equalsIgnoreCase(className)) {
      return new TypeToken<List<Announcement>>(){}.getType();
    }
    
    if ("Dealer".equalsIgnoreCase(className)) {
      return new TypeToken<List<Dealer>>(){}.getType();
    }
    
    if ("Endpoint".equalsIgnoreCase(className)) {
      return new TypeToken<List<Endpoint>>(){}.getType();
    }
    
    if ("EndpointConfiguration".equalsIgnoreCase(className)) {
      return new TypeToken<List<EndpointConfiguration>>(){}.getType();
    }
    
    if ("EndpointEntity".equalsIgnoreCase(className)) {
      return new TypeToken<List<EndpointEntity>>(){}.getType();
    }
    
    if ("EntityBase".equalsIgnoreCase(className)) {
      return new TypeToken<List<EntityBase>>(){}.getType();
    }
    
    if ("EventConferenceDay".equalsIgnoreCase(className)) {
      return new TypeToken<List<EventConferenceDay>>(){}.getType();
    }
    
    if ("EventConferenceRoom".equalsIgnoreCase(className)) {
      return new TypeToken<List<EventConferenceRoom>>(){}.getType();
    }
    
    if ("EventConferenceTrack".equalsIgnoreCase(className)) {
      return new TypeToken<List<EventConferenceTrack>>(){}.getType();
    }
    
    if ("EventEntry".equalsIgnoreCase(className)) {
      return new TypeToken<List<EventEntry>>(){}.getType();
    }
    
    if ("Image".equalsIgnoreCase(className)) {
      return new TypeToken<List<Image>>(){}.getType();
    }
    
    if ("Info".equalsIgnoreCase(className)) {
      return new TypeToken<List<Info>>(){}.getType();
    }
    
    if ("InfoGroup".equalsIgnoreCase(className)) {
      return new TypeToken<List<InfoGroup>>(){}.getType();
    }
    
    if ("NamedUrl".equalsIgnoreCase(className)) {
      return new TypeToken<List<NamedUrl>>(){}.getType();
    }
    
    return new TypeToken<List<Object>>(){}.getType();
  }

  public static Type getTypeForDeserialization(Class cls) {
    String className = cls.getSimpleName();
    
    if ("Announcement".equalsIgnoreCase(className)) {
      return new TypeToken<Announcement>(){}.getType();
    }
    
    if ("Dealer".equalsIgnoreCase(className)) {
      return new TypeToken<Dealer>(){}.getType();
    }
    
    if ("Endpoint".equalsIgnoreCase(className)) {
      return new TypeToken<Endpoint>(){}.getType();
    }
    
    if ("EndpointConfiguration".equalsIgnoreCase(className)) {
      return new TypeToken<EndpointConfiguration>(){}.getType();
    }
    
    if ("EndpointEntity".equalsIgnoreCase(className)) {
      return new TypeToken<EndpointEntity>(){}.getType();
    }
    
    if ("EntityBase".equalsIgnoreCase(className)) {
      return new TypeToken<EntityBase>(){}.getType();
    }
    
    if ("EventConferenceDay".equalsIgnoreCase(className)) {
      return new TypeToken<EventConferenceDay>(){}.getType();
    }
    
    if ("EventConferenceRoom".equalsIgnoreCase(className)) {
      return new TypeToken<EventConferenceRoom>(){}.getType();
    }
    
    if ("EventConferenceTrack".equalsIgnoreCase(className)) {
      return new TypeToken<EventConferenceTrack>(){}.getType();
    }
    
    if ("EventEntry".equalsIgnoreCase(className)) {
      return new TypeToken<EventEntry>(){}.getType();
    }
    
    if ("Image".equalsIgnoreCase(className)) {
      return new TypeToken<Image>(){}.getType();
    }
    
    if ("Info".equalsIgnoreCase(className)) {
      return new TypeToken<Info>(){}.getType();
    }
    
    if ("InfoGroup".equalsIgnoreCase(className)) {
      return new TypeToken<InfoGroup>(){}.getType();
    }
    
    if ("NamedUrl".equalsIgnoreCase(className)) {
      return new TypeToken<NamedUrl>(){}.getType();
    }
    
    return new TypeToken<Object>(){}.getType();
  }

};
