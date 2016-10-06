package project.android.softuni.bg.androiddetective.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import project.android.softuni.bg.androiddetective.listener.IServiceCommunicationListener;
import project.android.softuni.bg.androiddetective.rabbitmq.RabbitMQServer;
import project.android.softuni.bg.androiddetective.util.BitmapUtil;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.util.NotificationManagerLocal;
import project.android.softuni.bg.androiddetective.webapi.model.Counters;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class DetectiveServerService extends Service {
  //private IServiceCommunicationListener callback;
  private RabbitMQServer server;
  private IBinder binder = new DetectiveServerServiceBinder();

  public DetectiveServerService() {
  }

//  public void setServiceCallback(IServiceCommunicationListener listener) {
//    callback = listener;
//  }

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
