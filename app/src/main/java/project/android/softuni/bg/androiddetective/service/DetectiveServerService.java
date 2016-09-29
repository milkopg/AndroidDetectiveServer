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
    server = new  RabbitMQServer();

    new Thread(new Runnable() {
      @Override
      public void run() {

        ResponseObject responseObject = GsonManager.convertGsonStringToObject(server.receiveMessage());
        if ((responseObject != null) && (responseObject.id != null))
          ResponseObject.getDataMap().put(responseObject.id, responseObject);
      }
    }).start();

//    RabbitMQServer server = RabbitMQServer.getInstance();
//    String responseJson = server.receiveMessage();
//    ResponseObject responseObject = GsonManager.convertGsonStringToObject(responseJson);
//    if ((responseObject != null) && (responseObject.id != null))
//      ResponseObject.getDataMap().put(responseObject.id, responseObject);

    return super.onStartCommand(intent, flags, startId);
  }

}
