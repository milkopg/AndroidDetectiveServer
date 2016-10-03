package project.android.softuni.bg.androiddetective.webapi.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Milko on 24.9.2016 г..
 */

public class ResponseBase {
  private static ConcurrentHashMap<String, ResponseObject> dataMap = new ConcurrentHashMap<>();

  public static ConcurrentHashMap<String, ResponseObject> getDataMap() {
    if (dataMap == null) dataMap = new ConcurrentHashMap<>();
    return dataMap;
  }

  public static ResponseObject putIfAbsent(String key, ResponseObject value) {
    if (key != null) return null;
    return dataMap.putIfAbsent(key, value);
  }

  public static ResponseObject getObject(String key) {
    if (key != null) return null;
    return dataMap.get(key) ;
  }

}
