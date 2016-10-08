package project.android.softuni.bg.androiddetective.util;

import java.util.HashMap;

import project.android.softuni.bg.androiddetective.webapi.model.Sorter;

/**
 * Created by Milko on 8.10.2016 г..
 */

public class QueriesUtil {
  private static HashMap<String, Sorter> sorterMap = new HashMap<>();

  public static boolean getAscOrDescSorting(String columnName) {
    boolean ascending = true;
    if (!sorterMap.containsKey(columnName)) {
      sorterMap.put(columnName, new Sorter(columnName, ascending));
    } else {
      ascending = sorterMap.get(columnName).isAsc();
      ascending = !ascending;
      sorterMap.remove(columnName);
      sorterMap.put(columnName, new Sorter(columnName, ascending));
    }
    return ascending;
  }
}
