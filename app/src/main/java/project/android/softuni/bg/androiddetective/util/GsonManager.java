package project.android.softuni.bg.androiddetective.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.concurrent.ConcurrentHashMap;

import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;


/**
 * Created by Milko on 24.9.2016 г..
 */

public class GsonManager {
  private static final String TAG = GsonManager.class.getSimpleName();

  public static String convertObjectToGsonString(ResponseBase data) {
    Gson gson = new Gson();
    return gson.toJson(data);
  }

  public static String convertObjectMapToGsonString (ConcurrentHashMap<String, ResponseBase> objectBaseMap) {
    Gson gson = new Gson();
    return gson.toJson(objectBaseMap);
  }

  public static ResponseObject convertGsonStringToObject(String json) {
    Gson gson = new Gson();
    ResponseObject data = null;
    try {
      data = gson.fromJson(json, ResponseObject.class);
    } catch (JsonSyntaxException e) {
      Log.e(TAG, "convertGsonStringToObject: " + e.getLocalizedMessage());
    }
     return data;
  }

  public static ConcurrentHashMap<String, ResponseObject> convertGsonStringToObjectMap(String json) {
    Gson gson = new Gson();
    ConcurrentHashMap<String, ResponseObject>  objectMap = new ConcurrentHashMap<>();
    try {
      objectMap = gson.fromJson(json, objectMap.getClass());
    } catch (JsonSyntaxException e) {
      Log.e(TAG, "convertGsonStringToObjectMap: " + e.getLocalizedMessage());
    }
    return objectMap;
  }
}