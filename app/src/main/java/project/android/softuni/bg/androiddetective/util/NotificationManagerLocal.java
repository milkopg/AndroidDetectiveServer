package project.android.softuni.bg.androiddetective.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import project.android.softuni.bg.androiddetective.MainActivity;
import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 1.10.2016 г..
 */

public class NotificationManagerLocal {
  private static Context mContext;
  private static NotificationManagerLocal instance;
  private static int number = 0;

  /**
   * private constructor required for singleTon
   */
  private NotificationManagerLocal() {
  }

  /**
   * ShowNotification. It recognize BroadcastName and it send data for openning appropriate FragmentScreen
   * @param responseObject
   */
  public void showNotification(ResponseObject responseObject) {
    if (responseObject == null) return;;
    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
    mBuilder.setContentTitle("Android detective: " + responseObject.getBroadcastName());
    mBuilder.setContentText(responseObject.getSendTo() + ": " + responseObject.getSendText());
    mBuilder.setSmallIcon(R.mipmap.ic_launcher);
    mBuilder.setAutoCancel(true);
    Intent notificationIntent = new Intent(mContext, MainActivity.class);
    if (responseObject.getContacts() != null) {
      notificationIntent.putExtra(Constants.BROADCAST_NAME, Constants.RECEIVER_CONTACTS);
    } else {
      notificationIntent.putExtra(Constants.BROADCAST_NAME, responseObject.getBroadcastName());
    }

    TaskStackBuilder tsb = TaskStackBuilder.create(mContext);
    tsb.addParentStack(MainActivity.class);
    tsb.addNextIntent(notificationIntent);

    PendingIntent notificationPI = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(notificationPI);

    final android.app.NotificationManager notificationManager = (android.app.NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(number, mBuilder.build());
    number++;
  }

  /**
   * getInstance of NotificationManagerLocal
   * @param context
   * @return NotificationManagerLocal
   */
  public static NotificationManagerLocal getInstance(Context context) {
    mContext = context;
    if (instance == null)
      instance = new NotificationManagerLocal();
    return instance;
  }
}
