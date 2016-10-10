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

public class NetworkChangeReceiver extends BroadcastReceiver {

  public NetworkChangeReceiver() {
  }

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

    } else {
      Toast.makeText(context, "Internet connection is Off", Toast.LENGTH_LONG).show();
      Intent service = new Intent(context, DetectiveServerService.class);
      context.stopService(service);
    }
  }
}
