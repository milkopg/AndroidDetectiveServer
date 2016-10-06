package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.fragment.base.BaseFragment;
import project.android.softuni.bg.androiddetective.util.Constants;

/**
 * Created by Milko G on 22/09/2016.
 */
public class ReadSmsFragment extends BaseFragment {
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    Bundle bundle = new Bundle();
    bundle.putString(Constants.BROADCAST_NAME, Constants.RECEIVER_SMS_RECEIVED);
    bundle.putInt(Constants.LAYOUT_ID, R.layout.fragment_table_data);
    return super.onCreateView(inflater, container, bundle);
  }
}