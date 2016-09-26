package project.android.softuni.bg.androiddetective.rabbitmq;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Milko on 25.9.2016 г..
 */

public class RabbitMQClient2 {
  private ConnectionFactory factory;
  private Thread publishThread;
  private Thread subscribeThread;
  private Handler handler;
  private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();
  private String uri;
  private List<String> messageList = new ArrayList<>();

  public RabbitMQClient2(ConnectionFactory factory, String uri, Handler handler) {
    this.factory = factory;
    this.uri = uri;
    this.handler = handler;
    setupConnectionFactory();
   // publishToAMQP();
    subscribe(handler);
  }



  private void setupConnectionFactory() {
    //String uri = "CLOUDAMQP_URL";
    //String uri = "amqp://jdkiyofw:BQl1KMaDSs-6VQbaGM7AO-dhPrvw_Soe@wildboar.rmq.cloudamqp.com/jdkiyofw";
    try {
      factory.setAutomaticRecoveryEnabled(false);
      factory.setUri(this.uri);
    } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
      Log.e("setupConnectionFactory", e1.getLocalizedMessage());
      e1.printStackTrace();
    }
  }

  public void publishToAMQP() {
    publishThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Connection connection = factory.newConnection();
            Channel ch = connection.createChannel();
            ch.confirmSelect();

            while (true) {
              String message = queue.takeFirst();
              try {
                ch.basicPublish("amq.fanout", "chat", null, message.getBytes());
                Log.d("", "[s] " + message);
                ch.waitForConfirmsOrDie();
                messageList.add(message);
              } catch (Exception e) {
                Log.d("", "[f] " + message);
                queue.putFirst(message);
                throw e;
              }
            }
          } catch (InterruptedException e) {
            Log.e("publishToAMQP", e.toString());
            break;
          } catch (Exception e) {
            Log.d("", "Connection broken: " + e.toString());

            try {
              Thread.sleep(5000); //sleep and then try again
              break;
            } catch (InterruptedException e1) {
              break;
            }
          }
        }
      }
    });
    publishThread.start();
  }

  void subscribe(final Handler handler) {
    subscribeThread = new Thread(new Runnable() {
      @Override
      public void run() {
        while(true) {
          try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicQos(1);
            AMQP.Queue.DeclareOk q = channel.queueDeclare();
            channel.queueBind(q.getQueue(), "amq.fanout", "chat");
            QueueingConsumer consumer = new QueueingConsumer(channel);
            channel.basicConsume(q.getQueue(), true, consumer);

            // Process deliveries
            while (true) {
              QueueingConsumer.Delivery delivery = consumer.nextDelivery();

              String message = new String(delivery.getBody());
              Log.d("","[r] " + message);

              Message msg = handler.obtainMessage();
              Bundle bundle = new Bundle();

              bundle.putString("msg", message);
              msg.setData(bundle);
              handler.sendMessage(msg);
            }
          } catch (InterruptedException e) {
            Log.d("InterruptedException1", "InterruptedException: " + e.getLocalizedMessage());
            break;
          } catch (Exception e1) {
            Log.d("Exception", "Connection broken: " + e1.getLocalizedMessage());
            try {
              Thread.sleep(4000); //sleep and then try again
            } catch (InterruptedException e) {
              Log.e("InterruptedException2", e.getLocalizedMessage());
              break;
            }
          }
        }
      }
    });
    subscribeThread.start();
  }
}
