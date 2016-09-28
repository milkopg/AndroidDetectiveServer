package project.android.softuni.bg.androiddetective.rabbitmq;

import android.util.Log;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import project.android.softuni.bg.androiddetective.util.Constants;

/**
 * Created by Milko on 26.9.2016 г..
 */

public class RabbitMQServer {
  private static final String RPC_QUEUE_NAME = Constants.RABBIT_MQ_REQUES_QUEUE_NAME;
  private static final String TAG = RabbitMQServer.class.getSimpleName();
  private static RabbitMQServer instance;
  Connection connection = null;
  Channel channel = null;
  String message = null;

  public RabbitMQServer() {
    ConnectionFactory factory = new ConnectionFactory();

    try {
      factory.setAutomaticRecoveryEnabled(true);
      factory.setHost(Constants.RABBIT_MQ_URI);
//      factory.setUsername("jdkiyofw");
//      factory.setPassword("BQl1KMaDSs-6VQbaGM7AO-dhPrvw_Soe");
//      factory.setHost("wildboar.rmq.cloudamqp.com");
      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

      channel.basicQos(1);

    }catch (Exception e) {
      Log.e(TAG, "Cannot open RabbitMQ Connection: " + e);
    }
  }

  public String receiveMessage() {
//    Connection connection = null;
//    Channel channel = null;
    String message = null;
    ConnectionFactory factory = new ConnectionFactory();
    try {
//      factory.setAutomaticRecoveryEnabled(true);
//      factory.setHost(Constants.RABBIT_MQ_URI);
//
//      connection = factory.newConnection();
//      channel = connection.createChannel();
//
//      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
//
//      channel.basicQos(1);

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

          response = message;
        } catch (Exception e) {
          System.out.println(" [.] " + e.toString());
          response = "";
        } finally {
          channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
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

  public static RabbitMQServer getInstance() {
    if (instance == null)
      instance = new RabbitMQServer();
    return instance;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return null;
  }
}
