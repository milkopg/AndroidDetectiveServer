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

  /**
   * Sending unique data (broadcastName and layoutId) for BaseFragment and then inflate it
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Bundle bundle = new Bundle();
    bundle.putString(Constants.BROADCAST_NAME, Constants.RECEIVER_SMS_RECEIVED);
    bundle.putInt(Constants.LAYOUT_ID, R.layout.fragment_table_data);
    return super.onCreateView(inflater, container, bundle);
  }
}