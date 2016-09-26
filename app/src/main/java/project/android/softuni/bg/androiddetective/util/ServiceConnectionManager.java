package project.android.softuni.bg.androiddetective.util;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import project.android.softuni.bg.androiddetective.listener.IServiceCommunicationListener;
import project.android.softuni.bg.androiddetective.service.DetectiveServerService;

/**
 * Created by Milko on 22.9.2016 г..
 */

public class ServiceConnectionManager {
  private static ServiceConnection mConnection;
  private static final String TAG = "ServiceConnectionManage";

  private ServiceConnectionManager(){};
  public static ServiceConnection getInstance(final IServiceCommunicationListener callback) {
    if (mConnection == null) {
      mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
          DetectiveServerService.DetectiveServerServiceBinder serviceToOperate = (DetectiveServerService.DetectiveServerServiceBinder) service;
          serviceToOperate.getService().setServiceCallback(callback);
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
}
