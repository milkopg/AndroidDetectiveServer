package project.android.softuni.bg.androiddetective.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.android.softuni.bg.androiddetective.MainActivity;
import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.gestures.GestureFilter;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class CameraGridDetailsActivity extends AppCompatActivity implements GestureFilter.SimpleGestureListener {
  private GestureFilter mDetector;
  private String title;
  private String imagePath;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.details_activity);

    mDetector = new GestureFilter(this, this);

    title = getIntent().getStringExtra(Constants.INTENT_TITLE);
    imagePath = getIntent().getStringExtra(Constants.INTENT_IMAGE);

    Bitmap bm = BitmapFactory.decodeFile(imagePath);

    TextView titleTextView = (TextView) findViewById(R.id.title);
    titleTextView.setText(title);

    ImageView imageView = (ImageView) findViewById(R.id.imageViewPictureDetail);
    imageView.setImageBitmap(bm);
  }

  /**
   * required for dispatching events for GestureFilter
   * @param motionEvent
   * @return boolean Return true if this event was consumed.
   */
  @Override
  public boolean dispatchTouchEvent(MotionEvent motionEvent) {
    this.mDetector.onTouchEvent(motionEvent);
    return super.dispatchTouchEvent(motionEvent);
  }

  /**
   * We are detecting SWIPE_LEFT and SWIPE_RIGHT gestures. SWIPE_LEFT go to previous picture and SWIPE_RIGHT is going to next picture.
   * Picture list is fetched from database and method is checking if it's first or last picture to avoid exceptions
   * @param direction
   */
  @Override
  public void onSwipe(int direction) {
    List<ResponseObject> currentPictureList = QueriesUtil.getResponseObjectByBroadcastNameAndImageName(Constants.RECEIVER_CAMERA, title); //ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + " LIKE '" + Constants.RECEIVER_CAMERA + "' AND " + Constants.IMAGE_NAME + " = '" + title + "'", null, null, "ID DESC", null);
    List<ResponseObject> allPicturesList = QueriesUtil.getResponseObjectByBroadcastName(Constants.RECEIVER_CAMERA);
    ResponseObject currentPicture = currentPictureList != null ? currentPictureList.get(0) : null;
    int location = -1;
    for (ResponseObject responseObject : allPicturesList) {
      if (responseObject.getId().longValue() == currentPicture.getId().longValue()) {
        location = allPicturesList.indexOf(responseObject);
        break;
      }
    }

    if (currentPicture == null) return;
    if (location == -1) return;

    switch (direction) {
      case GestureFilter.SWIPE_RIGHT:
        //check if it's from beginning until last picture index - 1
        if (location >= 0 && location < allPicturesList.size() - 1) {
          changePicture(allPicturesList.get(++location));
        }

        break;
      case GestureFilter.SWIPE_LEFT:
        //check if it's index 1 or the last index
        if (location > 0 && location <= allPicturesList.size()) {
          changePicture(allPicturesList.get(--location));
        }
        break;
    }
  }

  /**
   * Change picture in onSwipe event. Picture path is taken from ResponseObject and it's being applied to activity
   * @param response
   */
  private void changePicture(ResponseObject response) {
    Intent intent = new Intent(getBaseContext(), CameraGridDetailsActivity.class);
    intent.putExtra(Constants.INTENT_TITLE, response.getImageName());
    intent.putExtra(Constants.INTENT_IMAGE, response.getImagePath() + "/" + response.getImageName());
    //set flags to avoid pressing back button to go for previous picture. In this way is going directly to Grid Image View
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
    startActivity(intent);
    this.finish();
  }

  @Override
  /**
   * Goto pictures menu
   */
  public void onDoubleTap() {
    gotoMenu(3);
  }

  /**
   * Goto settings menu
   */
  @Override
  public void onLongPress() {
    gotoMenu(0);
  }

  /**
   * Goto Menu by specific menuID
   * @param menuID
   */
  private void gotoMenu(int menuID) {
    Intent intent = new Intent(getBaseContext(), MainActivity.class);
    intent.putExtra(Constants.MENU_ID, menuID);
    startActivity(intent);
    this.finish();
  }
}
