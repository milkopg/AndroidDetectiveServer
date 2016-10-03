package project.android.softuni.bg.androiddetective.rabbitmq;

import android.content.Context;
import android.util.Log;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.util.NotificationManagerLocal;
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

  public String receiveMessage() {
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
          message = new String(delivery.getBody(), "UTF-8");
          Log.d(TAG, "Received message: " + message);
          response = message;
          ResponseObject responseObject = GsonManager.convertGsonStringToObject(response);
          if (responseObject != null) {
            ResponseBase.getDataMap().put(responseObject.uuid, responseObject);
            NotificationManagerLocal.getInstance(mContext).showNotification(responseObject);
          }
        } catch (Exception e) {
          System.out.println(" [.] " + e.toString());
          response = "";
        } finally {
          try {
            channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
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
