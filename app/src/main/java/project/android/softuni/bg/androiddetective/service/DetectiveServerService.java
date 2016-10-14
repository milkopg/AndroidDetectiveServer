package project.android.softuni.bg.androiddetective.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import project.android.softuni.bg.androiddetective.rabbitmq.RabbitMQServer;

public class DetectiveServerService extends Service {
  private RabbitMQServer server;
  private IBinder binder = new DetectiveServerServiceBinder();

  public DetectiveServerService() {
  }

  public class DetectiveServerServiceBinder extends Binder {
    public DetectiveServerService getService() {
      return DetectiveServerService.this;
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return binder;
  }

  /**
   * Once service is started it receive RabbitMQ data
   * @param intent
   * @param flags
   * @param startId
   * @return
   */
  @Override
  public int onStartCommand(final Intent intent, int flags, int startId) {
    server = RabbitMQServer.getInstance(getBaseContext());

    new Thread(new Runnable() {
      @Override
      public void run() {
        server.receiveMessage();
      }
    }).start();
    return super.onStartCommand(intent, flags, startId);
  }
}
