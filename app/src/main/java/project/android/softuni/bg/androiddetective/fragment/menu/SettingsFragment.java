package project.android.softuni.bg.androiddetective.fragment.menu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.android.softuni.bg.androiddetective.R;
import project.android.softuni.bg.androiddetective.util.Constants;
import project.android.softuni.bg.androiddetective.webapi.model.Contact;
import project.android.softuni.bg.androiddetective.webapi.model.ResponseObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

//    List<ResponseObject> cameras= ResponseObject.find(ResponseObject.class, Constants.BROADCAST_NAME + "=?", Constants.RECEIVER_CAMERA);
//    for (ResponseObject responseObject : cameras){
//      responseObject.delete();
//    }

    return inflater.inflate(R.layout.fragment_settings, container, false);
  }
}
