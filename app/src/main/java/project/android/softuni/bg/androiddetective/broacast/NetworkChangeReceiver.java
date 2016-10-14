package project.android.softuni.bg.androiddetective.broacast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import project.android.softuni.bg.androiddetective.service.DetectiveServerService;
import project.android.softuni.bg.androiddetective.util.ServiceConnectionManager;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

public class NetworkChangeReceiver extends BroadcastReceiver {
  public static final String TAG = NetworkChangeReceiver.class.getSimpleName();

  public NetworkChangeReceiver() {
  }

  /**
   * Custom NetworkStateChangeListener. It listen it internet connection is off or on.
   * If it's on it starts Service if it's off stop service
   * @param context
   * @param intent for Broadcast
   */
  @Override
  public void onReceive(Context context, Intent intent) {
    Bundle extras = intent.getExtras();

    NetworkInfo info = extras.getParcelable("networkInfo");

    NetworkInfo.State state = info.getState();
    Log.d("TEST Internet", info.toString() + " " + state.toString());

    if (state == NetworkInfo.State.CONNECTED) {
      Toast.makeText(context, "Internet connection is on", Toast.LENGTH_LONG).show();
      Intent service = new Intent(context, DetectiveServerService.class);
      context.startService(service);
      Log.d(TAG, "Internet connection is on: service started");
    } else {
      Toast.makeText(context, "Internet connection is Off", Toast.LENGTH_LONG).show();
      Intent service = new Intent(context, DetectiveServerService.class);
      context.stopService(service);
      Log.d(TAG, "Internet connection is off: service stopped");
    }
  }
}
