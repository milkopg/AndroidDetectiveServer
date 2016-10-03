package project.android.softuni.bg.androiddetective;

import android.app.Application;
import android.content.ContextWrapper;
import android.util.Log;

import com.orm.SugarContext;
import com.orm.SugarDb;

import java.io.File;

import project.android.softuni.bg.androiddetective.util.Constants;

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
      new File(sugarDb.getDB().getPath()).delete();
    }

    SugarContext.init(getApplicationContext());
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
