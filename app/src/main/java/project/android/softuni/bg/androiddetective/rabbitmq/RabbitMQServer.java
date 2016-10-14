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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
  int retry = 0;

  /**
   * RabbitMQ messaging queueing system. RabbitMQ is an open source message broker. It receives and delivers messages from and to your applications.
   * A message broker is (unlike databases and key-value store) purpose built to highly effectively and safely deliver information between your applications.
   * For more info https://www.rabbitmq.com/
   * We use CloudAMQP for free RabbitMQ message provider. I have Little Lemur account for development with Max 1 Million messages per month
   Max 20 concurrent connections
   Max 100 queues
   Max 10 000 queued messages
   * https://www.cloudamqp.com/
   *
   * @param context
   */
  public RabbitMQServer(Context context) {
    RabbitMQServer.mContext = context;
  }

  /**
   * This method creates new RabbitMq connection and channel. It create delivery and downloading delivery content. It recognized
   * if it's String (Json) or image byte array download. If delivery is not possible download already backuped data from JSONBlob api.
   * It retries 5 times to reconnect with 10 seconds timeout, if connection is failed , network connection listener will start it.
   */
  public void receiveMessage() {
    Channel channel;
    Connection connection = null;
    ConnectionFactory factory = new ConnectionFactory();
    byte[] responseArray;
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
                .messageId(props.getMessageId())
                .build();

        try {
          if (isJsonMessage(replyProps.getContentType())) {
            response = processRegularResponse(responseArray);
            if (response == null) {
              response = new String(responseArray, Constants.ENCODING_UTF8);
              responseArray = getJsonBlobData(generateJsonBlobUrl(replyProps.getMessageId()), null, response);
              processRegularResponse(responseArray);
            }

          } else {
            response = processImageResponse(responseArray, replyProps.getMessageId());
            if (response == null) {
              responseArray = getJsonBlobData(generateJsonBlobUrl(replyProps.getMessageId()), responseArray, null);
              processImageResponse(responseArray, replyProps.getMessageId());
            }
          }

        } catch (Exception e) {
          System.out.println(" [.] " + e.toString());
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
      Log.e(TAG, "Cannot open RabbitMQ Connection: retry " + retry);
      if (retry < 5) {
        retry++;
        try {
          Thread.sleep(10000);
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

  /**
   * generate JsonBlobUrl by adding URL + messageId
   * @param messageId
   * @return generated Url
   */
  private String generateJsonBlobUrl(String messageId) {
    return Constants.SETTING_JSON_BLOB_API_URL_VALUE + "/" + messageId;
  }

  /**
   * process Relugar String message. It convert it to normal UTF-8 string and then to ResponseObject using GsonManager
   * @param response byte array of regular String response
   * @return converted Json String
   */
  private synchronized String processRegularResponse(byte[] response) {
    String message;
    if (response == null) return null;
    try {
      message = new String(response, Constants.ENCODING_UTF8);
    } catch (UnsupportedEncodingException e) {
      Log.e(TAG, "processRegularResponse " + e);
      return null;
    }
    Log.d(TAG, "Received message: " + message);
    ResponseObject responseObject = GsonManager.convertGsonStringToObject(message.toString());
    persistObjectAndShowNotification(responseObject);
    return message;
  }

  /**
   * Process image response. Image byte array is being deserialized as a String and then String is decoded to deserialized Base64 byte array
   * 2 images are saved. Original one and Thumbnail. Thumbnail is used for performance at run time. Original one is used in details sceen.
   * Once images are persisted Notification is shown.
   * @param response raw byte array image
   * @param messageId of generated picture stored in JsonBlob
   * @return imagePath of new created picture in the server
   */
  private synchronized String processImageResponse(byte[] response, String messageId) {
    if (response == null) return null;
    String deserializedString = deserializeByArrayToString(response);
    byte[] deserializedByteArray = Base64.decode(deserializedString, Base64.NO_WRAP);

    Bitmap bitmap = BitmapUtil.getImage(deserializedByteArray);
    Bitmap bitmapThumbNail = BitmapUtil.getImage(deserializedByteArray);

    List<Counters> counters = Counters.find(Counters.class, null, null, null, "ID DESC", "1");
    long id = counters != null && counters.size() > 0 ? counters.get(0).getId() : 1;
    Counters count = Counters.findById(Counters.class, id);
    if (count == null) {
      Counters.deleteAll(Counters.class);
      count = new Counters(Constants.RECEIVER_CAMERA, id );
    } else {
      Counters.deleteAll(Counters.class);
      count = new Counters(Constants.RECEIVER_CAMERA, id );
      id++;
      count.setCounter(id);
    }
    count.save();

    String imageName = Constants.SETTING_RABBIT_MQ_IMAGES_PREFIX_VALUE + id + ".jpg";
    String imageNameThumbnails = Constants.RABBIT_MQ_IMAGES_THUMBNAIL_PREFIX + id + ".jpg";
    if (bitmap == null) return null;

    String imagePath = BitmapUtil.saveToInternalStorage(mContext, bitmap, imageName);
    String imagePathThum = BitmapUtil.saveToInternalStorage(mContext, ThumbnailUtils.extractThumbnail(bitmapThumbNail, 100, 100), imageNameThumbnails);
    Log.d(TAG, "processImageResponse: imagePath "  + imagePath);
    Log.d(TAG, "processImageResponse: imagePathThum "  + imagePathThum);

    ResponseObject responseObject = new ResponseObject(messageId, Constants.RECEIVER_CAMERA, DateUtil.convertDateLongToShortDate(new Date()), "", imageNameThumbnails, 0, imageName, imagePath);
    persistObjectAndShowNotification(responseObject);
    return imagePath;
  }

  /**
   * Get already saved data in JsonBlob service in case that rabbitMQ cannot download data
   * For more information: https://jsonblob.com/api#get
   * @param url on JsonBlob instance
   * @param binaryData - imageData
   * @param rawData - JsonData
   * @return - data in byte array
   */
  protected synchronized byte [] getJsonBlobData(String url, byte[] binaryData, String rawData) {
    if (binaryData == null && rawData == null) return  null;
    if (url == null) return null;
    HttpURLConnection conn = initHttpConnection(url, Constants.HTTP_HEADER_CONTENT_TYPE_JSON);
    try {
      if (conn.getResponseCode() == 200) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = conn.getInputStream().read(data, 0, data.length)) != -1) {
          buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
      }

    } catch (MalformedURLException e) {
      Log.e(TAG, "MalformedURLException" + e);
    } catch (ProtocolException e) {
      Log.e(TAG, "ProtocolException" + e);
    } catch (IOException e) {
      Log.e(TAG, "ProtocolException" + e);
    }
    return null;
  }

  /**
   * JsonBlob is free API for sending jsonObjects in free Cloud, and it's returning unique JsobBlobId after record is inserted.
   * For more information https://jsonblob.com/api
   * @param url - of JsonBlob
   * @param mimeType - mimeType of url Connection
   * @return HttpUrlConnection
   */
  private HttpURLConnection initHttpConnection(String url, String mimeType) {
    HttpURLConnection conn = null;
    URL u = null;
    try {
      u = new URL(url);
      conn = (HttpURLConnection) u.openConnection();
      conn.setRequestMethod(Constants.HTTP_REQUEST_METHOD_GET);

      conn.setRequestProperty(Constants.HTTP_HEADER_CONTENT_TYPE, Constants.HTTP_HEADER_CONTENT_TYPE_JSON);
      conn.setRequestProperty(Constants.HTTP_HEADER_HOST, Constants.HTTP_HEADER_HOST_JSONBLOB);
      conn.setRequestProperty(Constants.HTTP_HEADER_ACCEPT, mimeType);
    } catch (MalformedURLException e) {
      Log.e(TAG, "initHttpConnection MalformedURLException: " + e);
    } catch (ProtocolException e) {
      Log.e(TAG, "initHttpConnection ProtocolException: " + e);
    } catch (IOException e) {
      Log.e(TAG, "initHttpConnection IOException: " + e);
    }
    return conn;
  }

  /**
   * Deserialize normal byte[] array response to regular String
   * @param response
   * @return
   */
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

  /**
   * Persist response Object and Show Notification. It checks if contact data is saved and persist it.
   * @param responseObject
   */
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

  /**
   * check if contentType is null - it's json message, otherwise is picture.
   * @param contentType
   * @return
   */
  private boolean isJsonMessage(String contentType) {
    if (contentType == null) return true;
    return !(contentType != null && contentType.equals(Constants.RABBIT_MQ_CONTENT_TYPE));
  }

  /**
   * get SingleTop instance of RabbitMQ server
   * @param context
   * @return RabbitMQ isntance
   */
  public static RabbitMQServer getInstance(Context context) {
    mContext = context;
    if (instance == null)
      instance = new RabbitMQServer(context);
    return instance;
  }

  /**
   * disable cloning. Needed for singleton
   * @return
   * @throws CloneNotSupportedException
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    return null;
  }
}
