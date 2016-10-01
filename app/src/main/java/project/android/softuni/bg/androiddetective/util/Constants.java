package project.android.softuni.bg.androiddetective.util;

/**
 * Created by Milko on 24.9.2016 г..
 */

public class Constants {
  public static final String WEB_API_URL = "https://jsonblob.com/api/jsonBlob";
  public static final String RABBIT_MQ_URI = "amqp://jdkiyofw:BQl1KMaDSs-6VQbaGM7AO-dhPrvw_Soe@wildboar.rmq.cloudamqp.com/jdkiyofw";
  public static final String RABBIT_MQ_URI2 = "amqp://rzqmutrv:1kFbX3tXuCWzpRPy86neVmQhuscZ31Yg@hare.rmq.cloudamqp.com/rzqmutrv";
  public static final String RABBIT_MQ_REQUES_QUEUE_NAME = "rpc_queue";

  public static final String HTTP_HEADER_CONTENT_TYPE = "Content-type";
  public static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
  public static final String HTTP_HEADER_HOST = "Host";
  public static final String HTTP_HEADER_ACCEPT = "Accept";
  public static final String HTTP_HEADER_LOCATION = "Location";


  public static final String HTTP_REQUEST_METHOD_POST = "POST";
  public static final String HTTP_REQUEST_METHOD_GET = "GET";

  public static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";
  public static final String HTTP_HEADER_HOST_JSONBLOB = "jsonblob.com";
  public static final String HTTP_HEADER_ACCEPT_JSON = HTTP_HEADER_CONTENT_TYPE_JSON;

  public static final String RECEIVER_CALL = "CallBroadcastReceiver";
  public static final String RECEIVER_CALL_OUTGOING_CALL = "OutgoingCallBroadcastReceiver";
  public static final String RECEIVER_SMS_SENT = "SmsDeliverBroadcastReceiver";
  public static final String RECEIVER_SMS_RECEIVED = "SmsReceivedBroadcastReceiver";

  public static final String BROADCAST_NAME = "broacast_name";
}
