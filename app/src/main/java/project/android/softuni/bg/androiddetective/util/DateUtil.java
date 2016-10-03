package project.android.softuni.bg.androiddetective.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Milko on 30.9.2016 г..
 */

public class DateUtil {

  private static final String TAG = DateUtil.class.getSimpleName();

  public static Date convertStringToGMTDate(String dateString) {
    DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date inputDate = null;
    try {
      inputDate = dateFormat.parse(dateString);
    } catch (ParseException e) {
      Log.e(TAG, "convertStringToGMTDate: cannot convert String to date: " + e);
    }
    return inputDate;
  }

  public static String convertDateToShortString(Date date) {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    return dateFormat.format(date);
  }

  public static Date convertDateLongToShortDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_SHORT);
    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    String dateString = convertDateToShortString(date);
    try {
      return dateFormat.parse(dateString);
    } catch (ParseException e) {
      Log.e(TAG, "convertDateLongToShortDate: "  + e);
    }
    return date;
  }
}
