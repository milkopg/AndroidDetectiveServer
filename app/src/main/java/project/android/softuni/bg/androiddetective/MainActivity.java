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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import project.android.softuni.bg.androiddetective.adapter.DrawerItemCustomAdapter;
import project.android.softuni.bg.androiddetective.data.DataModel;
import project.android.softuni.bg.androiddetective.fragment.menu.ReadSmsFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.CallerFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.SettingsFragment;
import project.android.softuni.bg.androiddetective.fragment.menu.TableFragment;
import project.android.softuni.bg.androiddetective.listener.IServiceCommunicationListener;
import project.android.softuni.bg.androiddetective.service.DetectiveServerService;
import project.android.softuni.bg.androiddetective.util.GsonManager;
import project.android.softuni.bg.androiddetective.util.ServiceConnectionManager;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseBase;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

public class MainActivity extends AppCompatActivity implements IServiceCommunicationListener{

    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    @Override
    public void receiveJsonData(String data) {
        ResponseObject responseObject = GsonManager.convertGsonStringToObject(data);
        ResponseBase.getDataMap().put(responseObject.id, responseObject);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        setupToolbar();

        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel(R.mipmap.settings, getString(R.string.menu_settings));
        drawerItem[1] = new DataModel(R.mipmap.sms, getString(R.string.menu_read_sms));
        drawerItem[2] = new DataModel(R.mipmap.call, getString(R.string.read_call_info));
        drawerItem[3] = new DataModel(R.drawable.table, "Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_menu_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();



        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        Intent service = new Intent(this, DetectiveServerService.class);
        ServiceConnection connection = ServiceConnectionManager.getInstance(MainActivity.this);
        bindService(service, connection, Context.BIND_AUTO_CREATE);
        startService(service);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                RabbitMQServer server = RabbitMQServer.getInstance();
//                String responseJson = server.receiveMessage();
//                ResponseObject responseObject = GsonManager.convertGsonStringToObject(responseJson);
//                if ((responseObject != null) && (responseObject.id != null))
//                    ResponseObject.getDataMap().put(responseObject.id, responseObject);
//            }
//        }).start();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

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
                fragment = new TableFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
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

    void setupToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

}
