package project.android.softuni.bg.androiddetective.util;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;


/**
 * Created by Milko on 24.9.2016 г..
 */

public class GsonManager {
  private static final String TAG = GsonManager.class.getSimpleName();

  public static String convertObjectToGsonString(ResponseObject data) {
    Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_SHORT_DATE_TIME).create();
    return gson.toJson(data);
  }

  public static String convertObjectMapToGsonString (ConcurrentHashMap<String, ResponseObject> objectBaseMap) {
    Gson gson = new Gson();
    return gson.toJson(objectBaseMap);
  }

  public static ResponseObject convertGsonStringToObject(String json) {
    Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_SHORT_DATE_TIME).create();
    ResponseObject data = null;
    try {
      data = gson.fromJson(json, ResponseObject.class);
    } catch (JsonSyntaxException e) {
      Log.e(TAG, "convertGsonStringToObject: " + e);
    }
     return data;
  }

  public static ConcurrentHashMap<String, ResponseObject> convertGsonStringToObjectMap(String json) {
    Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_SHORT_DATE_TIME).create();
    ConcurrentHashMap<String, ResponseObject>  objectMap = new ConcurrentHashMap<>();
    try {
      objectMap = gson.fromJson(json, objectMap.getClass());
    } catch (JsonSyntaxException e) {
      Log.e(TAG, "convertGsonStringToObjectMap: " + e);
    }
    return objectMap;
  }

  public static final Gson customGson = new GsonBuilder().registerTypeHierarchyAdapter(byte[].class,
          new ByteArrayToBase64TypeAdapter()).create();


  // Using Android's base64 libraries. This can be replaced with any base64 library.
  private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
    public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return Base64.decode(json.getAsString(), Base64.NO_WRAP);
    }

    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
      return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
    }

  }
}
