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

  private NotificationManagerLocal() {
  }

  public void showNotification(ResponseObject responseObject) {
    if (responseObject == null) return;;
    final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
    mBuilder.setContentTitle("Android detective: " + responseObject.broadcastName);
    mBuilder.setContentText(responseObject.sendTo + ": " + responseObject.sendText);
    mBuilder.setSmallIcon(R.mipmap.ic_launcher);
    mBuilder.setAutoCancel(true);
    Intent notificationIntent = new Intent(mContext, MainActivity.class);
    notificationIntent.putExtra(Constants.BROADCAST_NAME, responseObject.broadcastName);

    TaskStackBuilder tsb = TaskStackBuilder.create(mContext);
    tsb.addParentStack(MainActivity.class);
    tsb.addNextIntent(notificationIntent);

    PendingIntent notificationPI = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    mBuilder.setContentIntent(notificationPI);

    final android.app.NotificationManager notificationManager = (android.app.NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.notify(number, mBuilder.build());
    number++;
  }

  public static NotificationManagerLocal getInstance(Context context) {
    mContext = context;
    if (instance == null)
      instance = new NotificationManagerLocal();
    return instance;
  }
}
