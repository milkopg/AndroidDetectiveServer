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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import project.android.softuni.bg.androiddetective.activity.CameraGridDetailsActivity;
import project.android.softuni.bg.androiddetective.adapter.CameraGridViewAdapter;
import project.android.softuni.bg.androiddetective.adapter.DrawerItemCustomAdapter;
import project.android.softuni.bg.androiddetective.data.DataModel;
import project.android.softuni.bg.androiddetective.fragment.menu.CallerFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.CameraGridFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.ContactsFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.GpsFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.ReadSmsFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.SettingsFragment;
import project.android.softuni.bg.androiddetective.gestures.GestureFilter;
import project.android.softuni.bg.androiddetective.listener.IOnImageClickListener;
import project.android.softuni.bg.androiddetective.service.DetectiveServerService;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.util.ServiceConnectionManager;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class MainActivity extends AppCompatActivity implements IOnImageClickListener
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
  private int mDrawerPosition;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    app = (MainApplication) getApplication();
    mTitle = mDrawerTitle = getTitle();
    mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);
    DataModel[] mDrawerItem = setupDrawerItems();

    setupToolbar();

    setupDrawerItems();

    setupDrawerAdapter(mDrawerList, mDrawerLayout, mDrawerItem);

    setupDrawerToggle();

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
              Manifest.permission.INTERNET)) {
       }
    }

    setupService();

    int menuId = getIntent().hasExtra(Constants.MENU_ID) ? getIntent().getIntExtra(Constants.MENU_ID, 1) : 1;

    selectItem(menuId);

    checkNotificationIntent(mDrawerItem);
  }

 @Override
  public void onClick(AdapterView<?> adapter, int position) {
    ResponseObject item = (ResponseObject) adapter.getItemAtPosition(position);

    //Create intent
    Intent intent = new Intent(MainActivity.this, CameraGridDetailsActivity.class);
    intent.putExtra(Constants.INTENT_TITLE, item.getImageName());
    intent.putExtra(Constants.INTENT_IMAGE, item.getImagePath() + "/" + item.getImageName());

    //Start details activity
    startActivity(intent);
  }

  @Override
  public boolean onLongClick(CameraGridViewAdapter adapter,List<ResponseObject> mAdapterData, int position) {
    ResponseObject item = mAdapterData.get(position);
    File file = new File(item.getImagePath() + "/" + item.getImageName());
    file.setWritable(true);
    String counter = item.getImageName().replaceAll("\\D+","");
    String imageNameThumb = Constants.RABBIT_MQ_IMAGES_THUMBNAIL_PREFIX + Integer.parseInt(counter) + ".jpg";
    File fileThumb = new File(item.getImagePath() + "/" +imageNameThumb);
    fileThumb.setWritable(true);
    if (file.getAbsoluteFile().delete() || fileThumb.getAbsoluteFile().delete()) {
      mAdapterData.remove(position);
      ResponseObject.delete(item);
      mDrawerList.invalidateViews();
      adapter.notifyDataSetChanged();
      Toast.makeText(this,  String.format(getResources().getString(R.string.file_was_deleted_succesfull) + " %s", file.getName()), Toast.LENGTH_SHORT).show();
    }
    return false;
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
        fragment = new CameraGridFragment();
        break;
      case 4:
        fragment = new ContactsFragment();
        break;
      case 5:
        fragment = new GpsFragment();
        break;

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
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }


  /**
   * setupToolBar for DrawerItem
   */
  void setupToolbar() {
    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  /**
   * Setup ActionBarDrawerToggle item, closed and opened state
   */
  void setupDrawerToggle() {
    mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
    //This is necessary to change the icon of the Drawer Toggle upon state change.
    mDrawerToggle.syncState();
  }

  /**
   * DataModel array holding Drawer Menus
   * @return DataModel array
   */
  private DataModel[] setupDrawerItems() {
    DataModel[] drawerItem = new DataModel[6];

    drawerItem[0] = new DataModel(R.mipmap.settings, getString(R.string.menu_settings), getString(R.string.menu_settings));
    drawerItem[1] = new DataModel(R.mipmap.sms, getString(R.string.menu_read_sms), Constants.RECEIVER_SMS_RECEIVED);
    drawerItem[2] = new DataModel(R.mipmap.call, getString(R.string.read_call_info), Constants.RECEIVER_CALL);
    drawerItem[3] = new DataModel(R.mipmap.camera, getString(R.string.camera_pictures), Constants.RECEIVER_CAMERA);
    drawerItem[4] = new DataModel(R.mipmap.contact, getString(R.string.contacts), Constants.RECEIVER_CONTACTS);
    drawerItem[5] = new DataModel(R.mipmap.call, getString(R.string.gps_places), Constants.RECEIVER_GPS);
    return drawerItem;
  }

  /**
   * Setup DrawerAdapter and adding DrawerListener
   * @param mDrawerList
   * @param mDrawerLayout
   * @param drawerItem
   */
  private void setupDrawerAdapter(ListView mDrawerList, DrawerLayout mDrawerLayout, DataModel[] drawerItem) {
    DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_menu_item, drawerItem);
    mDrawerList.setAdapter(adapter);
    mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    mDrawerLayout.addDrawerListener(mDrawerToggle);
  }

  /**
   * setup DetectiveServerService, bind and start it
   */
  private void setupService() {
    Intent service = new Intent(this, DetectiveServerService.class);
    mConnection = ServiceConnectionManager.getInstance();
    bindService(service, mConnection, Context.BIND_AUTO_CREATE);
    startService(service);
  }

  /**
   * Replace current fragment with new Fragment and setting appropriate drawer position in new Fragment
   * @param fragment new Fragment
   * @param layoutId
   * @param drawerPosition
   */
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
      mDrawerPosition = drawerPosition;
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
}
