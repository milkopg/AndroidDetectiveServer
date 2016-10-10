package project.android.softuni.bg.androiddetective.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import project.android.softuni.bg.androiddetective.service.DetectiveServerService;

/**
 * Created by Milko on 22.9.2016 г..
 */

public class ServiceConnectionManager {
  private static ServiceConnection mConnection;
  private static final String TAG = "ServiceConnectionManage";

  private ServiceConnectionManager(){};
  public static ServiceConnection getInstance() {
    if (mConnection == null) {
      mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
          Log.d(TAG, "OnServiceDisconnected: " + componentName);
        }
      };
    }
    return mConnection;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return null;
  }

  public static void startService(Context context) {
    Intent service = new Intent(context, DetectiveServerService.class);
    ServiceConnection mConnection = ServiceConnectionManager.getInstance();
    if (isServiceRunning(context, DetectiveServerService.class.getSimpleName()))
      context.unbindService(mConnection);
    context.bindService(service, mConnection, Context.BIND_AUTO_CREATE);
    context.startService(service);
  }

  public static boolean isServiceRunning(Context ctx, String serviceClassName) {
    ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
      if (DetectiveServerService.class.getName().equals(serviceClassName)) {
        return true;
      }
    }
    return false;
  }
}
