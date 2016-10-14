package project.android.softuni.bg.androiddetective.gestures;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Milko on 5.10.2016 г..
 */

public class GestureFilter extends GestureDetector.SimpleOnGestureListener{
  public static final int SWIPE_UP = 1;
  public static final int SWIPE_DOWN = 2;
  public static final int SWIPE_LEFT = 3;
  public static final int SWIPE_RIGHT = 4;

  public final static int MODE_SOLID = 1;
  public final static int MODE_DYNAMIC = 2;

  private final static int ACTION_FAKE = -13; //just an unlikely number
  private int swipe_Min_Distance = 100;
  private int swipe_Max_Distance = 400;
  private int swipe_Min_Velocity = 100;

  private int mode = MODE_DYNAMIC;
  private  boolean running = true;
  private boolean tapIndicator = false;

  private Activity context;
  private GestureDetector detector;
  private SimpleGestureListener listener;

  public GestureFilter(Activity context, SimpleGestureListener listener) {
    this.context = context;
    this.detector = new GestureDetector(context, this);
    this.listener = listener;
  }

  /**
   * dispatching touch event and recognizing them
   * @param event
   */
  public void onTouchEvent(MotionEvent event) {
    if (!this.running)
      return;

    boolean result = this.detector.onTouchEvent(event);
    if (this.mode == MODE_SOLID)
      event.setAction(MotionEvent.ACTION_CANCEL);
    else if (this.mode == MODE_DYNAMIC) {
      if (event.getAction() == ACTION_FAKE)
        event.setAction(MotionEvent.ACTION_UP);
      else if (result)
        event.setAction(MotionEvent.ACTION_CANCEL);
      else if (this.tapIndicator)
        event.setAction(MotionEvent.ACTION_DOWN);
    }
  }

  public void setMode(int m){
    this.mode = m;
  }

  public int getMode(){
    return this.mode;
  }

  public void setEnabled(boolean status){
    this.running = status;
  }

  public void setSwipeMaxDistance(int distance){
    this.swipe_Max_Distance = distance;
  }

  public void setSwipeMinDistance(int distance){
    this.swipe_Min_Distance = distance;
  }

  public void setSwipeMinVelocity(int distance){
    this.swipe_Min_Velocity = distance;
  }

  public int getSwipeMaxDistance(){
    return this.swipe_Max_Distance;
  }

  public int getSwipeMinDistance(){
    return this.swipe_Min_Distance;
  }

  public int getSwipeMinVelocity(){
    return this.swipe_Min_Velocity;
  }

  /**
   * recognizing directions of swiping
   * @param me1
   * @param me2
   * @param velocityX
   * @param velocityY
   * @return boolean true of it's recognized MotionEvent
   */
  @Override
  public boolean onFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    boolean result = false;

    final float xDistance = Math.abs(me1.getX() - me2.getX());
    final float yDistance = Math.abs(me1.getY() - me2.getY());

    if ((xDistance > swipe_Max_Distance) || (yDistance > swipe_Max_Distance))
      return false;

    velocityX = Math.abs(velocityX);
    velocityY = Math.abs(velocityY);

    if(velocityX > getSwipeMinVelocity() && xDistance > getSwipeMinDistance()){
      if(me1.getX() > me2.getX()) { // right to left
        this.listener.onSwipe(SWIPE_LEFT);
      } else {
        this.listener.onSwipe(SWIPE_RIGHT);
      }
      result = true;
    }
    else if(velocityY > getSwipeMinVelocity() && yDistance > getSwipeMinDistance()){
      if(me1.getY() > me2.getY()) { // bottom to up
        this.listener.onSwipe(SWIPE_UP);
      } else {
        this.listener.onSwipe(SWIPE_DOWN);
      }
      result = true;
    }
    return result;
  }

  /**
   * OnSingleTap MotionEvent
   * @param e - MotionEvent
   * @return true if event is consumed
   */
  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    this.tapIndicator = true;
    return false;
  }

  /**
   * OnDoubleTap MotionEvent
   * @param e - MotionEvent
   * @return boolean true if event is consumed
   */
  @Override
  public boolean onDoubleTap(MotionEvent e) {
    this.listener.onDoubleTap();
    return true;
  }

  /**
   * Notified when an event within a double-tap gesture occurs, including the down, move, and up events
   * @param e MotionEvent
   * @return boolean true if the event is consumed, else false
   */
  @Override
  public boolean onDoubleTapEvent(MotionEvent e) {
    Toast.makeText(context, "onDoubleTapEvent", Toast.LENGTH_SHORT).show();

    return super.onDoubleTapEvent(e);
  }

  /**
   * We owe an ACTION_UP, so we fake an setAction(ACTION_FAKE);
   * action which will be converted to an ACTION_UP later.
   * @param e The down motion event of the single-tap
   * @return true if the event is consumed, else false
   */
  @Override
  public boolean onSingleTapConfirmed(MotionEvent e) {
    if(this.mode == MODE_DYNAMIC){
      e.setAction(ACTION_FAKE);
      this.context.dispatchTouchEvent(e);
    }
    return false;
  }

  /**
   * Notified when a long press occurs with the initial on down MotionEvent that trigged it.
   * @param e The initial on down motion event that started the longpress.
   */
  @Override
  public void onLongPress(MotionEvent e) {
    listener.onLongPress();
    super.onLongPress(e);
  }

  /**
   * callback interface
   */
  public interface SimpleGestureListener{
    void onSwipe(int direction);
    void onDoubleTap();
    void onLongPress();
  }
}
