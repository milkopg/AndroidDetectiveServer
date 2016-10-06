package project.android.softuni.bg.androiddetective.fragment.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.Constants;

/**
 * Created by Milko on 6.10.2016 г..
 */

public class CameraFragment extends Fragment{
  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recycleview_image_row, container, false);
    Bundle bundle = new Bundle();
    bundle.putString(Constants.BROADCAST_NAME, Constants.RECEIVER_SMS_RECEIVED);
    bundle.putInt(Constants.LAYOUT_ID, R.layout.fragment_camera);

    return rootView;
  }
}
