package project.android.softuni.bg.androiddetective.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Milko on 5.10.2016 г..
 */

public class BitmapUtil {
  private static final String TAG = BitmapUtil.class.getSimpleName();

  /**
   * Convert byte array to BitMap
   * @param image
   * @return
   */
  public static Bitmap getImage(byte[] image) {
    return BitmapFactory.decodeByteArray(image, 0, image.length);
  }

  /**
   * Save bitmap to internal storage
   * @param context
   * @param bitmapImage
   * @param fileName
   * @return path of Image
   */
  public static String saveToInternalStorage(Context context, Bitmap bitmapImage, String fileName){
    ContextWrapper cw = new ContextWrapper(context);
    // path to /data/data/yourapp/app_data/imageDir
    File directory = cw.getDir(Constants.RABBIT_MQ_IMAGES_FOLDER, Context.MODE_PRIVATE);
    // Create imageDir
    File mypath=new File(directory, fileName);

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(mypath);
      // Use the compress method on the BitMap object to write image to the OutputStream
      bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
    } catch (Exception e) {
      Log.e(TAG, "saveToInternalStorage Cannot open FileOutputStream: " + e);
    } finally {
      try {
        fos.close();
      } catch (IOException e) {
        Log.e(TAG, "saveToInternalStorage Cannot close FileOutputStream: " + e);
      }
    }
    return directory.getAbsolutePath();
  }
}
