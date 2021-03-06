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

  /**
   * Convert RegularObject to Gson String with specific date short format yyyy-MM-dd HH:mm
   * @param data
   * @return convert gson string
   */
  public static String convertObjectToGsonString(ResponseObject data) {
    Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_SHORT_DATE_TIME).create();
    return gson.toJson(data);
  }

  /**
   * ConvertGsonString to Object.
   * @param json
   * @return serialized GsonObject
   */
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
}
