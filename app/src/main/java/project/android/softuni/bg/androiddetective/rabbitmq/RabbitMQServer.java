package project.android.softuni.bg.androiddetective.rabbitmq;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import project.android.softuni.bg.androiddetective.util.BitmapUtil;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.util.NotificationManagerLocal;
import project.android.softuni.bg.androiddetective.webapi.model.Counters;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 26.9.2016 г..
 */

public class RabbitMQServer {
  private static final String RPC_QUEUE_NAME = Constants.RABBIT_MQ_REQUES_QUEUE_NAME;
  private static final String TAG = RabbitMQServer.class.getSimpleName();
  private static final String RABBIT_MQ_URI = Constants.RABBIT_MQ_URI;
  private static RabbitMQServer instance;
  private static Context mContext;

  public RabbitMQServer(Context context) {
    this.mContext = context;
  }

  public Object receiveMessage() {
    String message = null;
    Channel channel;
    Connection connection = null;
    ConnectionFactory factory = new ConnectionFactory();
    try {
      factory.setAutomaticRecoveryEnabled(true);
      factory.setUri(Constants.RABBIT_MQ_URI);

      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

      channel.basicQos(1);

      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

      System.out.println(" [x] Awaiting RPC requests");
      Log.d(TAG, " [x] Awaiting RPC requests");

      while (true) {
        String response = null;

        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

        BasicProperties props = delivery.getProperties();
        BasicProperties replyProps = new BasicProperties
                .Builder()
                .correlationId(props.getCorrelationId())
                .build();

        try {
          if (isJsonMessage(delivery.getBody())) {
            message = new String(delivery.getBody(), "UTF-8");
            Log.d(TAG, "Received message: " + message);
            response = message;

            ResponseObject responseObject = GsonManager.convertGsonStringToObject(response.toString());
            if ((responseObject != null) && (responseObject.uuid != null)) {
              ResponseBase.getDataMap().put(responseObject.uuid, responseObject);
              responseObject.save();
              ResponseBase.getDataMap().put(responseObject.uuid, responseObject);
              responseObject.save();
              NotificationManagerLocal.getInstance(mContext).showNotification(responseObject);

            }

          } else {


            Bitmap bitmap = BitmapUtil.getImage(delivery.getBody());
            List<Counters> counters = Counters.find(Counters.class, null, null, null, "counter DESC", "1");
            long counter = counters != null && counters.size() > 0 ? counters.get(0).getCounter()  : 1;
            Counters count = Counters.findById(Counters.class, counter);
            count.setCounter(counter + 1);
            count.save();
            String imageName = "image_" + counter;
            String imagePath = BitmapUtil.saveToInternalStorage(mContext, bitmap, imageName);
            ResponseObject responseObject = new ResponseObject(UUID.randomUUID().toString(), Constants.RECEIVER_CAMERA, DateUtil.convertDateLongToShortDate(new Date()), "", "", 0, imageName, imagePath );
            responseObject.save();
            NotificationManagerLocal.getInstance(mContext).showNotification(responseObject);

            //delivery.getBody();
          }

        } catch (Exception e) {
          System.out.println(" [.] " + e.toString());
          response = "";
        } finally {
          try {
            channel.basicPublish("", props.getReplyTo(), replyProps, (isJsonMessage(delivery.getBody()) ? response.getBytes("UTF-8") : delivery.getBody()));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          } catch (IOException e) {
            Log.e(TAG, "receiveMessage: cannot basic publish or basicAck - " + e);
          }

        }
      }
    } catch (Exception e) {
      Log.e(TAG, "Cannot open RabbitMQ Connection: " + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception ignore) {
          Log.e(TAG, "Cannot close RabbitMQ Connection: " + ignore);
        }
      }
    }
    return message;
  }

  private boolean isJsonMessage (byte[] message) {
    return (message != null && message.length < 50000);
  }

  public static RabbitMQServer getInstance(Context context) {
    mContext = context;
    if (instance == null)
      instance = new RabbitMQServer(context);
    return instance;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return null;
  }
}
