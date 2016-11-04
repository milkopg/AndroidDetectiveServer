package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.fragment.base.BaseFragment;
import project.android.softuni.bg.androiddetective.util.Constants;

/**
 * Created by Milko on 4.11.2016 г..
 */

public class GpsFragment extends BaseFragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Bundle bundle = new Bundle();
    bundle.putString(Constants.BROADCAST_NAME, Constants.RECEIVER_GPS);
    bundle.putInt(Constants.LAYOUT_ID, R.layout.fragment_table_data);
    return super.onCreateView(inflater, container, bundle);
  }
}
