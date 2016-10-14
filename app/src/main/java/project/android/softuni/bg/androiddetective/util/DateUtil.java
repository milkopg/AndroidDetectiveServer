package project.android.softuni.bg.androiddetective.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Milko on 30.9.2016 г..
 */

public class DateUtil {

  private static final String TAG = DateUtil.class.getSimpleName();

  /**
   * Convert Long date to short String format : yyyy-MM-dd HH:mm
   * @param date Long date
   * @return String in format yyyy-MM-dd HH:mm
   */
  public static String convertDateToShortString(Date date) {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT_DATE_TIME);
    return dateFormat.format(date);
  }

  /**
   * Conver LongDate to Short Date format yyyy-MM-dd HH:mm
   * @param date Long Date
   * @return Short Date format : yyyy-MM-dd HH:mm
   */
  public static Date convertDateLongToShortDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT_DATE_TIME);
    String dateString = convertDateToShortString(date);
    try {
      return dateFormat.parse(dateString);
    } catch (ParseException e) {
      Log.e(TAG, "convertDateLongToShortDate: "  + e);
    }
    return date;
  }
}
