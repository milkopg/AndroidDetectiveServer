package project.android.softuni.bg.androiddetective.util;

/**
 * Created by Milko on 24.9.2016 г..
 */

public class Constants {

  public static final String SETTING_JSON_BLOB_API_URL_DB_NAME = "jsonBlobApiUrl";
  public static final String SETTING_JSON_BLOB_API_URL_VALUE = "https://jsonblob.com/api/jsonBlob";
  public static final String SETTING_JSON_BLOB_API_URL_STRING_NAME = "json_blob_api_url";

  public static final String RABBIT_MQ_CONTENT_TYPE = "image";
  public static final String SETTING_RABBIT_MQ_URI_DB_NAME = "rabbitMqUri";
  public static final String SETTING_RABBIT_MQ_URI_VALUE = "amqp://jdkiyofw:BQl1KMaDSs-6VQbaGM7AO-dhPrvw_Soe@wildboar.rmq.cloudamqp.com/jdkiyofw";
  public static final String SETTING_RABBIT_MQ_URI_STRING_NAME = "rabbit_mq_uri";
  //public static final String SETTING_RABBIT_MQ_URI_VALUE = "amqp://rzqmutrv:1kFbX3tXuCWzpRPy86neVmQhuscZ31Yg@hare.rmq.cloudamqp.com/rzqmutrv";

  public static final String SETTING_RABBIT_MQ_QUEUE_NAME_DB_NAME = "rpc_queue";
  public static final String SETTING_RABBIT_MQ_QUEUE_NAME_VALUE = "rpc_queue";
  public static final String SETTING_RABBIT_MQ_QUEUE_NAME_STRING_NAME = "rabbit_mq_queue_name";

  public static final String INTENT_IMAGE = "image";
  public static final String INTENT_TITLE = "title";


  public static final String SETTING_RABBIT_MQ_IMAGES_PREFIX_VALUE = "image_";
  public static final String RABBIT_MQ_IMAGES_THUMBNAIL_PREFIX = "image_thumbsnail_";
  public static final String RABBIT_MQ_IMAGES_FOLDER = "images";

  public static final String HTTP_HEADER_CONTENT_TYPE = "Content-type";
  public static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
  public static final String HTTP_HEADER_HOST = "Host";
  public static final String HTTP_HEADER_ACCEPT = "Accept";

  public static final String HTTP_REQUEST_METHOD_GET = "GET";

  public static final String HTTP_HEADER_CONTENT_TYPE_JSON = "application/json";
  public static final String HTTP_HEADER_HOST_JSONBLOB = "jsonblob.com";

  public static final String RECEIVER_CALL = "CallBroadcastReceiver";
  public static final String RECEIVER_SMS_RECEIVED = "SmsReceivedBroadcastReceiver";
  public static final String RECEIVER_CAMERA = "CameraReceiver";
  public static final String RECEIVER_CONTACTS = "ContactObserver";
  public static final String RECEIVER_GPS = "GpsReceiver";

  public static final String BROADCAST_NAME = "BROADCAST_NAME";
  public static final String IMAGE_NAME = "IMAGE_NAME";
  public static final String DATABASE_NAME = "android_detective_server.db";
  public static final String LAYOUT_ID = "layout_id";

  public static final String DATE_FORMAT_SHORT_DATE_TIME = "yyyy-MM-dd HH:mm";
  public static final String DATE_FORMAT_SHORT_DATE = "dd-MM-yyyy";

  public static final String ORDER_BY_ASC = "ASC";
  public static final String ORDER_BY_DESC = "DESC";

  public static final String COLUMN_CONTACT_NAME = "NAME";
  public static final String COLUMN_CONTACT_PHONE_NUMBER = "PHONE_NUMBER";
  public static final String COLUMN_CONTACT_EMAIL = "EMAIL";
  public static final String COLUMN_RESPONSE_OBJECT_DATE = "DATE";
  public static final String COLUMN_RESPONSE_OBJECT_SEND_TO = "SEND_TO";
  public static final String COLUMN_RESPONSE_OBJECT_SEND_TEXT = "SEND_TEXT";
  public static final String COLUMN_SETTING_RESOURCE_NAME = "RESOURCE_NAME";

  public static final String ENCODING_UTF8 = "UTF-8";

  public static final String MENU_ID = "MENU_ID";
}
