package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;
import project.android.softuni.bg.androiddetective.webapi.task.RetrieveTask;

/**
 * Created by Milko G on 22/09/2016.
 */
public class ReadSmsFragment extends Fragment {
  private static final String TAG = ReadSmsFragment.class.getSimpleName();
  private TextView mTextViewReadSms;
  private ConcurrentHashMap<String, ResponseObject> dataMap;
  private ConnectionFactory rabbitMqFactory;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_read_sms, container, false);
    mTextViewReadSms = (TextView) rootView.findViewById(R.id.text_view_read_sms);
    setTextViewText();

//    rabbitMqFactory = new ConnectionFactory();
//    final Handler incomingMessageHandler = new Handler() {
//      @Override
//      public void handleMessage(Message msg) {
//        String message = msg.getData().getString("msg");
//        Date now = new Date();
//        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
//        mTextViewReadSms.append(ft.format(now) + ' ' + message + '\n');
//        mTextViewReadSms.setText(ft.format(now) + ' ' + message + '\n');
//      }
//    };
//    mTextViewReadSms = (TextView) rootView.findViewById(R.id.text_view_read_sms);
//    mTextViewReadSms.post(new Runnable() {
//      @Override
//      public void run() {
//       // new SmsRetrieveTask("57e7827ae4b0dc55a4f8ef4a").execute();
//        new RabbitMQClient2(rabbitMqFactory, Constants.RABBIT_MQ_URI, incomingMessageHandler);
//      }
//    });
//    new Thread(new Runnable() {
//      @Override
//      public void run() {
//        receiveMessage();
//      }
//    }).start();

    return rootView;
  }

  private void receiveMessage() {
    String RPC_QUEUE_NAME = "hello";
    Connection connection = null;
    Channel channel = null;
    try {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setAutomaticRecoveryEnabled(true);
      factory.setUri("amqp://jdkiyofw:BQl1KMaDSs-6VQbaGM7AO-dhPrvw_Soe@wildboar.rmq.cloudamqp.com/jdkiyofw");

      connection = factory.newConnection();
      channel = connection.createChannel();

      channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);

      channel.basicQos(1);

      QueueingConsumer consumer = new QueueingConsumer(channel);
      channel.basicConsume(RPC_QUEUE_NAME, false, consumer);

      System.out.println(" [x] Awaiting RPC requests");

      while (true) {
        String response = null;

        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

        AMQP.BasicProperties props = delivery.getProperties();
        AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                .Builder()
                .correlationId(props.getCorrelationId())
                .build();

        try {
          String message = new String(delivery.getBody(), "UTF-8");
          // int n = Integer.parseInt(message);
          mTextViewReadSms.setText(message);
//          mTextViewReadSms.postInvalidate();
          System.out.println(" [.] fib(" + message + ")");


        } catch (Exception e) {
          System.out.println(" [.] " + e.toString());
          response = "";
        } finally {
          channel.basicPublish("", props.getReplyTo(), replyProps, response.getBytes("UTF-8"));

          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(TAG, "Cannot open connection " + e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception ignore) {
        }
      }
    }
  }

  /*private */class SmsRetrieveTask extends RetrieveTask {

    public SmsRetrieveTask(String blobId) {
      super(blobId);
    }

    @Override
    protected void onPostExecute(String response) {
      super.onPostExecute(response);
      //setTextViewText();
    }
  }

  private void setTextViewText() {
    dataMap = ResponseBase.getDataMap();
    if (dataMap == null) return;
    if (mTextViewReadSms == null) return;
    StringBuilder builder = new StringBuilder();

    for (ConcurrentHashMap.Entry<String, ResponseObject> entry : dataMap.entrySet()) {
      builder.append("Id:").append(entry.getValue().id).append("\n");
      builder.append("Broadcast name: ").append(entry.getValue().broadcastName).append("\n");
      builder.append("Date: ").append(entry.getValue().date).append("\n");
      builder.append("Send to: ").append(entry.getValue().sendTo).append("\n");
      builder.append("Send text: ").append(entry.getValue().sendText).append("\n");
      builder.append("Notes ").append(entry.getValue().notes).append("\n");
    }
    mTextViewReadSms.setText(builder.toString());
  }
}

//}
