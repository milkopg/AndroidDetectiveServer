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
  public int onStartCommand(final Intent intent, int flags, int startId) {
    server = RabbitMQServer.getInstance(getBaseContext());
    //server = new RabbitMQServer();

    new Thread(new Runnable() {
      @Override
      public void run() {
        Object response = server.receiveMessage();

//        if (response instanceof String) {
//          ResponseObject responseObject = GsonManager.convertGsonStringToObject(response.toString());
//          if ((responseObject != null) && (responseObject.uuid != null)) {
//            if (responseObject != null) {
//              ResponseBase.getDataMap().put(responseObject.uuid, responseObject);
//              responseObject.save();
//              NotificationManagerLocal.getInstance(getBaseContext()).showNotification(responseObject);
//            }
//            ResponseBase.getDataMap().put(responseObject.uuid, responseObject);
//            responseObject.save();
//          }
//        } else {
//          Bitmap bitmap = BitmapUtil.getImage((byte[]) response);
//          List<Counters> counters = Counters.find(Counters.class, null, null, null, "counter DESC", "1");
//          long counter = counters != null && counters.size() > 0 ? counters.get(0).getCounter()  : 1;
//          Counters count = Counters.findById(Counters.class, counter);
//          count.setCounter(counter + 1);
//          count.save();
//          String imageName = "image_" + counter;
//          String imagePath = BitmapUtil.saveToInternalStorage(getBaseContext(), bitmap, imageName);
//          ResponseObject responseObject = new ResponseObject(UUID.randomUUID().toString(), Constants.RECEIVER_CAMERA, DateUtil.convertDateLongToShortDate(new Date()), "", "", 0, imageName, imagePath );
//          responseObject.save();
//        }

      }
    }).start();
   return super.onStartCommand(intent, flags, startId);
  }
}
