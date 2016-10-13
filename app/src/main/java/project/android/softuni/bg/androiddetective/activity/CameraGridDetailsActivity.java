package project.android.softuni.bg.androiddetective.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.gestures.GestureFilter;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.QueriesUtil;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class CameraGridDetailsActivity extends AppCompatActivity implements GestureFilter.SimpleGestureListener {
  private GestureFilter mDetector;
  String title;
  String imagePath;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.details_activity);

    mDetector = new GestureFilter(this, this);

    title = getIntent().getStringExtra("title");
    imagePath = getIntent().getStringExtra("image");


    Bitmap bm = BitmapFactory.decodeFile(imagePath);

    TextView titleTextView = (TextView) findViewById(R.id.title);
    titleTextView.setText(title);

    ImageView imageView = (ImageView) findViewById(R.id.imageViewPictureDetail);
    imageView.setImageBitmap(bm);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent motionEvent) {
    this.mDetector.onTouchEvent(motionEvent);
    return super.dispatchTouchEvent(motionEvent);
  }

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

  private void changePicture(ResponseObject response) {
    Intent intent = new Intent(getBaseContext(), CameraGridDetailsActivity.class);
    intent.putExtra("title", response.getImageName());
    intent.putExtra("image", response.getImagePath() + "/" + response.getImageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
    startActivity(intent);
    this.finish();
  }

  @Override
  public void onDoubleTap() {

  }

  @Override
  public void onLongPress() {

  }
}
