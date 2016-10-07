package project.android.softuni.bg.androiddetective.rabbitmq;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import project.android.softuni.bg.androiddetective.util.BitmapUtil;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.util.NotificationManagerLocal;
import project.android.softuni.bg.androiddetective.webapi.model.Counters;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 26.9.2016 г..
 */

public class RabbitMQServer {
  private static final String RPC_QUEUE_NAME = Constants.RABBIT_MQ_REQUES_QUEUE_NAME;
  private static final String TAG = RabbitMQServer.class.getSimpleName();
  private static RabbitMQServer instance;
  private static Context mContext;

  public RabbitMQServer(Context context) {
    this.mContext = context;
  }

  public void receiveMessage() {
    Channel channel;
    Connection connection = null;
    ConnectionFactory factory = new ConnectionFactory();
    byte[] responseArray;
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
        responseArray = delivery.getBody();

        BasicProperties props = delivery.getProperties();
        BasicProperties replyProps = new BasicProperties
                .Builder()
                .contentType(props.getContentType())
                .correlationId(props.getCorrelationId())
                .build();

        try {
          if (isJsonMessage(replyProps.getContentType())) {
            response = processRegularResponse(responseArray);
          } else {
            processImageResponse(responseArray, replyProps.getCorrelationId());
          }

        } catch (Exception e) {
          System.out.println(" [.] " + e.toString());
          response = "";
        } finally {
          try {
            channel.basicPublish("", props.getReplyTo(), replyProps, (isJsonMessage(replyProps.getContentType()) ? response.getBytes("UTF-8") : delivery.getBody()));
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
  }

  private String processRegularResponse(byte[] response) {
    String message = null;
    try {
      message = new String(response, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    Log.d(TAG, "Received message: " + message);
    ResponseObject responseObject = GsonManager.convertGsonStringToObject(message.toString());
    persistObjectAndShowNotification(responseObject);
    return message;
  }

  private void processImageResponse(byte[] response, String correlationId) {
    String inputLine;
    StringBuilder builder = new StringBuilder();

    BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response)));
    try {
      while ((inputLine = in.readLine()) != null) {
        builder.append(inputLine);
      }
    } catch (IOException e) {
      Log.e(TAG, "processImageResponse: " + e);
    }

    byte[] deserializedByteArray = Base64.decode(builder.toString(), Base64.NO_WRAP);
    Bitmap bitmap = BitmapUtil.getImage(deserializedByteArray);
    List<Counters> counters = Counters.find(Counters.class, null, null, null, "counter DESC", "1");
    long counter = counters != null && counters.size() > 0 ? counters.get(0).getCounter() + 1 : 1;
    Counters count = Counters.findById(Counters.class, counter);
    if (count == null)
      count = new Counters(Constants.RECEIVER_CAMERA, counter );

    count.save();
    String imageName = Constants.RABBIT_MQ_IMAGES_PREFIX + counter + ".jpg";
    if (bitmap == null) return;

    String imagePath = BitmapUtil.saveToInternalStorage(mContext, bitmap, imageName);
    ResponseObject responseObject = new ResponseObject(correlationId, Constants.RECEIVER_CAMERA, DateUtil.convertDateLongToShortDate(new Date()), "", "", 0, imageName, imagePath);
    persistObjectAndShowNotification(responseObject);
  }

  private void persistObjectAndShowNotification(ResponseObject responseObject) {
    if ((responseObject != null) && (responseObject.getUuid() != null)) {
      responseObject.save();
      NotificationManagerLocal.getInstance(mContext).showNotification(responseObject);
    }
  }

  private boolean isJsonMessage(String contentType) {
    if (contentType == null) return true;
    return !(contentType != null && contentType.equals(Constants.RABBIT_MQ_CONTENT_TYPE));
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
