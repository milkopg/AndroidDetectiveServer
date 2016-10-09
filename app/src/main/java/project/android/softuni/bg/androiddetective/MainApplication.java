package project.android.softuni.bg.androiddetective;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;

import com.orm.SugarContext;
import com.orm.SugarDb;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

  private void initSettingList() {
    Setting setting = new Setting(Constants.SETTING_JSON_BLOB_API_URL_DB_NAME, Constants.SETTING_JSON_BLOB_API_URL_VALUE, R.string.json_blob_api_url);
    setting.save();

    setting = new Setting(Constants.SETTING_RABBIT_MQ_URI_DB_NAME, Constants.SETTING_RABBIT_MQ_URI_VALUE, R.string.rabbit_mq_uri);
    setting.save();

    setting = new Setting(Constants.SETTING_RABBIT_MQ_QUEUE_NAME_DB_NAME, Constants.SETTING_RABBIT_MQ_QUEUE_NAME_DB_NAME, R.string.rabbit_mq_queue_name);
    setting.save();
  }

  private void initDb() {

  }

  @Override
  public void onTerminate() {
    super.onTerminate();
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }

  private boolean databaseExists(ContextWrapper context, String dbName) {
    File dbFile = context.getDatabasePath(dbName);

    return dbFile.exists();
  }
}
