package project.android.softuni.bg.androiddetective;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;

import com.orm.SugarContext;
import com.orm.SugarDb;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.DateUtil;
import project.android.softuni.bg.androiddetective.webapi.model.Counters;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;
import project.android.softuni.bg.androiddetective.webapi.model.Setting;

/**
 * Created by Milko on 3.10.2016 г..
 */

public class MainApplication extends Application {
  private static final String TAG = MainApplication.class.getSimpleName();

  @Override
  public void onCreate() {
    super.onCreate();
    boolean doesDatabaseExists = databaseExists(this, Constants.DATABASE_NAME);
    Log.d(TAG, "onCreate() doesDatabaseExists: " + doesDatabaseExists);

    if (!doesDatabaseExists) {
      SugarDb sugarDb = new SugarDb(getApplicationContext());
      SugarContext.init(getApplicationContext());
      new File(sugarDb.getDB().getPath()).delete();
      ResponseObject init = new ResponseObject(UUID.randomUUID().toString(), Constants.RECEIVER_CALL, DateUtil.convertDateLongToShortDate(new Date()), "1234", "Send TExt", 0, null, null);
      init.save();
      Counters counter = new Counters(Constants.RECEIVER_CAMERA, 1L);
      counter.save();

      initSettingList();
    } else {
      SugarContext.init(getApplicationContext());
    }
  }

  /**
   * Insert base setting for jsonBlobUrl, RabbitMQ uri and queue_name
   */
  private void initSettingList() {
    Setting.deleteAll(Setting.class);
    Setting setting = new Setting(Constants.SETTING_JSON_BLOB_API_URL_DB_NAME, Constants.SETTING_JSON_BLOB_API_URL_VALUE, Constants.SETTING_JSON_BLOB_API_URL_STRING_NAME);
    setting.save();

    setting = new Setting(Constants.SETTING_RABBIT_MQ_URI_DB_NAME, Constants.SETTING_RABBIT_MQ_URI_VALUE, Constants.SETTING_RABBIT_MQ_URI_STRING_NAME);
    setting.save();

    setting = new Setting(Constants.SETTING_RABBIT_MQ_QUEUE_NAME_DB_NAME, Constants.SETTING_RABBIT_MQ_QUEUE_NAME_DB_NAME, Constants.SETTING_RABBIT_MQ_QUEUE_NAME_STRING_NAME);
    setting.save();
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }

  /**
   * check if current database exists
   * @param context
   * @param dbName
   * @return boolean if database exists
   */
  private boolean databaseExists(ContextWrapper context, String dbName) {
    File dbFile = context.getDatabasePath(dbName);

    return dbFile.exists();
  }
}
