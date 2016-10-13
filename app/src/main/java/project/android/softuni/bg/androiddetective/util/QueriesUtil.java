package project.android.softuni.bg.androiddetective.util;

import android.support.v7.widget.RecyclerView;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.Contact;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;
import project.android.softuni.bg.androiddetective.webapi.model.Setting;
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

  public static String getDatabaseColumnNameByViewId(int viewId) {
    String columnName = null;
    switch (viewId) {
      case R.id.textViewContactName:
        columnName = Constants.COLUMN_CONTACT_NAME;
        break;
      case R.id.textViewContactPhoneNumber:
        columnName = Constants.COLUMN_CONTACT_PHONE_NUMBER;
        break;
      case R.id.textViewContactEmail:
        columnName = Constants.COLUMN_CONTACT_EMAIL;
        break;
      case R.id.textViewDateLabel:
        columnName = Constants.COLUMN_RESPONSE_OBJECT_DATE;
        break;
      case R.id.textViewSendToLabel:
        columnName = Constants.COLUMN_RESPONSE_OBJECT_SEND_TO;
        break;
      case R.id.textViewSendTextLabel:
        columnName = Constants.COLUMN_RESPONSE_OBJECT_SEND_TEXT;
        break;
    }
    return columnName;
  }

  public static List<ResponseObject> getResponseObjectByBroadcastNameAndImageName(String broacastName, String imageName) {
    return  ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + " LIKE '" + broacastName + "' AND " + Constants.IMAGE_NAME + " = '" + imageName + "'", null, null, "ID DESC", null);
  }

  public static List<ResponseObject> getResponseObjectByBroadcastName(String broacastName) {
    return  ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + " LIKE '" + broacastName + "'", null, null, "ID DESC", null);
  }

  public static void orderContactData(Class<Contact> contactClass, RecyclerView.Adapter mAdapter, List<Contact> mAdapterData, String columnName, String orderBy) {
    if (mAdapterData == null || mAdapter == null || columnName == null || orderBy == null) return;
    mAdapterData.clear();
    mAdapterData.addAll(Contact.find(contactClass, null, null, null, columnName + " " + orderBy, null));
    mAdapter.notifyDataSetChanged();
  }


  public static void orderTableData(Class<ResponseObject> responseClass, RecyclerView.Adapter mAdapter, List<ResponseObject> mAdapterData, String columnName, String orderByColumnName, Calendar fromCalendar, Calendar toCalendar) {
    if (mAdapterData == null || mAdapter == null || columnName == null || orderByColumnName == null || fromCalendar == null || toCalendar == null)
      return;
    boolean asc = QueriesUtil.getAscOrDescSorting(orderByColumnName);
    String orderBy = QueriesUtil.getOrderBy(asc);
    mAdapterData.clear();
    mAdapterData.addAll(ResponseObject.find(responseClass, Constants.BROADCAST_NAME + " LIKE '" + columnName + "' AND DATE > " + fromCalendar.getTime().getTime() + " AND  DATE < " + toCalendar.getTime().getTime(), null, null, orderByColumnName + " " + orderBy, null));
    mAdapter.notifyDataSetChanged();
  }

  public static String getOrderBy(boolean ascending) {
    return ascending ? Constants.ORDER_BY_ASC : Constants.ORDER_BY_DESC;
  }

  public static Setting getSettingByDatabaseKey(String databaseKey) {
    if (databaseKey == null) return null;
    List<Setting> settings = Setting.find(Setting.class, Constants.COLUMN_SETTING_RESOURCE_NAME + " LIKE '" + databaseKey + "'", null, null, null, null);
    if (settings != null && settings.size() > 0) {
      return settings.get(0);
    } else {
      return null;
    }
  }

  public static String getNotNullValue(String object, String defaultValue) {
    return  object == null ? defaultValue : object;
  }

}
