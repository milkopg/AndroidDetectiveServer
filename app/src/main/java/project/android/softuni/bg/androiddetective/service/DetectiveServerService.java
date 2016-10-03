package project.android.softuni.bg.androiddetective.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import project.android.softuni.bg.androiddetective.listener.IServiceCommunicationListener;
import project.android.softuni.bg.androiddetective.rabbitmq.RabbitMQServer;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class DetectiveServerService extends Service {
  private IServiceCommunicationListener callback;
  private RabbitMQServer server;
  private IBinder binder = new DetectiveServerServiceBinder();

  public DetectiveServerService() {
  }

  public void setServiceCallback(IServiceCommunicationListener listener) {
    callback = listener;
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

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    server = RabbitMQServer.getInstance(getBaseContext());
    //server = new RabbitMQServer();

    new Thread(new Runnable() {
      @Override
      public void run() {
        String response = server.receiveMessage();
        ResponseObject responseObject = GsonManager.convertGsonStringToObject(response);
        if ((responseObject != null) && (responseObject.id != null))
          ResponseBase.getDataMap().put(responseObject.id, responseObject);
      }
    }).start();
   return super.onStartCommand(intent, flags, startId);
  }
}
