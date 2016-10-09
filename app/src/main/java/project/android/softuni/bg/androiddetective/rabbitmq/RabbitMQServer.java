package project.android.softuni.bg.androiddetective.rabbitmq;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.util.Base64;
import android.util.Log;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.Contact;
import project.android.softuni.bg.androiddetective.webapi.model.Counters;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * Created by Milko on 26.9.2016 г..
 */

public class RabbitMQServer {
  private static final String TAG = RabbitMQServer.class.getSimpleName();
  private static RabbitMQServer instance;
  private static Context mContext;
  private String rabbitMqQueueName = QueriesUtil.getNotNullValue(QueriesUtil.getSettingByDatabaseKey(Constants.SETTING_RABBIT_MQ_QUEUE_NAME_STRING_NAME).getValue(), Constants.SETTING_RABBIT_MQ_QUEUE_NAME_VALUE);
  private String rabbitMQUri = QueriesUtil.getNotNullValue(QueriesUtil.getSettingByDatabaseKey(Constants.SETTING_RABBIT_MQ_URI_STRING_NAME).getValue(), Constants.SETTING_RABBIT_MQ_URI_VALUE);

  public RabbitMQServer(Context context) {
    RabbitMQServer.mContext = context;
  }

  public void receiveMessage() {
    Channel channel;
    Connection connection = null;
    ConnectionFactory factory = new ConnectionFactory();
    byte[] responseArray;
    int retry = 0;
    try {
      factory.setAutomaticRecoveryEnabled(true);
      factory.setUri(rabbitMQUri);

      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.queueDeclare(rabbitMqQueueName, false, false, false, null);
      channel.basicQos(1);

      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(rabbitMqQueueName, false, consumer);

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
      if (retry < 5) {
        retry++;
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e1) {
          Log.e(TAG, "Thread cannot sleep " + e1);
        }
        receiveMessage();
      }

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
    String deserializedString = deserializeByArrayToString(response);
    byte[] deserializedByteArray = Base64.decode(deserializedString, Base64.NO_WRAP);

    Bitmap bitmap = BitmapUtil.getImage(deserializedByteArray);
    Bitmap bitmapThumbNail = BitmapUtil.getImage(deserializedByteArray);

    List<Counters> counters = Counters.find(Counters.class, null, null, null, "counter DESC", "1");
    long counter = counters != null && counters.size() > 0 ? counters.get(0).getCounter() : 1;
    Counters count = Counters.findById(Counters.class, counter);
    if (count == null) {
      count = new Counters(Constants.RECEIVER_CAMERA, counter );
    } else {
      counter++;
      count.setCounter(counter);
    }
    count.save();

    String imageName = Constants.SETTING_RABBIT_MQ_IMAGES_PREFIX_VALUE + counter + ".jpg";
    String imageNameThumbnails = Constants.RABBIT_MQ_IMAGES_THUMBNAIL_PREFIX + counter + ".jpg";
    if (bitmap == null) return;

    String imagePath = BitmapUtil.saveToInternalStorage(mContext, bitmap, imageName);
    String imagePathThum = BitmapUtil.saveToInternalStorage(mContext, ThumbnailUtils.extractThumbnail(bitmapThumbNail, 100, 100), imageNameThumbnails);
    Log.d(TAG, "processImageResponse: imagePath "  + imagePath);
    Log.d(TAG, "processImageResponse: imagePathThum "  + imagePathThum);

    ResponseObject responseObject = new ResponseObject(correlationId, Constants.RECEIVER_CAMERA, DateUtil.convertDateLongToShortDate(new Date()), "", imageNameThumbnails, 0, imageName, imagePath);
    persistObjectAndShowNotification(responseObject);
  }

//  private void createThumbnailBitmap(String fullPath) {
//    Bitmap thumbnail = null;
//    File thumbnailFile;
//    FileOutputStream fos = null;
//    try {
//      thumbnailFile = new File(fullPath);
//      fos = new FileOutputStream(thumbnailFile);
//      thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, fos);
//      fos.flush();
//      fos.close();
//    } catch (FileNotFoundException e) {
//      Log.e(TAG, "createThumbnailBitmap: FileNotFoundException " + e);
//    } catch (IOException e) {
//      Log.e(TAG, "createThumbnailBitmap: IOException " + e);
//    }
//
//  }

  private String deserializeByArrayToString(byte [] response) {
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

    return builder.toString();
  }

  private void persistObjectAndShowNotification(ResponseObject responseObject) {
    if ((responseObject != null) && (responseObject.getUuid() != null)) {
      List<Contact> contacts = responseObject.getContacts();
      if (contacts != null && contacts.size() > 0) {
        Contact.deleteAll(Contact.class);
        for (Contact contact : contacts) {
          contact.save();
        }
      }
      responseObject.save();
      Log.d(TAG, "persistObjectAndShowNotification object persisted " + GsonManager.convertObjectToGsonString(responseObject));
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
