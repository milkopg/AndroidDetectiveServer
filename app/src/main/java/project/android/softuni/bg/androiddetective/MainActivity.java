package project.android.softuni.bg.androiddetective;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import project.android.softuni.bg.androiddetective.activity.DetailsActivity;
import project.android.softuni.bg.androiddetective.adapter.DrawerItemCustomAdapter;
import project.android.softuni.bg.androiddetective.data.DataModel;
import project.android.softuni.bg.androiddetective.fragment.menu.CallerFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.CameraFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.CameraGridFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.ReadSmsFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.SettingsFragment;
import project.android.softuni.bg.androiddetective.gestures.GestureFilter;
import project.android.softuni.bg.androiddetective.listener.IOnImageClickListener;
import project.android.softuni.bg.androiddetective.listener.IServiceCommunicationListener;
import project.android.softuni.bg.androiddetective.service.DetectiveServerService;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.util.ServiceConnectionManager;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class MainActivity extends AppCompatActivity implements IServiceCommunicationListener, GestureFilter.SimpleGestureListener,IOnImageClickListener
{

  private String[] mNavigationDrawerItemTitles;
  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private Toolbar toolbar;
  private CharSequence mDrawerTitle;
  private CharSequence mTitle;
  private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
  private ServiceConnection mConnection;
  private MainApplication app;
  private GestureFilter detector;

  @Override
  public void receiveJsonData(String data) {
    ResponseObject responseObject = GsonManager.convertGsonStringToObject(data);
    responseObject.save();
    ResponseBase.getDataMap().put(responseObject.getUuid(), responseObject);
  }

  @Override
  public void onClick(AdapterView<?> adapter, int position) {
    ResponseObject item = (ResponseObject) adapter.getItemAtPosition(position);

    //Create intent
    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
    intent.putExtra("title", item.imageName);
    intent.putExtra("image", item.imagePath + "/" + item.imageName);

    //Start details activity
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    app = (MainApplication) getApplication();
    //setTitle(getString(R.string.android_detective_server));

    mTitle = mDrawerTitle = getTitle();
    mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    DataModel[] mDrawerItem = setupDrawerItems();
    detector = new GestureFilter(this, this);

    setupToolbar();

    setupDrawerItems();

    setupDrawerAdapter(mDrawerList, mDrawerLayout, mDrawerItem);

    setupDrawerToggle();



    if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

      // Should we show an explanation?
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.INTERNET)) {

        // Show an expanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

      }/* else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }*/
    }

    setupService();

    checkNotificationIntent(mDrawerItem);
  }



  @Override
  public boolean dispatchTouchEvent(MotionEvent motionEvent) {
    this.detector.onTouchEvent(motionEvent);
    return super.dispatchTouchEvent(motionEvent);
  }

  @Override
  public void onSwipe(int direction) {
    String text = "";
    int position = 0;

    switch (direction) {
      case GestureFilter.SWIPE_RIGHT :
        text = getString(R.string.swipe_right);
        break;
      case GestureFilter.SWIPE_LEFT :
        text = getString(R.string.swipe_left);
        break;
      case GestureFilter.SWIPE_DOWN :
        text = getString(R.string.swipe_down);
        position = 1;
        replaceFragment(getFragmentByPosition(position), R.id.content_frame, position);
        break;
      case GestureFilter.SWIPE_UP :
        text = getString(R.string.swipe_up);
        position = 2;
        replaceFragment(getFragmentByPosition(position), R.id.content_frame, position);
        break;
    }
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onDoubleTap() {
    //TODO
  }

  @Override
  public void onLongPress() {
    int position = 0;
    replaceFragment(getFragmentByPosition(position), R.id.content_frame, position);
  }


  private void checkNotificationIntent(DataModel[] mDrawerItem) {
    String receiverName;
    if (getIntent().hasExtra(Constants.BROADCAST_NAME)) {
      receiverName = getIntent().getStringExtra(Constants.BROADCAST_NAME);
      int position = getFragmentPositionByReceiverName(receiverName, mDrawerItem);
      Fragment fragment = getFragmentByPosition(position);
      replaceFragment(fragment, R.id.content_frame, position);
    }
  }

  private class DrawerItemClickListener implements ListView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      selectItem(position);
    }

  }

  private void selectItem(int position) {
    Fragment fragment = getFragmentByPosition(position);

    replaceFragment(fragment, R.id.content_frame, position);
  }

  private Fragment getFragmentByPosition(int position) {
    Fragment fragment = null;

    switch (position) {
      case 0:
        fragment = new SettingsFragment();
        break;
      case 1:
        fragment = new ReadSmsFragment();
        break;
      case 2:
        fragment = new CallerFragment();
        break;
      case 3:
        //fragment = new CameraFragment();
        fragment = new CameraGridFragment();
        break;
      case 4:
        fragment = new CameraGridFragment();

      default:
        break;
    }
    return fragment;
  }

  private  int getFragmentPositionByReceiverName(String name, DataModel[] drawerItem) {
    if ((drawerItem == null) || (name == null)) return 0;
    for (int i=0; i < drawerItem.length; i++) {
      if (name.equals(drawerItem[i].getReceiverName())) {
        return i;
      }
    }
    return 0;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void setTitle(CharSequence title) {
    mTitle = title;
    getSupportActionBar().setTitle(mTitle);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  void setupToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  void setupDrawerToggle() {
    mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
    //This is necessary to change the icon of the Drawer Toggle upon state change.
    mDrawerToggle.syncState();
  }

  private DataModel[] setupDrawerItems() {
    DataModel[] drawerItem = new DataModel[4];

    drawerItem[0] = new DataModel(R.mipmap.settings, getString(R.string.menu_settings), getString(R.string.menu_settings));
    drawerItem[1] = new DataModel(R.mipmap.sms, getString(R.string.menu_read_sms), Constants.RECEIVER_SMS_RECEIVED);
    drawerItem[2] = new DataModel(R.mipmap.call, getString(R.string.read_call_info), Constants.RECEIVER_CALL);
    drawerItem[3] = new DataModel(R.mipmap.camera, getString(R.string.camera_pictures), Constants.RECEIVER_CAMERA);
    return drawerItem;
  }

  private void setupService() {
    Intent service = new Intent(this, DetectiveServerService.class);
    mConnection = ServiceConnectionManager.getInstance(MainActivity.this);
    bindService(service, mConnection, Context.BIND_AUTO_CREATE);
    startService(service);
  }

  private void setupDrawerAdapter(ListView mDrawerList, DrawerLayout mDrawerLayout, DataModel[] drawerItem) {
    DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_menu_item, drawerItem);
    mDrawerList.setAdapter(adapter);
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerLayout.addDrawerListener(mDrawerToggle);
  }

  private void replaceFragment(Fragment fragment, int layoutId, int drawerPosition) {
    if (fragment != null) {
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

      //fragmentTransaction.addToBackStack(null);
      fragmentTransaction.replace(layoutId, fragment).commit();
      mDrawerList.setItemChecked(drawerPosition, true);
      mDrawerList.setSelection(drawerPosition);
      setTitle(mNavigationDrawerItemTitles[drawerPosition]);
      mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
      mDrawerLayout.closeDrawer(mDrawerList);

    } else {
      Log.e("MainActivity", "Error in creating fragment");
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    if (mConnection != null) {
      unbindService(mConnection);
    }


}

 private static class MyPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
      Log.d("TAG", "PINCH! OUCH!");
      return true;
    }
  }

}
